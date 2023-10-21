package com.example.drumpads.sampler

import android.content.res.AssetFileDescriptor

interface DrumKit {

    fun loadSamplePack(samples: List<AssetFileDescriptor>)

    fun playSample(index: Int)

    fun release() {}

}
