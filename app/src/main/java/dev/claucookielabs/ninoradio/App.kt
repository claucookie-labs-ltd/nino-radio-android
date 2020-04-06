package dev.claucookielabs.ninoradio

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

}
