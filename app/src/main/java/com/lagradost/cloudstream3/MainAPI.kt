package com.lagradost.cloudstream3

open class MainAPI {
    open var name: String = ""
    open var mainUrl: String = ""
    open suspend fun search(query: String): List<SearchResponse> = emptyList()
    open suspend fun load(url: String): LoadResponse? = null
    open suspend fun loadLinks(url: String, isCasting: Boolean = false, subtitleCallback: (SubtitleLink) -> Unit = {}, callback: (ExtractorLink) -> Unit = {}): Boolean = false
}
