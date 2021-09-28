package com.example.drumpads.di

import com.example.drumpads.data.SamplesProvider
import com.example.drumpads.presentation.SamplerViewModel
import com.example.drumpads.sampler.DrumKit
import com.example.drumpads.sampler.SoundPoolDrumKit
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<DrumKit> { SoundPoolDrumKit() }
    single { SamplesProvider(androidContext().assets) }

    viewModel { SamplerViewModel(get(), get()) }
}
