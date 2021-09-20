package com.example.drumpads.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.drumpads.data.SamplesProvider
import com.example.drumpads.sampler.DrumKit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SamplerViewModel(
    samplesProvider: SamplesProvider,
    private val drumKit: DrumKit
) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO) {
            samplesProvider.getSamplesFromPack("909").forEach { sample ->
                drumKit.addSample(sample)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        drumKit.release()
    }

    fun handleAction(action: Action) {
        when(action) {
            is Action.PlaySample -> {
                drumKit.playSample(action.atIndex)
            }
        }
    }

}
