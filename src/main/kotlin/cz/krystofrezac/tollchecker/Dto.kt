package cz.krystofrezac.tollchecker

enum class CheckStateDTO {
    Valid,
    Invalid,
    Loading,
}

data class CheckResultDTO(
    val licensePlates: Map<String, CheckStateDTO>,
)
