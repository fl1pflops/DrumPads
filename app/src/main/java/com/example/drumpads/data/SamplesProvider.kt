package com.example.drumpads.data

import android.content.res.AssetFileDescriptor
import android.content.res.AssetManager
import java.io.IOException

class SamplesProvider(private val assetManager: AssetManager) {

    fun getSamplesFromPack(packName: String): List<AssetFileDescriptor> = assetManager.list(packName)
        ?.mapIndexedNotNull { index, sampleName ->
            try {
                assetManager.openFd("$packName/$sampleName")
            } catch (exception: IOException) {
                null
            }
        }.orEmpty()

}
