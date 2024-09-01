package cz.krystofrezac.tollchecker.services

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
interface TempStorage<T> {
    suspend fun insert(
        id: Uuid,
        data: T,
    )

    suspend fun get(id: Uuid): T?
}
