package com.sid.testscreenmirr.activities

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.databinding.ActivityVideoPlayBinding

class VideoPlayActivity : AppCompatActivity() {
    lateinit var binding:ActivityVideoPlayBinding
    private var playbackPosition = 0L
    private var playWhenReady = true
    private lateinit var imgPath: String
    private var isVideo: Boolean = false
    private var exoPlayer: ExoPlayer? = null
    private lateinit var imageViewFullScreen: ImageView
    private lateinit var extendedfullScreen: ImageView
    private lateinit var imageViewLock: ImageView
    private lateinit var linearLayoutControlUp: LinearLayout
    private lateinit var linearLayoutControlBottom: LinearLayout
    private lateinit var parentRelative: ConstraintLayout
    private lateinit var controllerbg: RelativeLayout
    private lateinit var controlleroverlayy: RelativeLayout
    private lateinit var contentFrame: AspectRatioFrameLayout
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityVideoPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imgPath = intent.getStringExtra("imgPath").toString()
        isVideo = intent.getBooleanExtra("isVideo", false)
        preparePlayer()
        imageViewFullScreen = findViewById<ImageView>(R.id.imageViewFullScreen)
        extendedfullScreen = findViewById<ImageView>(R.id.imageViewAdvanceFullScreen)
        linearLayoutControlUp = findViewById(R.id.linearLayoutControlUp)
        linearLayoutControlBottom = findViewById(R.id.linearLayoutControlBottom)
        parentRelative = findViewById(R.id.parentRelative)
        controllerbg = findViewById(R.id.controllerbgg)
        controlleroverlayy = findViewById(R.id.controllerRl)
        contentFrame =findViewById(com.google.android.exoplayer2.R.id.exo_content_frame)
        contentFrame.fitsSystemWindows=false
        setFullScreen()
        setExtendedFullScreen()
    }

    private fun preparePlayer() {
        val mediaUri = Uri.parse(imgPath)
        exoPlayer = ExoPlayer.Builder(this).setSeekBackIncrementMs(5000)
            .setSeekForwardIncrementMs(5000)
            .build()
        exoPlayer?.playWhenReady = true
        binding.player.player = exoPlayer
        val mediaItem: MediaItem = MediaItem.fromUri(mediaUri)
        exoPlayer?.setMediaItem(mediaItem)
        exoPlayer?.seekTo(playbackPosition)
        exoPlayer?.playWhenReady = playWhenReady
        exoPlayer?.prepare()
    }
    private fun lockScreen(lock: Boolean) {
        if (lock) {
            linearLayoutControlUp.visibility = View.INVISIBLE
            linearLayoutControlBottom.visibility = View.INVISIBLE
            imageViewFullScreen.visibility= View.INVISIBLE
            controllerbg.visibility= View.INVISIBLE
        } else {
            linearLayoutControlUp.visibility = View.VISIBLE
            linearLayoutControlBottom.visibility = View.VISIBLE
            imageViewFullScreen.visibility= View.VISIBLE
            controllerbg.visibility= View.VISIBLE
        }
    }
    @SuppressLint("SourceLockedOrientationActivity")
    private fun setFullScreen() {
        imageViewFullScreen.setOnClickListener {

            if (!isFullScreen) {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_screen_rotation_24
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            } else {
                imageViewFullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_screen_rotation_24
                    )
                )
                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
            isFullScreen = !isFullScreen
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    private fun setExtendedFullScreen() {
        extendedfullScreen.setOnClickListener {

            if (!isExtendedFullScreen) {
                extendedfullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_24
                    )
                )
                binding.player.hideController()
                applyInsets()
                hideBars()
            } else {
                showSystemUI()
                extendedfullScreen.setImageDrawable(
                    ContextCompat.getDrawable(
                        applicationContext,
                        R.drawable.ic_baseline_fullscreen_24
                    )
                )
                binding.player.showController()

            }
        }
    }
    override fun onBackPressed() {
        if (isLock) return
        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageViewFullScreen.performClick()
        } else super.onBackPressed()
    }
    override fun onStop() {
        super.onStop()
        exoPlayer?.stop()
    }
    override fun onDestroy() {
        super.onDestroy()
        exoPlayer?.release()
    }
    override fun onPause() {
        super.onPause()
        exoPlayer?.pause()
    }
    private fun showSystemUI() {
        isExtendedFullScreen =false
        val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        windowInsetsController.show(WindowInsetsCompat.Type.systemBars())
        windowInsetsController.show(WindowInsetsCompat.Type.navigationBars())

    }
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        var currentPosition: Long = exoPlayer?.currentPosition!!
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            exoPlayer?.seekTo(currentPosition)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            exoPlayer?.seekTo(currentPosition)
        }
    }

    fun hideBars(){
        isExtendedFullScreen =true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowInsetsController = ViewCompat.getWindowInsetsController(window.decorView) ?: return
            windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            binding.player.fitsSystemWindows=false
            parentRelative.fitsSystemWindows=false

        }else{
            val flags =
                (View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            window!!.decorView.setSystemUiVisibility(flags)
        }
    }
    @RequiresApi(Build.VERSION_CODES.R)
    fun applyInsets(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val topPadding: Int = WindowInsets.Type.systemBars()
            binding.player.setOnApplyWindowInsetsListener { v, insets ->
                binding.player.setPadding(
                    0,
                    -topPadding,
                    0,
                    0
                )
                insets
            }
        }
        return

    }



    private fun setWindowFlag(bits: Int, on: Boolean) {
        val win = window
        val winParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    companion object {
        var isFullScreen = false
        var isExtendedFullScreen = false
        var isLock = false
    }
}
