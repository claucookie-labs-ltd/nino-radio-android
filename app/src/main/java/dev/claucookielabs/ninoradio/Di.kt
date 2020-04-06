package dev.claucookielabs.ninoradio

import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

fun App.initKoin() {
    startKoin {
        androidLogger(Level.DEBUG)
        androidContext(this@initKoin)
        modules(listOf(appModules))
    }
}

private val appModules = module {
    single<SimpleExoPlayer> { ExoPlayerFactory.newSimpleInstance(androidContext()) }
}
