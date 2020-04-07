package dev.claucookielabs.ninoradio

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.Player.STATE_ENDED
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import org.koin.android.ext.android.inject

class RadioPlayerService : Service() {

    private var configurationChanged = false
    private var isInForeground = false
    private val player: SimpleExoPlayer by inject()
    private val binder = LocalBinder()
    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    override fun onBind(intent: Intent?): IBinder? {
        Log.i("Info", "RadioPlayer service Binded")
        configurationChanged = false
        stopForeground(true)
        isInForeground = false
        Log.i("Info", "RadioPlayer service to background")
        return binder
    }

    override fun onRebind(intent: Intent?) {
        Log.i("Info", "RadioPlayer service reBinded")
        configurationChanged = false
        stopForeground(true)
        isInForeground = false
        Log.i("Info", "RadioPlayer service to background")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.i("Info", "RadioPlayer service unBinded")
        if (!configurationChanged) {
            Log.i("Info", "RadioPlayer service to foreground")
            startForeground(NOTIFICATION_ID, createNotification())
            isInForeground = true
        }
        return true
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.i("Info", "RadioPlayer service changed orientation")
        configurationChanged = true
    }

    override fun onCreate() {
        super.onCreate()
        Log.i("Info", "RadioPlayer service onCreate")

    }

    override fun onDestroy() {
        Log.i("Info", "RadioPlayer service onDestroy")
        super.onDestroy()
    }

    fun startRadio() {
        try {
            startService(Intent(applicationContext, RadioPlayerService::class.java))
        } catch (ex: IllegalStateException) {
            stopSelf()
        }
    }

    fun play() {
        initializePlayer()
    }

    fun pause() {
        player.stop()
    }

    fun isPlaying(): Boolean = player.isPlaying

    private fun initializePlayer() {
        if (!player.isPlaying) {
            val uri = Uri.parse(getString(R.string.media_url_mp3))
            val mediaSource: MediaSource = buildMediaSource(uri)
            player.playWhenReady = playWhenReady
            player.seekTo(currentWindow, playbackPosition)
            player.prepare(mediaSource, false, false)
            player.addListener(object : Player.EventListener {
                override fun onPlayerError(error: ExoPlaybackException?) {
                    super.onPlayerError(error)
                    pause()
                    stopSelf()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    /**
                     * The player does not have any media to play.
                     *
                     * val STATE_IDLE = 1

                     * The player is not able to immediately play from its current position. This state typically
                     * occurs when more data needs to be loaded.
                     *
                     * val STATE_BUFFERING = 2

                     * The player is able to immediately play from its current position. The player will be playing if
                     * [.getPlayWhenReady] is true, and paused otherwise.
                     *
                     * val STATE_READY = 3

                     * The player has finished playing the media.
                     *
                     * val STATE_ENDED = 4
                     **/
                    if (playbackState == STATE_ENDED) {
                        pause()
                        stopSelf()
                    }
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                }
            })

        }
    }

    private fun buildMediaSource(uri: Uri): MediaSource {
        val dataSourceFactory: DataSource.Factory = DefaultDataSourceFactory(this, "nino-radio")
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun createNotification(): Notification {
        val title = getString(R.string.playing_radio_station)
        val content = getString(R.string.playing_cofrade)
        val intent = Intent(this, MainActivity::class.java)
        val builder = NotificationCompat.Builder(this, createNotificationChannel())
            .setContentText(content)
            .setContentTitle(title)
            .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
            .setColor(ContextCompat.getColor(this, R.color.colorPrimaryLight))
            .setColorized(true)
            .setOngoing(true)
            .setPriority(NotificationManagerCompat.IMPORTANCE_LOW)
            .setSmallIcon(R.drawable.ic_nino_notif)
            .setTicker(content)
            .setContentIntent(PendingIntent.getActivity(this, 0, intent, 0))
            .setWhen(System.currentTimeMillis())

        return builder.build()
    }

    private fun createNotificationChannel(): String {
        if (Util.SDK_INT >= 26) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            channel.lightColor = R.color.colorPrimaryLight
            channel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
            return channel.id
        }
        return NOTIFICATION_CHANNEL_ID
    }


    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    inner class LocalBinder : Binder() {

        val playerService: RadioPlayerService
            get() {
                return this@RadioPlayerService
            }
    }
}


private const val NOTIFICATION_CHANNEL_ID = "dev.claucookielabs.ninoradio"
private const val NOTIFICATION_CHANNEL_NAME = "Radio Notifications"
private const val NOTIFICATION_ID: Int = 32862234
