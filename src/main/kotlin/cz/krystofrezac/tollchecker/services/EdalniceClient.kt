package cz.krystofrezac.tollchecker.services

interface EdalniceClient {
    suspend fun getIsTollValid(licensePlate: String): Boolean
}
