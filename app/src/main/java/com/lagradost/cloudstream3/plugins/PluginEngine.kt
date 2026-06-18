package com.lagradost.cloudstream3.plugins

import android.content.Context
import android.util.Log
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.MainAPI
import dalvik.system.DexClassLoader
import java.io.File

object PluginEngine {
    private val loadedPlugins = mutableListOf<MainAPI>()
    private const val TAG = "KINO_Plugin"

    fun loadPlugin(context: Context, pluginFile: File) {
        Log.d(TAG, "Starting to load plugin from: ${pluginFile.absolutePath}")
        try {
            Log.d(TAG, "Creating DexClassLoader for ${pluginFile.name}")
            val classLoader = DexClassLoader(
                pluginFile.absolutePath,
                context.codeCacheDir.absolutePath,
                null,
                context.classLoader
            )
            
            // In CloudStream plugins, the main class is often com.lagradost.cloudstream3.plugins.Plugin
            // We'll try to find it or similar
            Log.d(TAG, "Attempting to load class: com.lagradost.cloudstream3.MainPlugin")
            val pluginClass = try {
                classLoader.loadClass("com.lagradost.cloudstream3.MainPlugin")
            } catch (e: Exception) {
                Log.w(TAG, "MainPlugin class not found, trying com.lagradost.cloudstream3.plugins.Plugin")
                classLoader.loadClass("com.lagradost.cloudstream3.plugins.Plugin")
            }
            
            Log.d(TAG, "Instantiating plugin class: ${pluginClass.name}")
            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance() as MainAPI
            
            Log.d(TAG, "Successfully loaded plugin: ${pluginInstance.name}")
            loadedPlugins.add(pluginInstance)
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load plugin: ${pluginFile.name}", e)
        }
    }

    fun getPlugins() = loadedPlugins

    suspend fun search(query: String): List<String> {
        Log.d(TAG, "Searching for: $query in ${loadedPlugins.size} plugins")
        val results = mutableListOf<String>()
        for (plugin in loadedPlugins) {
            try {
                Log.d(TAG, "Calling search on plugin: ${plugin.name}")
                val searchResults = plugin.search(query)
                Log.d(TAG, "Plugin ${plugin.name} returned ${searchResults.size} results")
                searchResults.forEach { results.add(it.url) }
            } catch (e: Exception) {
                Log.e(TAG, "Error searching in plugin: ${plugin.name}", e)
            }
        }
        return results
    }

    suspend fun load(url: String): List<ExtractorLink> {
        Log.d(TAG, "Loading links for URL: $url")
        val links = mutableListOf<ExtractorLink>()
        for (plugin in loadedPlugins) {
            try {
                Log.d(TAG, "Calling loadLinks on plugin: ${plugin.name}")
                plugin.loadLinks(url, callback = { link ->
                    Log.d(TAG, "Found link: ${link.name} - ${link.url}")
                    links.add(link)
                })
            } catch (e: Exception) {
                Log.e(TAG, "Error loading links in plugin: ${plugin.name}", e)
            }
            if (links.isNotEmpty()) {
                Log.d(TAG, "Found ${links.size} links, stopping search")
                break
            }
        }
        return links
    }
}
