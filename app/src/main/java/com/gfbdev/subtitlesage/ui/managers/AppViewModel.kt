package com.gfbdev.subtitlesage.ui.managers



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gfbdev.subtitlesage.AppApplication
import com.gfbdev.subtitlesage.data.MovieRepository
import com.gfbdev.subtitlesage.data.MovieRepositoryLocal
import com.gfbdev.subtitlesage.model.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


interface AppUiState {
    data class Success(val movies: List<MovieInfoLocal>) : AppUiState
    object Error : AppUiState
    object Loading : AppUiState
}

class OnlineViewModel(
    private val movieRepository: MovieRepository,
    private val movieRepositoryLocal: MovieRepositoryLocal,
) : ViewModel() {

    var uiState: AppUiState by mutableStateOf(AppUiState.Loading)
        private set

    init {
        getMovieInfo()
    }

    //Current Movie val used to build details screen from grid click
    private val _currentMovie = MutableStateFlow(DefaultMovie)
    val currentMovie: StateFlow<MovieInfoLocal> = _currentMovie.asStateFlow()

    //Add Title val used to build AddTitle screen from search and add it to database
    private val _addTitleCurrentMovie = MutableStateFlow(DefaultTitleToAdd)
    val addTitleCurrentMovie: StateFlow<MovieInfoNet> = _addTitleCurrentMovie.asStateFlow()


    fun updateCurrentMovie(movieInfoLocal: MovieInfoLocal) {
        _currentMovie.update {
            movieInfoLocal
        }
    }

    fun getTitleToAdd(idToAdd: String) {
        val regex = Regex("""tt(\d+)""")
        val matchResult = regex.find(idToAdd)
        val extractedValue = matchResult?.value
        viewModelScope.launch {
            val titleToAdd = try {
                movieRepository.getTitleToAdd(extractedValue ?: "" )
            } catch (e: IOException) {
                BrokenTitle
            } catch (e: HttpException) {
                BrokenTitle
            }
            catch (e: Exception) {
                BrokenTitle
            }
            _addTitleCurrentMovie.update {
                titleToAdd
            }
        }
    }

    fun addTitle() {
        viewModelScope.launch {
            movieRepositoryLocal.insertItem(_addTitleCurrentMovie.value.toMovieInfoLocal())
            getMovieInfo()

            _addTitleCurrentMovie.update {
                DefaultTitleToAdd
            }
        }
    }

    fun deleteTitle(listToDelete: MutableList<MovieInfoLocal>) {
        viewModelScope.launch {
            for (movie in listToDelete) {
                movieRepositoryLocal.deleteItem(movie)
            }
            getMovieInfo()
        }
    }

    fun getMovieInfo() {
        viewModelScope.launch {
            uiState = AppUiState.Loading

            val listToSave = try {
                movieRepository.getMovieInfo()
            } catch (e: IOException) {
                emptyList()
            } catch (e: HttpException) {
                emptyList()
            }

            for (movie in listToSave) {
                movieRepositoryLocal.insertItem(movie.toMovieInfoLocal())
            }

            val movies by mutableStateOf(movieRepositoryLocal.getAllItemsStream().first())
            uiState = AppUiState.Success(movies)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as AppApplication)
                val movieRepository = application.container.movieRepository
                val movieRepositoryLocal = application.container.movieRepositoryLocal
                OnlineViewModel(movieRepository = movieRepository,
                    movieRepositoryLocal = movieRepositoryLocal)
            }
        }
    }
}