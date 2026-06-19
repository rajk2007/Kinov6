package com.lagradost.cloudstream3.plugins

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.lagradost.cloudstream3.ExtractorLink
import com.lagradost.cloudstream3.MainAPI
import com.lagradost.cloudstream3.SearchResponse
import dalvik.system.DexClassLoader
import dalvik.system.DexFile
import java.io.File

object PluginEngine {
    private val loadedPlugins = mutableListOf<MainAPI>()
    private const val TAG = "KINO_Plugin"

    fun loadPlugin(context: Context, pluginFile: File) {
        Log.d(TAG, "Starting to load plugin from: ${pluginFile.absolutePath}")
        try {
            if (pluginFile.length() == 0L) {
                throw Exception("Plugin file is 0 bytes")
            }

            val classLoader = DexClassLoader(
                pluginFile.absolutePath,
                context.codeCacheDir.absolutePath,
                null,
                context.classLoader
            )
            
            val dexFile = DexFile(pluginFile)
            val entries = dexFile.entries()
            var pluginInstance: MainAPI? = null

            while (entries.hasMoreElements()) {
                val className = entries.nextElement()
                if (className.contains("com.lagradost")) {
                    try {
                        val loadedClass = classLoader.loadClass(className)
                        if (MainAPI::class.java.isAssignableFrom(loadedClass) && loadedClass != MainAPI::class.java) {
                            Log.d(TAG, "Found valid plugin class: $className")
                            pluginInstance = loadedClass.getDeclaredConstructor().newInstance() as MainAPI
                            break
                        }
                    } catch (e: Exception) {
                        // Skip classes that can't be loaded or instantiated
                    }
                }
            }
            
            if (pluginInstance != null) {
                Log.d(TAG, "Successfully loaded plugin: ${pluginInstance.name}")
                loadedPlugins.add(pluginInstance)
            } else {
                throw Exception("No valid MainAPI class found in ${pluginFile.name}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load plugin: ${pluginFile.name}", e)
            context.mainLooper?.let {
                android.os.Handler(it).post {
                    Toast.makeText(context, "Plugin Error (${pluginFile.name}): ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun getPlugins() = loadedPlugins

    suspend fun search(query: String): List<SearchResponse> {
        Log.d(TAG, "Searching for: $query in ${loadedPlugins.size} plugins")
        val results = mutableListOf<SearchResponse>()
        for (plugin in loadedPlugins) {
            try {
                Log.d(TAG, "Calling search on plugin: ${plugin.name}")
                val searchResults = plugin.search(query)
                Log.d(TAG, "Plugin ${plugin.name} returned ${searchResults?.size ?: 0} results")
                searchResults?.let { results.addAll(it) }
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
                plugin.loadLinks(
                    data = url,
                    isCasting = false,
                    subtitleCallback = { _ -> },
                    callback = { link ->
                        Log.d(TAG, "Found link: ${link.name} - ${link.url}")
                        links.add(link)
                    }
                )
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
