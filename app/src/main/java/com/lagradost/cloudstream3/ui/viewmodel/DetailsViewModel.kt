package com.lagradost.cloudstream3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.data.repository.TmdbRepository
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.SearchResponse
import com.lagradost.cloudstream3.plugins.PluginEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class DetailsUiState {
    object Idle : DetailsUiState()
    object Loading : DetailsUiState()
    data class Success(val media: MediaItem, val similar: List<MediaItem>) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}

sealed class WatchUiState {
    object Idle : WatchUiState()
    object FetchingLinks : WatchUiState()
    data class LinksFound(val links: List<ExtractorLink>) : WatchUiState()
    object NoLinksFound : WatchUiState()
    data class Error(val message: String) : WatchUiState()
}

class DetailsViewModel : ViewModel() {
    private val repository = TmdbRepository()

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Idle)
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()

    private val _watchState = MutableStateFlow<WatchUiState>(WatchUiState.Idle)
    val watchState: StateFlow<WatchUiState> = _watchState.asStateFlow()

    fun fetchDetails(mediaId: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            try {
                val media = repository.getMovieDetails(mediaId)
                val similar = repository.getSimilarMovies(mediaId)
                if (media != null) {
                    _uiState.value = DetailsUiState.Success(media, similar)
                } else {
                    _uiState.value = DetailsUiState.Error("Failed to load details")
                }
            } catch (e: Exception) {
                _uiState.value = DetailsUiState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun watchNow(media: MediaItem) {
        viewModelScope.launch {
            _watchState.value = WatchUiState.FetchingLinks
            try {
                val query = media.displayTitle
                val searchResults: List<SearchResponse> = PluginEngine.search(query)
                
                if (searchResults.isEmpty()) {
                    _watchState.value = WatchUiState.NoLinksFound
                    return@launch
                }

                val allLinks = mutableListOf<ExtractorLink>()
                val topResults = searchResults.take(3)
                
                for (result in topResults) {
                    try {
                        val links = PluginEngine.load(result.url)
                        if (links.isNotEmpty()) {
                            allLinks.addAll(links)
                        }
                    } catch (e: Exception) {
                        // Log and continue
                    }
                }

                if (allLinks.isEmpty()) {
                    _watchState.value = WatchUiState.NoLinksFound
                } else {
                    _watchState.value = WatchUiState.LinksFound(allLinks)
                }
            } catch (e: Exception) {
                _watchState.value = WatchUiState.Error(e.message ?: "Error fetching links")
            }
        }
    }

    fun resetWatchState() {
        _watchState.value = WatchUiState.Idle
    }
}
