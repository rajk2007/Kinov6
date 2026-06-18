package com.lagradost.cloudstream3.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lagradost.cloudstream3.data.models.MediaItem
import com.lagradost.cloudstream3.data.repository.TmdbRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository = TmdbRepository()

    private val _rows = MutableStateFlow<Map<String, List<MediaItem>>>(emptyMap())
    val rows: StateFlow<Map<String, List<MediaItem>>> = _rows

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedCategory = MutableStateFlow("Trending")
    val selectedCategory: StateFlow<String> = _selectedCategory

    init {
        fetchHomeData()
    }

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val trending = async { repository.getTrending() }
                val popularKino = async { repository.getPopularMovies() }
                val newReleases = async { repository.getNowPlaying() }
                val hollywood = async { repository.getHollywood() }
                val bollywood = async { repository.getBollywood() }
                val korean = async { repository.getKoreanDrama() }
                val japanese = async { repository.getJapanese() }
                val anime = async { repository.getAnime() }
                val tvSeries = async { repository.getPopularTv() }
                val topRated = async { repository.getTopRatedMovies() }
                val action = async { repository.getAction() }
                val comedy = async { repository.getComedy() }
                val horror = async { repository.getHorror() }
                val sciFi = async { repository.getSciFi() }

                _rows.value = mapOf(
                    "Trending Now" to trending.await(),
                    "Popular on KINO" to popularKino.await(),
                    "New Releases" to newReleases.await(),
                    "Hollywood Movies" to hollywood.await(),
                    "Bollywood Movies" to bollywood.await(),
                    "Korean Drama & Movies" to korean.await(),
                    "Japanese Movies" to japanese.await(),
                    "Anime" to anime.await(),
                    "TV Series" to tvSeries.await(),
                    "Top Rated" to topRated.await(),
                    "Action & Adventure" to action.await(),
                    "Comedy" to comedy.await(),
                    "Horror & Thriller" to horror.await(),
                    "Sci-Fi & Fantasy" to sciFi.await()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
