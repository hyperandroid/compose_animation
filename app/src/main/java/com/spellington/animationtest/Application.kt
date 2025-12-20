package com.spellington.animationtest

import android.app.Application
import androidx.compose.runtime.Composer
import androidx.compose.runtime.tooling.ComposeStackTraceMode

class Application: Application() {

    override fun onCreate() {
        super.onCreate()

        Composer.setDiagnosticStackTraceMode(ComposeStackTraceMode.SourceInformation)
    }
}