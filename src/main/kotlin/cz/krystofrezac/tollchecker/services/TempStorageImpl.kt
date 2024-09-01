package cz.krystofrezac.tollchecker.services

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.time.LocalDateTime
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TempStorageImpl<T> : TempStorage<T> {
    private val dataStore = mutableMapOf<Uuid, DataEntry>()
    private val dataStoreMutex = Mutex()

    init {
        runBlocking {
            launch {
                while (true) {
                    pruneStore()
                    delay(PRUNE_INTERVAL)
                }
            }
        }
    }

    override suspend fun insert(
        id: Uuid,
        data: T,
    ) {
        dataStoreMutex.withLock {
            dataStore[id] = DataEntry(LocalDateTime.now(), data)
        }
    }

    override suspend fun get(id: Uuid): T? {
        dataStoreMutex.withLock {
            return dataStore[id]?.data
        }
    }

    private suspend fun pruneStore() {
        dataStoreMutex.withLock {
            dataStore.forEach {
                if (it.value.storeTime
                        .plus(MAX_LIFETIME.toJavaDuration())
                        .isBefore(LocalDateTime.now())
                ) {
                    dataStore.remove(it.key)
                }
            }
        }
    }

    private inner class DataEntry(
        val storeTime: LocalDateTime,
        val data: T,
    )

    companion object {
        // TODO: configuration
        val MAX_LIFETIME = 10.minutes
        val PRUNE_INTERVAL = 10.minutes
    }
}
