package com.example.drumpads.sampler

import android.content.res.AssetFileDescriptor

interface DrumKit {

    fun addSample(assetFileDescriptor: AssetFileDescriptor)

    fun playSample(index: Int)

    fun release() {}

}
