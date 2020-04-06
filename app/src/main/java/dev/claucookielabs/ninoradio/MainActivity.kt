package dev.claucookielabs.ninoradio

import android.app.NotificationManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var notificationManager: NotificationManager
    private var radioPlayerService: RadioPlayerService? = null
    private var isRadioPlayerServiceBound = false
    private lateinit var equalizerView: EqualizerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        equalizerView = findViewById(R.id.equalizer_view)
    }

    override fun onStart() {
        super.onStart()
        bindRadioPlayerService()
    }

    override fun onStop() {
        unbindRadioPlayerService()
        super.onStop()
    }

    fun togglePlay(view: View?) {
        if (radioPlayerService?.isPlaying() == true) {
            radioPlayerService?.pause()
            showPaused()
        } else {
            radioPlayerService?.play()
            showPlaying()
        }
    }

    private fun showPlaying() {
        play_fab.setImageResource(android.R.drawable.ic_media_pause)
        equalizerView.animateBars()
    }

    private fun showPaused() {
        play_fab.setImageResource(android.R.drawable.ic_media_play)
        equalizerView.stopBars()
    }

    private fun bindRadioPlayerService() {
        // Bind to the service. If the service is in foreground mode, this signals to the service
        // that since this activity is in the foreground, the service can exit foreground mode.
        bindService(
            Intent(this, RadioPlayerService::class.java),
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
        Log.i("Info", "Bind RadioPlayer service")
    }

    private fun unbindRadioPlayerService() {
        if (isRadioPlayerServiceBound) {
            Log.i("Info", "Unbinding RadioPlayer service")
            unbindService(serviceConnection)
            isRadioPlayerServiceBound = false
        }
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            Log.i("Info", "RadioPlayer service Connected")
            val binder: RadioPlayerService.LocalBinder =
                service as RadioPlayerService.LocalBinder
            radioPlayerService = binder.playerService
            isRadioPlayerServiceBound = true
            play_fab.isClickable = true
            radioPlayerService?.startRadio()
            if (radioPlayerService?.isPlaying() == true) {
                showPlaying()
            } else {
                showPaused()
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.i("Info", "RadioPlayer service Disconnected")
            radioPlayerService = null
            isRadioPlayerServiceBound = false
            play_fab.isClickable = false
        }
    }
}
