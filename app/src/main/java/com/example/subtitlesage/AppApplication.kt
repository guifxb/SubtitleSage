package com.example.subtitlesage

import android.app.Application
import com.example.subtitlesage.data.AppContainer
import com.example.subtitlesage.data.DefaultAppContainer

class AppApplication: Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}