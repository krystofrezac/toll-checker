package cz.krystofrezac.tollchecker.services

interface CheckService {
    suspend fun check(licensePlates: List<String>): Map<String, Boolean>
}

