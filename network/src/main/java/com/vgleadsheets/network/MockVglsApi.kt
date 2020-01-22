package com.vgleadsheets.network

import com.vgleadsheets.model.composer.ApiComposer
import com.vgleadsheets.model.game.VglsApiGame
import com.vgleadsheets.model.jam.ApiJam
import com.vgleadsheets.model.jam.ApiSetlist
import com.vgleadsheets.model.song.ApiSong
import com.vgleadsheets.model.time.ApiTime
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.EmptyStackException
import java.util.Locale
import java.util.Random
import java.util.Stack

class MockVglsApi : VglsApi {
    private val random = Random(1234L)

    private var possibleComposers: List<ApiComposer>? = null

    private var remainingSongs: Stack<ApiSong>? = null

    override fun getDigest() = Single
        .just(
            generateGames()
        )
        .subscribeOn(Schedulers.io())

    // TODO Fill this in
    override fun getLastUpdateTime() = Single
        .just(
            ApiTime("2020-01-21T03:30:06Z")
        )
        .subscribeOn(Schedulers.io())

    override fun getJamState(name: String): Observable<ApiJam> =
        Observable.error(NotImplementedError())

    override fun getSetlistForJam(name: String): Single<ApiSetlist> =
        Single.error(NotImplementedError())

    private fun generateGames(): List<VglsApiGame> {
        val gameCount = random.nextInt(MAX_GAMES)
        Timber.i("Generating $gameCount games...")
        val games = ArrayList<VglsApiGame>(gameCount)

        for (gameIndex in 0 until gameCount) {
            val game = generateGame()
            games.add(game)
        }

        return games.distinctBy { it.game_id }
    }

    private fun generateGame(): VglsApiGame {
        val gameId = random.nextLong()
        return VglsApiGame(
            gameId,
            generateTitle(),
            getSongs()
        )
    }

    private fun generateTitle(): String {
        val wordCount = random.nextInt(MAX_WORDS_PER_TITLE - 1) + 1
        val builder = StringBuilder()

        for (wordIndex in 0 until wordCount) {
            val randomWordIndex = random.nextInt(RANDOM_WORDS.size - 1)
            val randomWord = RANDOM_WORDS[randomWordIndex]
            builder.append(randomWord)
            builder.append(' ')
        }

        return builder.toString().trim()
    }

    private fun getSongs(): List<ApiSong> {
        if (remainingSongs == null) {
            generateSongs()
        }

        val availableSongs = remainingSongs!!
        val songCount = random.nextInt(MAX_SONGS_PER_GAME) + 1

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
        val songCount = random.nextInt(MAX_SONGS) + 1
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
    }

    private fun generateSong() = ApiSong(
        random.nextLong(),
        "goose",
        getParts(),
        generateTitle(),
        random.nextInt(MAX_PAGE_COUNT) + 1,
        random.nextInt(MAX_PAGE_COUNT) + 1,
        getComposers(),
        getTags()
    )

    private fun getParts(): Set<String> {
        val randomNumber = random.nextInt(10)

        return if (randomNumber < 2) {
            PARTS_WITH_VOCALS
        } else {
            PARTS_NO_VOCALS
        }
    }

    private fun getComposers(): List<ApiComposer> {
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

        return composers.distinctBy { it.id }
    }

    private fun generateComposers() {
        val composerCount = random.nextInt(MAX_COMPOSERS) + 1
        Timber.i("Generating $composerCount composers...")
        val composers = ArrayList<ApiComposer>(composerCount)

        for (composerIndex in 0 until composerCount) {
            val composer = generateComposer()
            composers.add(composer)
        }

        possibleComposers = composers.distinctBy { it.id }
    }

    private fun generateComposer() = ApiComposer(
        random.nextLong(),
        generateName()
    )

    private fun generateName() = "${FIRST_NAMES[random.nextInt(FIRST_NAMES.size)]} " +
            LAST_NAMES[random.nextInt(LAST_NAMES.size)]

    // TODO fill this in
    private fun getTags(): Map<String, List<String>> = mapOf()

    companion object {
        const val MAX_SONGS = 1000
        const val MAX_GAMES = 400
        const val MAX_COMPOSERS = 200
        const val MAX_SONGS_PER_GAME = 10
        const val MAX_WORDS_PER_TITLE = 5
        const val MAX_PAGE_COUNT = 2

        val PARTS_NO_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto")
        val PARTS_WITH_VOCALS = setOf("C", "Bb", "Eb", "F", "Bass", "Alto", "Vocals")

        @UseExperimental(ExperimentalStdlibApi::class)
        val RANDOM_WORDS = """Lorem ipsum dolor sit amet consectetur adipiscing elit Curabitur 
                |iaculis neque vel fermentum dictum Pellentesque ac justo ultricies 
                |hendrerit sem in blandit tellus Nam non congue ante In ultricies 
                |hendrerit velit at lobortis velit sodales eget Quisque quis pellentesque 
                |urna Suspendisse consequat ut tellus in sollicitudin Aliquam facilisis a 
                |justo quis iaculis Donec feugiat pharetra orci in iaculis metus tincidunt 
                |quis In eget ligula leo Integer finibus metus ut est molestie et finibus 
                |ligula convallis"""
            .trimMargin()
            .split(" ")
            .map { it.trim() }
            .map { it.capitalize(Locale.getDefault()) }
            .toList()

        val FIRST_NAMES = listOf(
            "Exie",
            "Euna",
            "Kimiko",
            "Robbin",
            "Maximina",
            "Elias",
            "Maryellen",
            "Marcene",
            "Phung",
            "Karoline",
            "Rosie",
            "Branden",
            "Eryn",
            "Kurt",
            "Cristopher",
            "Tien",
            "Kori",
            "Eldon",
            "Jeannetta",
            "Cinda",
            "William",
            "Alona",
            "Tommy",
            "Tawana",
            "Nevada",
            "Terrell",
            "Camille",
            "Albertine",
            "Tristan",
            "Joye",
            "Amber",
            "Laree",
            "Kali",
            "Lacresha",
            "Dione",
            "Paul",
            "Myung",
            "Trista",
            "Epifania",
            "Michell",
            "Odessa",
            "Columbus",
            "Emerita",
            "Rosann",
            "Ethelene",
            "Domitila",
            "Lawanda",
            "Jamaal",
            "Hilario",
            "Liane"
        )

        val LAST_NAMES = listOf(
            "Clayton",
            "Crane",
            "Browning",
            "Conrad",
            "Joseph",
            "Hanson",
            "Donovan",
            "Rice",
            "Green",
            "Nixon",
            "Hoffman",
            "Haley",
            "Kelly",
            "Powell",
            "Costa",
            "Blackburn",
            "Anthony",
            "Gutierrez",
            "Mcintosh",
            "Bolton",
            "Maynard",
            "Pratt",
            "Conley",
            "Blackwell",
            "Mullen",
            "Simpson",
            "Collins",
            "Matthews",
            "English",
            "Chapman",
            "Frederick",
            "Montoya",
            "Campos",
            "Spencer",
            "Mccullough",
            "Rivas",
            "Weeks",
            "Quinn",
            "Morrison",
            "Franco",
            "Moran",
            "Allen",
            "Good",
            "Benitez",
            "Haas",
            "Wyatt",
            "Dunn",
            "Davila",
            "Harrison",
            "Wallace"
        )
    }
}
