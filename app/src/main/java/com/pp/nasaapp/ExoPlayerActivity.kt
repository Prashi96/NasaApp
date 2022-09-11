package com.pp.nasaapp

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.PersistableBundle
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.PlayerView

class ExoPlayerActivity : AppCompatActivity(), Player.Listener {

    private val KEY_VIDEO_URI = "video_uri"
    var videoUrl: String? = null
    var exoPlayerView: PlayerView? = null
    var exoPlayer: SimpleExoPlayer? = null
    lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
            setContentView(R.layout.activity_exo_player)

        val configuration: Configuration = resources.configuration
        configuration.fontScale = 1.toFloat() //0.85 small size, 1 normal size, 1,15 big etc
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        metrics.scaledDensity = configuration.fontScale * metrics.density
        configuration.densityDpi = resources.displayMetrics.xdpi.toInt()
        baseContext.resources.updateConfiguration(configuration, metrics)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        // To disable zooming variations in device level
          exoPlayerView = findViewById(R.id.videoFullScreenPlayer)
        progressBar = findViewById(R.id.progressBar)
        if (intent.hasExtra(KEY_VIDEO_URI)) {
            videoUrl = intent.getStringExtra(KEY_VIDEO_URI)
            videoUrl = "https://www.youtube.com/embed/my1euFQHH-o?rel=0"
        }
        exoPlayer = SimpleExoPlayer.Builder(this).build()
        initializePlayer()
    }

    private fun initializePlayer() {
        exoPlayerView?.player = exoPlayer
        videoUrl?.let {
            val mediaItem: MediaItem = MediaItem.fromUri(it)
            exoPlayer?.setMediaItem(mediaItem)
        }
        exoPlayer?.prepare()
        exoPlayer?.playWhenReady =  true
        exoPlayer?.addListener(this)
    }

    fun exit(view: View?) {
        finish()
    }


    override fun onLoadingChanged(isLoading: Boolean) {
        /**
         * No operation required
         */
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        when (playbackState) {
            Player.STATE_BUFFERING -> {
                progressBar.visibility = View.VISIBLE
            }
            Player.STATE_READY -> {
                progressBar.visibility = View.GONE
            }
            else -> {
                /**
                 * No operation required
                 */
            }
        }
    }

    override fun onRepeatModeChanged(repeatMode: Int) {   /**
     * No operation required
     */}

    override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {   /**
     * No operation required
     */}


    override fun onPositionDiscontinuity(reason: Int) {   /**
     * No operation required
     */}


    override fun onSeekProcessed() {   /**
     * No operation required
     */}


    override fun onRestart() {
        super.onRestart()
        resumePlayer()
    }


    override fun onPause() {
        super.onPause()
        pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    private fun releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.release()
            exoPlayer = null
        }
    }
    private fun resumePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = true
            exoPlayer!!.playbackState
        }
    }
    private fun pausePlayer() {
        if (exoPlayer != null) {
            exoPlayer!!.playWhenReady = false
            exoPlayer!!.playbackState
        }
    }

}