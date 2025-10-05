package com.sid.testscreenmirr.activities

import android.annotation.SuppressLint
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.databinding.ActivityAudioPlayerBinding
import java.io.IOException
import java.util.concurrent.TimeUnit

class AudioPlayerActivity : AppCompatActivity() {
    lateinit var binding:ActivityAudioPlayerBinding
    var imgPath: String? = null
    var scaleDownAnim: Animation? = null
    private var mPlayer: MediaPlayer? = null
     var seekHandler:Handler=Handler()
    var eTime:Int=0
    var sTime:Int=0
    var oTime:Int=0
    val hdlr = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        imgPath = intent.getStringExtra("imgPath")
        scaleDownAnim = AnimationUtils.loadAnimation(this, R.anim.scale_down)
        val filename = imgPath!!.substring(imgPath!!.lastIndexOf("/") + 1)
        mPlayer = MediaPlayer()
        mPlayer!!.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mPlayer!!.setDataSource(this, Uri.parse(imgPath))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        try {
            mPlayer!!.prepareAsync()
        } catch (e: Exception) {
            binding.animationView.pauseAnimation()
            Toast.makeText(this, "Error playing file!", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
        mPlayer!!.setOnPreparedListener { mp ->
            updateSeekBar()
            mp.start()
            binding.animationView.playAnimation()
            binding.btnPlay.setImageResource(R.drawable.ic_playmusicicon)
            binding.sBar.setMax(mPlayer!!.duration)
        }
        binding.txtSname.setText(filename)
        binding.btnPause.setEnabled(false)
       eTime = mPlayer!!.duration
       sTime =
            mPlayer!!.currentPosition

        binding.sBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
              eTime =
                    mPlayer!!.duration
               sTime = progress
                binding.txtSongTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(eTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(eTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(eTime.toLong())
                        )
                    )
                )
                binding.txtStartTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(sTime.toLong())
                        )
                    )
                )
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                mPlayer!!.seekTo(seekBar.progress)
               sTime =
                    seekBar.progress
                binding.txtSongTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(eTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(eTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(eTime.toLong())
                        )
                    )
                )
                binding.txtStartTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(sTime.toLong())
                        )
                    )
                )
            }
        })
        binding.btnPlay.setOnClickListener{
            if (mPlayer!!.isPlaying) {
                mPlayer!!.pause()
                binding.animationView.pauseAnimation()
                binding.btnPlay.setImageResource(R.drawable.ic_pause)
            } else {
                mPlayer!!.start()
                binding.animationView.playAnimation()
                binding.btnPlay.setImageResource(R.drawable.ic_playmusicicon)
                if (oTime == 0) {
                    oTime = 1
                }
                binding.sBar.setMax(mPlayer!!.duration)
                binding.txtSongTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(eTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(eTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(eTime.toLong())
                        )
                    )
                )
                binding.txtStartTime.setText(
                    String.format(
                        "%d : %d ",
                        TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                        TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(sTime.toLong())
                        )
                    )
                )
                hdlr.postDelayed(UpdateSongTime, 100)
            }
        }
        binding.btnPause.setOnClickListener {
            mPlayer!!.pause()
            binding.btnPause.setEnabled(false)
            binding.btnPlay.setEnabled(true)
            Toast.makeText(applicationContext, "Pausing Audio", Toast.LENGTH_SHORT).show()
        }
        binding.btnForward.setOnClickListener {
            if (mPlayer!!.currentPosition + 5000 <= mPlayer!!.duration) {
                mPlayer!!.seekTo(mPlayer!!.currentPosition + 5000)
            } else {
                mPlayer!!.seekTo(mPlayer!!.duration)
            }
        }
        binding.btnBackward.setOnClickListener{
            if (mPlayer!!.currentPosition - 5000 > 0) {
                mPlayer!!.seekTo(mPlayer!!.currentPosition - 5000)
            } else {
                mPlayer!!.seekTo(0)
            }
        }

        mPlayer!!.setOnCompletionListener {
            binding.animationView.pauseAnimation()
            binding.btnPlay.setImageResource(R.drawable.ic_pause)
        }

        binding.backBtn.setOnClickListener {
            scaleDownAnim!!.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    onBackPressed()
                }
                override fun onAnimationRepeat(animation: Animation) {}
            })
            binding.backBtn.startAnimation(scaleDownAnim)
        }
    }
    private val UpdateSongTime: Runnable = object : Runnable {
        @SuppressLint("DefaultLocale")
        override fun run() {
         sTime =
                mPlayer!!.currentPosition
            binding.txtStartTime.setText(
                String.format(
                    "%d : %d ",
                    TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()),
                    TimeUnit.MILLISECONDS.toSeconds(sTime.toLong()) - TimeUnit.MINUTES
                        .toSeconds(TimeUnit.MILLISECONDS.toMinutes(sTime.toLong()))
                )
            )
            hdlr.postDelayed(this, 100)
        }
    }
    var runnable = Runnable { updateSeekBar() }
    private fun updateSeekBar() {
        binding.sBar.setProgress(mPlayer!!.currentPosition)
        seekHandler.postDelayed(runnable, 100)
    }
    override fun onBackPressed() {
        try {
            mPlayer!!.stop()
        } catch (e: Exception) {
            Log.d("", "")
        }
        super.onBackPressed()
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}