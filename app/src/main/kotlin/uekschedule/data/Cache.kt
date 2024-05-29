package uekschedule.data

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.util.reflect.TypeInfo
import io.ktor.util.reflect.typeInfo
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.coroutines.cancellation.CancellationException

private val mutex = Mutex()
private val Cache = mutableMapOf<String, Any>()

suspend inline fun <reified T : Any> HttpClient.getCacheable(url: String): Either<Exception, T> =
    getCacheable(url, typeInfo<T>())

suspend fun <T : Any> HttpClient.getCacheable(
    url: String,
    typeInfo: TypeInfo,
): Either<Exception, T> {
    val cached = Cache[url]
    if (cached != null) {
        @Suppress("UNCHECKED_CAST")
        return (cached as T).right()
    }
    return try {
        get(url).body<T>(typeInfo).also { mutex.withLock { Cache[url] = it } }.right()
    } catch (e: Exception) {
        if (e is CancellationException) throw e else e.left()
    }
}
