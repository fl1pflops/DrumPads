package com.example.drumpads.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drumpads.data.SamplesProvider
import com.example.drumpads.sampler.DrumKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SamplerViewModel(
    private val samplesProvider: SamplesProvider,
    private val drumKit: DrumKit
) : ViewModel() {

    private val _state = MutableStateFlow<UiState>(UiState.Initializing)
    val state = _state.asStateFlow()

    private val availableSamplePacks: List<String> by lazy {
        samplesProvider.getAvailableSamplePacks(SAMPLE_PACKS_FOLDER_NAME).orEmpty()
    }
    private var selectedSamplePackIndex: Int = 0

    init {
        availableSamplePacks.firstOrNull()?.also { name ->
            val showButton = availableSamplePacks.size >= CHANGE_SAMPLE_PACK_BUTTON_THRESHOLD

            loadSamplePack(name)
            _state.value = UiState.Loaded(name, showButton)
        } ?: run {
            _state.value = UiState.Error(ErrorType.NO_SAMPLE_PACKS)
        }
    }

    override fun onCleared() {
        drumKit.release()
        super.onCleared()
    }

    fun handleAction(action: Action) {
        when (action) {
            is Action.PlaySample -> {
                drumKit.playSample(action.atIndex)
            }

            Action.ChangeSamplePack -> {
                if (availableSamplePacks.isEmpty()) return

                var nextSamplePackIndex = selectedSamplePackIndex + 1
                if (nextSamplePackIndex >= availableSamplePacks.size) {
                    nextSamplePackIndex = 0
                }

                availableSamplePacks.getOrNull(nextSamplePackIndex)
                    ?.also { name ->
                        loadSamplePack(name)
                        selectedSamplePackIndex = nextSamplePackIndex
                        _state.value = UiState.Loaded(name, true)
                    }
            }
        }
    }

    private fun loadSamplePack(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            samplesProvider.getSamplesFromPack("$SAMPLE_PACKS_FOLDER_NAME/$name").run {
                drumKit.loadSamplePack(this)
            }
        }
    }

    companion object {
        private const val CHANGE_SAMPLE_PACK_BUTTON_THRESHOLD = 2
        private const val SAMPLE_PACKS_FOLDER_NAME = "samplepacks"
    }

}
