package com.example.repositoriomanualmars

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.repositoriomanualmars.data.MarsPhoto
import com.example.repositoriomanualmars.ui.theme.RepositorioManualMarsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RepositorioManualMarsTheme {
                val model: MarsViewModel = viewModel(factory = MarsViewModel.Factory)
                Scaffold(topBar = { MarsTopBar() }) { padding ->
                    MarsScreen(model.uiState, model::loadPhotos, Modifier.padding(padding))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarsTopBar() {
    CenterAlignedTopAppBar(title = { Text(stringResource(R.string.app_name)) })
}

@Composable
fun MarsScreen(state: MarsUiState, retry: () -> Unit, modifier: Modifier = Modifier) {
    when (state) {
        MarsUiState.Loading -> Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        MarsUiState.Error -> Column(modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(stringResource(R.string.loading_error))
            Button(onClick = retry) { Text(stringResource(R.string.retry)) }
        }
        is MarsUiState.Success -> {
            if (state.photos.isEmpty()) {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text(stringResource(R.string.no_photos)) }
            } else {
                PhotosGrid(state.photos, modifier)
            }
        }
    }
}

@Composable
fun PhotosGrid(photos: List<MarsPhoto>, modifier: Modifier = Modifier) {
    LazyVerticalGrid(columns = GridCells.Adaptive(150.dp), modifier = modifier.fillMaxSize(), contentPadding = PaddingValues(4.dp)) {
        items(photos, key = { it.id }) { photo ->
            Card(modifier = Modifier.padding(4.dp).fillMaxWidth().aspectRatio(1f)) {
                AsyncImage(
                    model = photo.imgSrc,
                    contentDescription = stringResource(R.string.mars_photo),
                    contentScale = ContentScale.Crop,
                    placeholder = androidx.compose.ui.res.painterResource(R.drawable.image_loading),
                    error = androidx.compose.ui.res.painterResource(R.drawable.image_error),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}
