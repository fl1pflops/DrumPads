package com.example.drumpads.sampler

import android.content.res.AssetFileDescriptor
import android.media.SoundPool

class SoundPoolDrumKit : DrumKit {

    private var soundPool: SoundPool? = null
    private val sampleIds = mutableListOf<Int>()

    override fun loadSamplePack(samples: List<AssetFileDescriptor>) {
        release()
        initSoundPool()

        samples.forEach {
            soundPool?.load(it, PRIORITY)?.also { soundId ->
                if (soundId != 0) sampleIds.add(soundId)
            }
        }
    }

    override fun playSample(index: Int) {
        val soundId = sampleIds.getOrNull(index) ?: return
        soundPool?.play(soundId, VOLUME, VOLUME, PRIORITY, LOOP_COUNT, RATE)
    }

    override fun release() {
        sampleIds.clear()
        soundPool?.release()
    }

    private fun initSoundPool() {
        soundPool = SoundPool.Builder()
            .setMaxStreams(MAX_SIMULTANEOUS_STREAMS)
            .build()
    }

    companion object {
        private const val MAX_SIMULTANEOUS_STREAMS = 10
        private const val VOLUME = 1f
        private const val PRIORITY = 1
        private const val LOOP_COUNT = 0
        private const val RATE = 1f
    }

}
