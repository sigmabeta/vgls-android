package com.vgleadsheets.network

import com.vgleadsheets.model.ApiDigest
import com.vgleadsheets.model.composer.ApiComposer
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.ApiSetlist
import com.vgleadsheets.model.jam.ApiSetlistEntry
import com.vgleadsheets.model.jam.ApiSongHistoryEntry
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection
import java.util.EmptyStackException
import java.util.Random
import java.util.Stack
import javax.inject.Named

@Suppress("TooManyFunctions")
class MockVglsApi(
    private val random: Random,
    @Named("RngSeed") private val seed: Long,
    private val stringGenerator: StringGenerator
) : VglsApi {
    private var possibleTags: Map<String, List<String>>? = null

    private var possibleComposers: List<ApiComposer>? = null

    private var remainingSongs: Stack<ApiSong>? = null

    private var possibleSongs: List<ApiSong>? = null

    var generateEmptyState = false

    var maxSongs = DEFAULT_MAX_SONGS
    var maxGames = DEFAULT_MAX_GAMES
    var maxComposers = DEFAULT_MAX_COMPOSERS
    var maxTags = DEFAULT_MAX_TAGS
    var maxTagsValues = DEFAULT_MAX_TAGS_VALUES
    var maxSongsPerGame = DEFAULT_MAX_SONGS_PER_GAME

    var digestEmitTrigger: Observable<Long> = Single.just(0L).toObservable()
    var updateTimeEmitTrigger: Observable<Long> = Single.just(0L).toObservable()

    override fun getDigest() = digestEmitTrigger
        .flatMapSingle { Single.just(generateDigest()) }
        .firstOrError()
        .subscribeOn(Schedulers.io())

    // TODO Fill this in
    override fun getLastUpdateTime() = updateTimeEmitTrigger
        .flatMapSingle {
            Single
                .just(
                    ApiTime("2017-04-01T23:30:06Z")
                )
        }
        .firstOrError()
        .subscribeOn(Schedulers.io())

    override fun getJamState(name: String) = Observable.create<ApiJam> {
        if (possibleSongs == null) {
            generateGames()
        }

        if (name.length % 2 > 0) {
            val jsonObject = JSONObject()

            jsonObject.put("error", "Only jam names of even-numbered length are valid!")

            val body = jsonObject
                .toString()
                .toResponseBody("application/json; charset=utf-8".toMediaTypeOrNull())

            it.onError(
                HttpException(
                    Response.error<Unit>(
                        HttpURLConnection.HTTP_NOT_FOUND,
                        body
                    )
                )
            )

            return@create
        }

        val jamSeed = name.hashCode().toLong()

        val jamRandomGenerator = Random(jamSeed)
        val jamId = jamRandomGenerator.nextLong()

        val previousJamsSize = jamRandomGenerator.nextInt(JAM_PREVIOUS_SONGS_SIZE) + 1
        val previousJams = ArrayList<ApiSongHistoryEntry>(previousJamsSize)

        for (entryIndex in 0 until previousJamsSize) {
            val possibleSongsSize = possibleSongs?.size ?: 0
            val possibleSongIndex = jamRandomGenerator.nextInt(possibleSongsSize)

            Timber.w("History entry $entryIndex will be possible song with index: $possibleSongIndex")

            val song = possibleSongs?.get(possibleSongIndex)

            if (song != null) {
                val entry = ApiSongHistoryEntry(song.id)
                previousJams.add(entry)
            } else {
                Timber.e("Invalid song with index $possibleSongIndex our of possible song list size $possibleSongsSize")
            }
        }

        val jam = ApiJam(
            jamId,
            previousJams
        )

        it.onNext(jam)
    }.subscribeOn(Schedulers.io())

    @SuppressWarnings("MagicNumber")
    override fun getSetlistForJam(name: String) = Single.create<ApiSetlist> {
        if (possibleSongs == null) {
            generateGames()
        }

        val jamRandomGenerator = Random(name.hashCode().toLong())

        val setlistSize = if (name.length % 4 > 0) {
            0
        } else {
            jamRandomGenerator.nextInt(5) + 1
        }

        val songs = ArrayList<ApiSetlistEntry>(setlistSize)

        for (jamIndex in 0 until setlistSize) {
            val possibleSongsSize = possibleSongs?.size ?: 0
            val possibleSongIndex = jamRandomGenerator.nextInt(possibleSongsSize)

            val song = possibleSongs?.get(possibleSongIndex)

            if (song != null) {

                val entry = ApiSetlistEntry(song.id, "Mocks Don't Name Games Yet", song.name)
                songs.add(entry)
            } else {
                Timber.e("Invalid song with index $possibleSongIndex our of possible song list size $possibleSongsSize")
            }
        }

        val setlist = ApiSetlist(
            songs
        )

        it.onSuccess(setlist)
    }.subscribeOn(Schedulers.io())

    private fun generateDigest(): ApiDigest {
        val games = generateGames()
        val composers = possibleComposers!!

        return ApiDigest(
            composers,
            games
        )
    }

    private fun generateGames(): List<VglsApiGame> {
        possibleTags = null
        possibleComposers = null
        remainingSongs = null
        possibleSongs = null

        random.setSeed(seed)

        if (generateEmptyState) {
            throw IOException("Arbitrarily failed a network request!")
        }

        val gameCount = random.nextInt(maxGames)
        Timber.i("Generating $gameCount games...")
        val games = ArrayList<VglsApiGame>(gameCount)

        for (gameIndex in 0 until gameCount) {
            val game = generateGame()
            games.add(game)
        }

        Timber.i("Generated ${games.size} games...")

        val filteredGames = games
            .distinctBy { it.game_id }
            .filter { it.songs.isNotEmpty() }

        Timber.i("Returning ${filteredGames.size} games...")

        return filteredGames
    }

    private fun generateGame(): VglsApiGame {
        val gameId = random.nextLong()
        return VglsApiGame(
            null,
            gameId,
            stringGenerator.generateTitle(),
            getSongs(),
            null
        )
    }

    private fun getSongs(): List<ApiSong> {
        if (remainingSongs == null) {
            generateSongs()
        }

        val availableSongs = remainingSongs!!
        val songCount = random.nextInt(maxSongsPerGame) + 1

        val songs = ArrayList<ApiSong>(songCount)

        for (songIndex in 0 until songCount) {
            try {
                val song = availableSongs.pop()
                songs.add(song)
            } catch (ex: EmptyStackException) {
                return songs
            }
        }

        return songs
    }

    private fun generateSongs() {
        val songCount = random.nextInt(maxSongs) + 1
        Timber.d("Generating $songCount songs...")

        val songs = Stack<ApiSong>()
        val songIds = HashSet<Long>(songCount)

        for (songIndex in 0 until songCount) {
            val song = generateSong()
            val newId = song.id

            if (!songIds.contains(newId)) {
                songs.add(song)
                songIds.add(newId)
            }
        }

        remainingSongs = songs
        possibleSongs = songs.toMutableList()
    }

    private fun generateSong() = ApiSong(
        random.nextLong(),
        "goose",
        getParts(),
        stringGenerator.generateTitle(),
        random.nextInt(MAX_PAGE_COUNT) + 1,
        random.nextInt(MAX_PAGE_COUNT) + 1,
        getComposersForSong(),
        getTags()
    )

    @Suppress("MagicNumber")
    private fun getParts(): Set<String> {
        val randomNumber = random.nextInt(10)

        return if (randomNumber < 2) {
            PARTS_WITH_VOCALS
        } else {
            PARTS_NO_VOCALS
        }
    }

    @Suppress("MagicNumber")
    private fun getComposersForSong(): List<ApiComposer> {
        if (possibleComposers == null) {
            generateComposers()
        }

        val availableComposers = possibleComposers!!
        val randomNumber = random.nextInt(10)
        val composerCount = if (randomNumber < 1) {
            3
        } else if (randomNumber < 3) {
            2
        } else {
            1
        }

        val composers = ArrayList<ApiComposer>(composerCount)

        for (composerIndex in 0 until composerCount) {
            val whichComposer = random.nextInt(availableComposers.size - 1)
            val composer = availableComposers.get(whichComposer)
            composers.add(composer)
        }

        return composers.distinctBy { it.composer_id }
    }

    private fun generateComposers() {
        val composerCount = random.nextInt(maxComposers) + 1
        Timber.i("Generating $composerCount composers...")
        val composers = ArrayList<ApiComposer>(composerCount)

        for (composerIndex in 0 until composerCount) {
            val composer = generateComposer()
            composers.add(composer)
        }

        possibleComposers = composers.distinctBy { it.composer_id }
    }

    private fun generateComposer() = ApiComposer(
        null,
        random.nextLong(),
        stringGenerator.generateName(),
        null
    )

    private fun getTags(): Map<String, List<String>> {
        if (possibleTags == null) {
            generateTags()
        }

        val songTags = mutableMapOf<String, List<String>>()

        possibleTags!!.forEach { possibleTag ->
            val valuesSize = possibleTag.value.size
            val selectedValue = possibleTag.value[random.nextInt(valuesSize)]

            songTags.put(possibleTag.key, listOf(selectedValue))
        }

        return songTags
    }

    private fun generateTags() {
        val tagCount = random.nextInt(maxTags) + 1
        Timber.i("Generating $tagCount tags...")
        val tags = mutableMapOf<String, List<String>>()

        for (tagIndex in 0 until tagCount) {
            val tagName = stringGenerator.generateTitle()
            val isNumericTag = random.nextBoolean()

            val tagValues = if (isNumericTag) {
                TAG_VALUES_NUMERIC
            } else {
                val tagValuesCount = random.nextInt(maxTagsValues - 1) + 1
                val tagValues = ArrayList<String>(tagValuesCount)

                for (tagValueIndex in 0 until tagValuesCount) {
                    val tagValue = stringGenerator.generateTitle()
                    tagValues.add(tagValue)
                }

                tagValues
            }

            tags[tagName] = tagValues
        }

        possibleTags = tags
    }

    companion object {
        const val DEFAULT_MAX_SONGS = 50
        const val DEFAULT_MAX_GAMES = 400
        const val DEFAULT_MAX_COMPOSERS = 200
        const val DEFAULT_MAX_TAGS = 20
        const val DEFAULT_MAX_TAGS_VALUES = 10
        const val DEFAULT_MAX_SONGS_PER_GAME = 10

        const val JAM_PREVIOUS_SONGS_SIZE = 10

        const val MAX_WORDS_PER_TITLE = 5
        const val MAX_PAGE_COUNT = 2

        val PARTS_NO_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto")
        val PARTS_WITH_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto", "Vocals")

        val TAG_VALUES_NUMERIC = listOf("1", "2", "3", "4", "5")
    }
}
