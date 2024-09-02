package cz.krystofrezac.tollchecker.services

class CheckServiceImpl(
    private val edalniceClient: EdalniceClient,
) : CheckService {
    override suspend fun check(licensePlates: List<String>): Map<String, Boolean> =
        licensePlates
            .associateWith {
                edalniceClient.getIsTollValid(it)
            }
}
