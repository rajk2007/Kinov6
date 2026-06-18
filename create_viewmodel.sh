#!/bin/bash
mkdir -p app/src/main/java/com/lagradost/cloudstream3/ui/viewmodel

# HomeViewModel.kt
cat <<EOF > app/src/main/java/com/lagradost/cloudstream3/ui/viewmodel/HomeViewModel.kt
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

    private val _trending = MutableStateFlow<List<MediaItem>>(emptyList())
    val trending: StateFlow<List<MediaItem>> = _trending

    private val _popularMovies = MutableStateFlow<List<MediaItem>>(emptyList())
    val popularMovies: StateFlow<List<MediaItem>> = _popularMovies

    private val _popularTv = MutableStateFlow<List<MediaItem>>(emptyList())
    val popularTv: StateFlow<List<MediaItem>> = _popularTv

    private val _anime = MutableStateFlow<List<MediaItem>>(emptyList())
    val anime: StateFlow<List<MediaItem>> = _anime

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchHomeData()
    }

    fun fetchHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val trendingDef = async { repository.getTrending() }
                val moviesDef = async { repository.getPopularMovies() }
                val tvDef = async { repository.getPopularTv() }
                val animeDef = async { repository.getAnime() }

                _trending.value = trendingDef.await()
                _popularMovies.value = moviesDef.await()
                _popularTv.value = tvDef.await()
                _anime.value = animeDef.await()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
EOF
