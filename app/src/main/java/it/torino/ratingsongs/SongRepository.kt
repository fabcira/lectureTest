package it.torino.ratingsongs

import android.util.Log
import kotlin.random.Random

class SongRepository {
    private val TAG = SongRepository::class.simpleName

    /**
     * it creates the initial list of n songs
     * @param numberOfSongs
     * @return
     */
    fun initSongs(numberOfSongs: Int): List<Song> {
        val newSongs = mutableListOf<Song>()
        for (counter in 0..numberOfSongs) {
            val newSong = generateNewSong()
            newSongs.add(newSong)
        }
        //@todo sort newSong using the field rating
        val sortedSongs = ...
        return sortedSongs
    }

    /**
     * given a song id it changes its rating to newRating
     * @param currentSongs
     * @param songId
     * @param newRating
     * @return updated list
     */
    fun updateSongRating(currentSongs: List<Song>, songId: Int, newRating: Int): List<Song> {
        //todo
        // 1. find the song in the current list
        // 2. change the song's rating
        // 3. sort the list
        // return the list
    }

    /**
     * it generates and adds a new song to the list of songs
     * @param currentSongs
     * @param song
     * @return updated list
     */
    fun addSong(currentSongs: List<Song>, song: Song): List<Song> {
        //@todo add teh song to the list and sort the list again
    }

    /**
     * it selects a random song and it generates a random rating
     * @param currentSongs
     * @return updated list
     */
    private fun randomRatingUpdate(currentSongs: List<Song>): List<Song> {
        if (currentSongs.isEmpty()) return currentSongs
        val randomIndex = Random.nextInt(currentSongs.size)
        val newRating = Random.nextInt(1, 6) // Rating usually 1 to 5
        Log.i(TAG, "updating song ${currentSongs[randomIndex].title} rating from ${currentSongs[randomIndex].rating} to $newRating")
        return currentSongs.mapIndexed { index, song ->
            if (index == randomIndex) song.copy(rating = newRating) else song
        }.sortedByDescending { it.rating }
    }

    /**
     * it generates a new song with random values
     * @return
     */
    private fun generateNewSong(): Song {
        val newRating = Random.nextInt(1, 6)
        val newSong = Song(
            id = Random.nextInt(100, 10000), // Increased range for safety
            title = "Song ${Random.nextInt(100)}",
            artist = "Artist ${Random.nextInt(10)}",
            year = Random.nextInt(1990, 2026),
            rating = newRating
        )
        Log.i(TAG, "Adding song ${newSong.title} (rating ${newSong.rating})")
        return newSong
    }

    /**
     * performs a random update (either rating update or new song)
     * @param currentSongs
     * @return updated list
     */
    fun performRandomUpdate(currentSongs: List<Song>): List<Song> {
        return if (Random.nextBoolean() && currentSongs.isNotEmpty()) {
            Log.i(TAG, "generating rating update")
            randomRatingUpdate(currentSongs)
        } else {
            Log.i(TAG, "generating new song")
            val newSong = generateNewSong()
            addSong(currentSongs, newSong)
        }
    }
}
