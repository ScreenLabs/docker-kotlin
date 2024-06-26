package me.devnatan.dockerkt.io

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import me.devnatan.dockerkt.Closeable

public fun interface YokiFlow<T> {
    public fun onEach(value: T)

    public fun onStart(): Unit = Unit

    public fun onError(cause: Throwable): Unit = Unit

    public fun onComplete(error: Throwable?): Unit = Unit
}

internal class InternalYokiFlow internal constructor() : Closeable {
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Default)
    private var error: Throwable? = null

    fun <T> start(
        flow: Flow<T>,
        callback: YokiFlow<T>,
    ) {
        flow.onStart { callback.onStart() }
            .onCompletion { error -> callback.onComplete(error.also { this@InternalYokiFlow.error = it }) }
            .onEach(callback::onEach)
            .catch { error -> callback.onError(error.also { this@InternalYokiFlow.error = it }) }
            .launchIn(coroutineScope)
    }

    override fun close() {
        val exception = error?.let { cause -> CancellationException("An error occurred while consuming flow.", cause) }
        coroutineScope.cancel(exception)
    }
}
