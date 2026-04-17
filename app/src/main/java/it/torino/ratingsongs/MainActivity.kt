package it.torino.ratingsongs


import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import it.torino.ratingsongs.ui.theme.RatingSongsTheme
import it.torino.ratingsongs.ui.theme.VeryLightGray

class MainActivity : ComponentActivity() {
    val TAG : String? = MainActivity::class.simpleName

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: SongViewModel by viewModels()
        setContent {
            // define the songs list as a state depending on the appropriate repository flow
            val lifecycleOwner: LifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

            Scaffold(modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopAppBar(
                        colors = topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        ),
                        title = {
                            Text(stringResource(R.string.app_name))
                        }
                    )
                }) { innerPadding ->
                ManageLifeCyle(lifecycleOwner, viewModel)
                SongList(songs, viewModel::updateRating, modifier= Modifier.padding(innerPadding))
            }
        }
    }

    @Composable
    private fun ManageLifeCyle(lifecycleOwner: LifecycleOwner, viewModel: SongViewModel) {
        // Use DisposableEffect to observe the lifecycle
        DisposableEffect(lifecycleOwner) {
            // Create and add the LifecycleEventObserver
            // Initialization Block: This is the main body of the DisposableEffect.
            // It's where you set up listeners, subscribe to data streams, or perform other operations
            // that have a lasting effect beyond the immediate execution of the composable function.
            // These operations are executed when the effect enters the composition.
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_PAUSE) {
                    Log.i(TAG, "on_pause: stopping generation process")
                    viewModel.stopSongsGeneration()
                }
                if (event == Lifecycle.Event.ON_RESUME) {
                    Log.i(TAG, "resuming generation process")
                    viewModel.startSongGeneration()
                }
            }
            // Add the observer to the lifecycle
            lifecycleOwner.lifecycle.addObserver(observer)

            // When the effect leaves the composition, remove the lifecycle observer
            // Cleanup Block (onDispose): Defined using the onDispose function call within
            // DisposableEffect, it specifies the cleanup actions to be taken when the effect leaves
            // the composition. This is where you unregister listeners, unsubscribe from data streams,
            // or release any other resources that were acquired in the initialization block.
            onDispose {
                Log.i(TAG, "disposing the activity")
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }
}
@Composable
fun SongList(songs: List<Song>, onRatingChanged: (Int, Int) -> Unit, modifier: Modifier) {
    LazyColumn (modifier=  modifier){
        items(songs.size) { index ->
            SongRow(songs[index], onRatingChanged,
                modifier= Modifier
                    .background(if (index%2 ==0) VeryLightGray else Color.White)
                    .padding(20.dp))

        }
    }
}

@Composable
fun SongRow(song: Song, onRatingChanged: (Int, Int) -> Unit, modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Implement the row layout
        Text(
            text = song.title,
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = "by ${song.artist}, ${song.year}",
            textAlign = TextAlign.Left,
            style = MaterialTheme.typography.bodyMedium
        )
        RatingBar(rating = song.rating, onRatingChanged = { newRating ->
            onRatingChanged(song.id, newRating)
        })
    }
}


@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    maxRating: Int = 5,
    color: Color = Color.Blue
) {
    Row(modifier = modifier) {
        for (i in 1..rating) {
            Icon(
                imageVector =
                Icons.Filled.Star,
                contentDescription = "Rating $i",
                modifier = Modifier
                    .padding(4.dp)
                    .clickable { onRatingChanged(i) },
                tint = color
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingSongsPreview() {
    val viewModel: SongViewModel = viewModel()

    RatingSongsTheme {
        val songs by viewModel.songs.collectAsState(initial = emptyList())
        SongList(
            songs,
            viewModel::updateRating,
            modifier= Modifier.padding(14.dp)
        )
    }
}