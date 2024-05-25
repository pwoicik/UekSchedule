package uekschedule.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

val Cache = mutableMapOf<String, Any>()

suspend inline fun <reified T : Any> HttpClient.getCacheable(url: String) =
    Cache[url]?.let { it as T }
        ?: get(url).body<T>()
            .also { synchronized(Cache) { Cache[url] = it } }
