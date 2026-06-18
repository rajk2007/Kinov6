package com.lagradost.cloudstream3.plugins

import android.content.Context
import dalvik.system.DexClassLoader
import java.io.File

data class ExtractorLink(
    val name: String,
    val source: String,
    val url: String,
    val quality: Int
)

object PluginEngine {
    private val loadedPlugins = mutableListOf<Any>()

    fun loadPlugin(context: Context, pluginFile: File) {
        try {
            val classLoader = DexClassLoader(
                pluginFile.absolutePath,
                context.codeCacheDir.absolutePath,
                null,
                context.classLoader
            )
            
            val pluginClass = classLoader.loadClass("com.lagradost.cloudstream3.MainPlugin")
            val pluginInstance = pluginClass.getDeclaredConstructor().newInstance()
            
            loadedPlugins.add(pluginInstance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPlugins() = loadedPlugins

    suspend fun search(query: String): List<String> {
        val results = mutableListOf<String>()
        for (plugin in loadedPlugins) {
            try {
                val method = plugin.javaClass.getMethod("search", String::class.java)
                val result = method.invoke(plugin, query) as? List<*>
                result?.filterIsInstance<String>()?.let { results.addAll(it) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return results
    }

    suspend fun load(url: String): List<ExtractorLink> {
        val links = mutableListOf<ExtractorLink>()
        for (plugin in loadedPlugins) {
            try {
                val method = plugin.javaClass.getMethod("load", String::class.java)
                val result = method.invoke(plugin, url) as? List<*>
                result?.forEach { item ->
                    // Use reflection to map to ExtractorLink if needed, 
                    // but for now we assume the plugin returns objects that can be cast or mapped
                    if (item != null) {
                        val name = item.javaClass.getMethod("getName").invoke(item) as String
                        val source = item.javaClass.getMethod("getSource").invoke(item) as String
                        val linkUrl = item.javaClass.getMethod("getUrl").invoke(item) as String
                        val quality = item.javaClass.getMethod("getQuality").invoke(item) as Int
                        links.add(ExtractorLink(name, source, linkUrl, quality))
                    }
                }
            } catch (e: Exception) {
                // If it fails on one plugin, try the next
                continue
            }
            if (links.isNotEmpty()) break // Found links in one plugin, stop
        }
        return links
    }
}
