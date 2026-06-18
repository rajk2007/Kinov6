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
                
                // Curated lists for Trending category
                val popularKino = async { repository.getPopularMovies() }
                val newReleases = async { repository.getNowPlaying() }
                val hollywood = async { repository.getHollywood() }
                val bollywood = async { repository.getBollywood() }
                val korean = async { repository.getKoreanDrama() }
                val japanese = async { repository.getJapanese() }
                val anime = async { repository.getAnime() }
                val tvSeries = async { repository.getPopularTv() }
                val topRated = async { repository.getTopRatedMovies() }
                val action = async { repository.getActionMovies() }
                val comedy = async { repository.getComedyMovies() }
                val horror = async { repository.getHorrorMovies() }
                val sciFi = async { repository.getSciFiMovies() }

                // Other categories
                val topRatedMovies = async { repository.getTopRatedMovies() }
                val upcomingMovies = async { repository.getUpcomingMovies() }
                val topRatedTv = async { repository.getTopRatedTv() }
                val airingToday = async { repository.getAiringTodayTv() }
                val crimeTv = async { repository.getCrimeTv() }
                val dramaTv = async { repository.getDramaTv() }
                val sciFiTv = async { repository.getSciFiTv() }
                val topRatedAnime = async { repository.getTopRatedAnime() }
                val actionAnime = async { repository.getActionAnime() }
                val romanceAnime = async { repository.getRomanceAnime() }
                val fantasyAnime = async { repository.getFantasyAnime() }

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
                    "Sci-Fi & Fantasy" to sciFi.await(),

                    // Movie Category
                    "Now Playing" to newReleases.await(),
                    "Popular Movies" to popularKino.await(),
                    "Top Rated Movies" to topRatedMovies.await(),
                    "Upcoming Movies" to upcomingMovies.await(),
                    "Action Movies" to action.await(),
                    "Comedy Movies" to comedy.await(),
                    "Horror Movies" to horror.await(),
                    "Sci-Fi Movies" to sciFi.await(),

                    // Series Category
                    "Popular TV" to tvSeries.await(),
                    "Top Rated TV" to topRatedTv.await(),
                    "Airing Today" to airingToday.await(),
                    "Crime TV" to crimeTv.await(),
                    "Drama TV" to dramaTv.await(),
                    "Sci-Fi TV" to sciFiTv.await(),

                    // Anime Category
                    "Popular Anime" to anime.await(),
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
