package com.sid.testscreenmirr.activities

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sid.testscreenmirr.databinding.ActivityImageDisplayBinding
import java.io.File
import java.io.FileNotFoundException

class ImageDisplayActivity : AppCompatActivity() {
    lateinit var binding:ActivityImageDisplayBinding
    var imgPath: String? = null
    var tabLayout: LinearLayout? = null
    var finalWd: Double? = null
    var finalHt:kotlin.Double? = null
    var mediaController: MediaController? = null
    var metadataRetriever: MediaMetadataRetriever? = null
    var imageHeight = 0
    var imageWidth = 0
    var fullScreenistrue = false
    var uiHidden = false
    var isVideo : Boolean = false
    var notFullScreen:Boolean = true
    var notLandScape:Boolean = true
    private var scaleGestureDetector: ScaleGestureDetector? = null
    var isDoubleClicked:Boolean = false
    private val mScaleFactor = 1.0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityImageDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imgPath = intent.getStringExtra("imgPath")
        isVideo = intent.getBooleanExtra("isVideo", false)

        metadataRetriever = MediaMetadataRetriever()
        mediaController = MediaController(this)
        binding.backBtn.setOnClickListener {
            finish()
        }

        val handler = Handler()
        val r = Runnable { isDoubleClicked = false }
        if (isVideo) {
//            binding.fullScreen.setVisibility(View.GONE)
            Toast.makeText(this, "Double tap video for full screen", Toast.LENGTH_SHORT).show()
            binding.idIVImage.setVisibility(View.GONE)
            binding.videoView.setVisibility(View.VISIBLE)
            metadataRetriever!!.setDataSource(imgPath)
            val width =
                metadataRetriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)
            val height =
                metadataRetriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)
            finalWd = width!!.toDouble()
            finalHt = height!!.toDouble()
            binding.videoView.setVideoPath(imgPath)
            binding.videoView.start()
            mediaController!!.setAnchorView( binding.videoView)
            mediaController!!.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {}
                override fun onViewDetachedFromWindow(v: View) {}
            })
            binding.videoView.setMediaController(mediaController)
        } else {
            binding.videoView.setVisibility(View.GONE)
            binding.idIVImage.setVisibility(View.VISIBLE)
            binding.relativeLayoutParent.setFitsSystemWindows(true)
            val imgFile = File(imgPath)
            if (imgFile.exists()) {
//                binding.fullScreen.setVisibility(View.VISIBLE)
                Glide.with(this).load(imgPath).into(binding.idIVImage)
                val options = BitmapFactory.Options()
                options.inJustDecodeBounds = true
                try {
                    BitmapFactory.decodeStream(
                        this.contentResolver.openInputStream(Uri.parse(imgPath)),
                        null,
                        options
                    )
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
                imageHeight = options.outHeight
                imageWidth = options.outWidth
            }
        }
            binding.videoView.setOnClickListener {
            if (isDoubleClicked) {
                isDoubleClicked = false
                if (notFullScreen) {
                    notFullScreen = false
                    if (finalWd!! <= finalHt!!) {
                        hideSystemUI()
                        binding.videoView.setLayoutParams(
                            RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.MATCH_PARENT,
                                RelativeLayout.LayoutParams.MATCH_PARENT
                            )
                        )
                        val layoutParams =
                            binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams
                        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                        binding.tabLayout.setVisibility(View.GONE)
                        binding.relativeLayoutParent.setFitsSystemWindows(true)
                    } else if (finalWd!! > finalHt!!) {
                        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                    }
                } else {
                    notFullScreen = true
                    showSystemUI()
                    binding.videoView.setLayoutParams(
                        RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        )
                    )
                    val layoutParams =  binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams
                    layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                    binding.tabLayout.setVisibility(View.VISIBLE)
                    binding.relativeLayoutParent.setFitsSystemWindows(true)
                    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
                handler.removeCallbacks(r)
            } else {
                isDoubleClicked = true
                handler.postDelayed(r, 1000)
            }
        }
        scaleGestureDetector = ScaleGestureDetector(this, ScaleListener())
        val filename = imgPath!!.substring(imgPath!!.lastIndexOf("/") + 1)
        binding.mediaName.setText(filename)
        val decorView = window.decorView
        decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                showSystemUI()
            } else {
                hideSystemUI()
            }
        }

    }
    override fun onTouchEvent(motionEvent: MotionEvent?): Boolean {
        scaleGestureDetector!!.onTouchEvent(motionEvent)
        return true
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
        } else {
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideSystemUI()
            binding.videoView.setLayoutParams(
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
            )
            val layoutParams =  binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            tabLayout!!.visibility = View.GONE
            binding.relativeLayoutParent.setFitsSystemWindows(false)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            binding.videoView.setLayoutParams(
                RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
            )
            val layoutParams =  binding.videoView.getLayoutParams() as RelativeLayout.LayoutParams
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
            tabLayout!!.visibility = View.VISIBLE
            binding.relativeLayoutParent.setFitsSystemWindows(true)
        }
    }

    private class ScaleListener : SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
//            mScaleFactor = scaleGestureDetector.scaleFactor
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f))
//            imageView.setScaleX(mScaleFactor)
//            imageView.setScaleY(mScaleFactor)
            return true
        }
    }

    private fun hideSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
        uiHidden = true
    }

    private fun showSystemUI() {
        val decorView = window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        uiHidden = false
    }
}
