package com.sid.testscreenmirr.activities

import androidx.appcompat.app.AppCompatActivity
import com.sid.testscreenmirr.interfaces.itemClickListener
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.os.Bundle
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import com.sid.testscreenmirr.adapters.PictureAdapter
import android.provider.MediaStore
import com.sid.testscreenmirr.modelclasses.PicHolder
import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.databinding.ActivitySingleAudioPlayBinding
import java.lang.Exception
import java.util.ArrayList

class SingleAudioPlayActivity : AppCompatActivity(), itemClickListener {
    lateinit var binding:ActivitySingleAudioPlayBinding
    var allpictures= ArrayList<PictureFacer>()
    var foldePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySingleAudioPlayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            BannerAd(this@SingleAudioPlayActivity).loadAdaptiveBanner(bannerAdId,bannerText)
            foldername.setText(intent.getStringExtra("folderName"))
            foldePath = intent.getStringExtra("folderPath")
            allpictures = ArrayList()
          recycler.addItemDecoration(
                MarginDecoration(
                    this@SingleAudioPlayActivity
                )
            )
            val gridLayoutManager = GridLayoutManager(this@SingleAudioPlayActivity, 1)
            recycler.setLayoutManager(gridLayoutManager)
            recycler.hasFixedSize()
            if (allpictures.isEmpty()) {
                loader.setVisibility(View.VISIBLE)
                allpictures = getAllAudiosByFolder(foldePath)
                binding.recycler.setAdapter(
                    PictureAdapter(
                        allpictures,
                        this@SingleAudioPlayActivity,
                        this@SingleAudioPlayActivity,
                        "Audios"
                    )
                )
                binding.loader.setVisibility(View.GONE)
            } else {
            }
        }

    }
    fun getAllAudiosByFolder(path: String?): ArrayList<PictureFacer> {
        var audio = ArrayList<PictureFacer>()
        val allAudiosuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.SIZE
        )
        val cursor = this@SingleAudioPlayActivity.contentResolver.query(
            allAudiosuri, projection, MediaStore.Audio.Media.DATA + " like ? ", arrayOf(
                "%$path%"
            ), null
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pic = PictureFacer()
                pic.picturName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                pic.picturePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))
                pic.pictureSize =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE))
                audio.add(pic)
            } while (cursor.moveToNext())
            cursor.close()
            val reSelection = ArrayList<PictureFacer>()
            for (i in audio.size - 1 downTo -1 + 1) {
                reSelection.add(audio[i])
            }
            audio = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return audio
    }
    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {
        val i = Intent(this@SingleAudioPlayActivity, AudioPlayerActivity::class.java)
        i.putExtra("imgPath", "" + pics[position].picturePath)
        startActivity(i)
    }
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {}
}