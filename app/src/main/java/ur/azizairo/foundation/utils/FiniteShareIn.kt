package ur.azizairo.foundation.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*

sealed class Element<T>

class ItemElement<T>(val item: T): Element<T>()

class ErrorElement<T>(
    val error: Throwable
): Element<T>()

class CompletedElement<T>: Element<T>()

/**
 * Transform this Flow into Hot Shared Flow (like by using [shareIn] operator), but which is finite and
 * which also propagates exceptions from the source flow.
 */
fun <T> Flow<T>.finiteShareIn(
    coroutineScope: CoroutineScope
): Flow<T> {

    return this
        .map<T, Element<T>> { item -> ItemElement(item) }
        .onCompletion {
            emit(CompletedElement())
        }
        .catch { exception ->
            emit(ErrorElement(exception))
        }
        .shareIn(coroutineScope, started = SharingStarted.Eagerly, 1)
        .map {
            if (it is ErrorElement) {
                throw it.error
            }
            return@map it
        }
        .takeWhile { it is ItemElement }
        .map { (it as ItemElement).item }
}