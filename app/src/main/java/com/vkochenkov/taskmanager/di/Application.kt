package com.vkochenkov.taskmanager.di

import android.app.Application
import com.vkochenkov.taskmanager.base.di.baseModule
import com.vkochenkov.taskmanager.feature.main.di.featureMainModule
import com.vkochenkov.taskmanager.feature.settings.di.featureSettingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApplication)
            // include all modules
            modules(baseModule, featureMainModule, featureSettingsModule)
        }
    }
}