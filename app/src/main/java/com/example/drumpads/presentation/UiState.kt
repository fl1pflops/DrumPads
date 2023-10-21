package com.example.drumpads.presentation

sealed class UiState {
    object Initializing : UiState()
    data class Loaded(
        val currentSamplePackName: String,
        val showChangeSamplePackButton: Boolean
    ) : UiState()

    data class Error(val type: ErrorType) : UiState()
}

enum class ErrorType {
    NO_SAMPLE_PACKS
}
