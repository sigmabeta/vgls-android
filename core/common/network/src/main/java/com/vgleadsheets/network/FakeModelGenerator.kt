package com.vgleadsheets.network

import com.vgleadsheets.logging.Hatchet
import com.vgleadsheets.network.model.ApiComposer
import com.vgleadsheets.network.model.ApiSong
import com.vgleadsheets.network.model.VglsApiGame
import java.io.IOException
import java.util.EmptyStackException
import java.util.Random
import java.util.Stack
import javax.inject.Inject
import javax.inject.Named

@Suppress("TooManyFunctions", "UnusedPrivateMember")
class FakeModelGenerator @Inject constructor(
    private val random: Random,
    @Named("RngSeed") private val seed: Long,
    private val stringGenerator: StringGenerator,
    private val hatchet: Hatchet,
) {
    var possibleTags: Map<String, List<String>>? = null
        get() {
            if (field == null) {
                generateModels()
            }
            return field
        }

    var possibleSongs: List<ApiSong>? = null
        get() {
            if (field == null) {
                generateModels()
            }
            return field
        }

    var possibleComposers: List<ApiComposer>? = null
        get() {
            if (field == null) {
                generateModels()
            }
            return field
        }

    var possibleGames: List<VglsApiGame>? = null
        get() {
            if (field == null) {
                generateModels()
            }
            return field
        }

    var generateEmptyState = false

    var maxSongs = DEFAULT_MAX_SONGS

    var maxGames = DEFAULT_MAX_GAMES
    var maxComposers = DEFAULT_MAX_COMPOSERS
    var maxTags = DEFAULT_MAX_TAGS
    var maxTagsValues = DEFAULT_MAX_TAGS_VALUES
    var maxSongsPerGame = DEFAULT_MAX_SONGS_PER_GAME

    private var remainingSongs: Stack<ApiSong>? = null

    private fun generateModels() {
        possibleTags = null
        possibleComposers = null
        remainingSongs = null
        possibleSongs = null

        random.setSeed(seed)

        if (generateEmptyState) {
            throw IOException("Arbitrarily failed a network request!")
        }

        val gameCount = random.nextInt(maxGames)
        hatchet.i(this.javaClass.simpleName, "Generating $gameCount games...")
        val games = ArrayList<VglsApiGame>(gameCount)

        for (gameIndex in 0 until gameCount) {
            val game = generateGame()
            games.add(game)
        }

        hatchet.i(this.javaClass.simpleName, "Generated ${games.size} games...")

        val filteredGames = games
            .distinctBy { it.game_id }
            .filter { it.songs.isNotEmpty() }

        hatchet.i(this.javaClass.simpleName, "Returning ${filteredGames.size} games...")
        possibleGames = filteredGames
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

    @Suppress("SwallowedException")
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
        // hatchet.d(this.javaClass.simpleName, "Generating $songCount songs...")

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
        random.nextInt(MAX_PAGE_COUNT) + 1,
        getComposersForSong(),
        getTags(),
        listOf()
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
        // hatchet.i(this.javaClass.simpleName, "Generating $composerCount composers...")
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
        // hatchet.i(this.javaClass.simpleName, "Generating $tagCount tags...")
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

        const val MAX_WORDS_PER_TITLE = 5
        const val MAX_PAGE_COUNT = 2

        val PARTS_NO_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto")
        val PARTS_WITH_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto", "Vocals")

        val TAG_VALUES_NUMERIC = listOf("1", "2", "3", "4", "5")
    }
}
