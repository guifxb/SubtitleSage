package com.gfbdev.subtitlesage

import android.app.Application
import com.gfbdev.subtitlesage.data.AppContainer
import com.gfbdev.subtitlesage.data.DefaultAppContainer

class AppApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}