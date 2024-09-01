package cz.krystofrezac.tollchecker.services

import cz.krystofrezac.tollchecker.CheckStateDTO

interface EdalniceClient {
    suspend fun getIsTollValid(licensePlate: String): Boolean
}
