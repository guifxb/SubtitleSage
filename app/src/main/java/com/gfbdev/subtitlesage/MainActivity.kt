package com.gfbdev.subtitlesage

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gfbdev.subtitlesage.ui.theme.PortfolioTheme
import com.gfbdev.subtitlesage.ui.managers.PortfolioApp
import com.gfbdev.subtitlesage.ui.theme.rememberWindowsSizeClass


class MainActivity : ComponentActivity() {

    private fun checkPermission(): Boolean {
        val permission1 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val permission2 = ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return permission1 == PackageManager.PERMISSION_GRANTED && permission2 == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE),
            200
        )
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(!checkPermission()) {
            requestPermission()
        }
        installSplashScreen()

        setContent {
            val window = rememberWindowsSizeClass()
            PortfolioTheme(window) {
                PortfolioApp()
            }
        }
    }
}