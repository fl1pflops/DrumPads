package com.example.drumpads

import android.app.Application
import com.example.drumpads.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DrumPadsApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@DrumPadsApp)
            modules(appModule)
        }
    }

}
