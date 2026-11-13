package com.example.repositoriomanualmars

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.repositoriomanualmars.data.MarsPhoto
import com.example.repositoriomanualmars.data.MarsPhotosRepository
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

sealed interface MarsUiState {
    data object Loading : MarsUiState
    data object Error : MarsUiState
    data class Success(val photos: List<MarsPhoto>) : MarsUiState
}

class MarsViewModel(private val repository: MarsPhotosRepository) : ViewModel() {
    var uiState: MarsUiState by mutableStateOf(MarsUiState.Loading)
        private set

    init { loadPhotos() }

    fun loadPhotos() {
        viewModelScope.launch {
            uiState = MarsUiState.Loading
            uiState = try {
                MarsUiState.Success(repository.getMarsPhotos())
            } catch (e: IOException) {
                MarsUiState.Error
            } catch (e: HttpException) {
                MarsUiState.Error
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MarsApplication
                MarsViewModel(app.container.marsPhotosRepository)
            }
        }
    }
}
