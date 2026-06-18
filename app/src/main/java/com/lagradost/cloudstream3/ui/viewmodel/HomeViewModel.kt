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
                // Common
                val trending = async { repository.getTrending() }
                
                // Movies
                val nowPlaying = async { repository.getNowPlaying() }
                val popularMovies = async { repository.getPopularMovies() }
                val topRatedMovies = async { repository.getTopRatedMovies() }
                val upcomingMovies = async { repository.getUpcomingMovies() }
                val actionMovies = async { repository.getActionMovies() }
                val comedyMovies = async { repository.getComedyMovies() }
                val horrorMovies = async { repository.getHorrorMovies() }
                val sciFiMovies = async { repository.getSciFiMovies() }

                // Series
                val popularTv = async { repository.getPopularTv() }
                val topRatedTv = async { repository.getTopRatedTv() }
                val airingToday = async { repository.getAiringTodayTv() }
                val crimeTv = async { repository.getCrimeTv() }
                val dramaTv = async { repository.getDramaTv() }
                val sciFiTv = async { repository.getSciFiTv() }

                // Anime
                val popularAnime = async { repository.getAnime() }
                val topRatedAnime = async { repository.getTopRatedAnime() }
                val actionAnime = async { repository.getActionAnime() }
                val romanceAnime = async { repository.getRomanceAnime() }
                val fantasyAnime = async { repository.getFantasyAnime() }

                _rows.value = mapOf(
                    "Trending Now" to trending.await(),
                    
                    // Movies
                    "Now Playing" to nowPlaying.await(),
                    "Popular Movies" to popularMovies.await(),
                    "Top Rated Movies" to topRatedMovies.await(),
                    "Upcoming Movies" to upcomingMovies.await(),
                    "Action Movies" to actionMovies.await(),
                    "Comedy Movies" to comedyMovies.await(),
                    "Horror Movies" to horrorMovies.await(),
                    "Sci-Fi Movies" to sciFiMovies.await(),

                    // Series
                    "Popular TV" to popularTv.await(),
                    "Top Rated TV" to topRatedTv.await(),
                    "Airing Today" to airingToday.await(),
                    "Crime TV" to crimeTv.await(),
                    "Drama TV" to dramaTv.await(),
                    "Sci-Fi TV" to sciFiTv.await(),

                    // Anime
                    "Popular Anime" to popularAnime.await(),
                    "Top Rated Anime" to topRatedAnime.await(),
                    "Action Anime" to actionAnime.await(),
                    "Romance Anime" to romanceAnime.await(),
                    "Fantasy Anime" to fantasyAnime.await()
                )
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
