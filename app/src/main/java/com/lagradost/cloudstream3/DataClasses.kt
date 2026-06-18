package com.lagradost.cloudstream3

data class SearchResponse(val name: String, val url: String, val apiName: String, val type: String? = null)
data class LoadResponse(val name: String, val url: String, val data: String, val type: String? = null)
data class ExtractorLink(val name: String, val url: String, val referer: String? = null, val quality: Int = 0, val headers: Map<String, String> = mapOf(), val type: String = "M3U8")
data class SubtitleLink(val url: String, val lang: String)
