package cz.krystofrezac.tollchecker.services

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class CheckServiceImpl(
    val edalniceClient: EdalniceClient,
) : CheckService {
    override suspend fun check(licensePlates: List<String>): Map<String, Boolean> =
        coroutineScope {
            licensePlates
                .associate {
                    async {
                        edalniceClient.getIsTollValid(it)
                    }.let { promise -> it to promise }
                }.mapValues { it.value.await() }
        }
}
