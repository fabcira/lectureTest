package it.torino.ratingsongs

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job

class SongViewModel : ViewModel() {
    //@todo create repository and flow variables
    // @todo create an independent class representing the songs with fields such as
    //id: Int, title: String,  artist: String, year: Int, rating: Int

    // the job is a variable that allows stopping a coroutine when started.
    // It is to be assigned when the coroutine is launched
         // generationJob = viewModelscope(...).launch...
    //  and used as
        // generationJob?.cancel()
    // to sop the coroutine
    private var generationJob: Job? = null

    /**
     * used to update a rating to a song on the interface side
     * @param songId the id of the song
     * @param rating the new rating
     */
    fun updateRating(songId: Int, rating: Int) {
        // @todo launch the coroutine that will update the rating by calling the repository method
        // generationJob = ...

    }

    /**
     * called by onPause
     */
    fun stopSongsGeneration() {
        generationJob?.cancel()
    }

    /**
     * called by onResume
     */
    fun startSongGeneration() {
        stopSongsGeneration()
        // launch the coroutine that will generate a new song by calling the repository method
        // (repository.performRandomUpdate)
        // as:
        // generationJob = ...

    }
}
