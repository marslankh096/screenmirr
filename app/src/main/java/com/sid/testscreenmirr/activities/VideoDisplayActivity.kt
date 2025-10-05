package com.sid.testscreenmirr.activities

import androidx.appcompat.app.AppCompatActivity
import com.sid.testscreenmirr.interfaces.itemClickListener
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.os.Bundle
import com.sid.testscreenmirr.adapters.PictureAdapter
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import com.sid.testscreenmirr.modelclasses.PicHolder
import com.sid.testscreenmirr.databinding.ActivityVideoDisplayBinding
import java.lang.Exception
import java.util.ArrayList

class VideoDisplayActivity : AppCompatActivity(), itemClickListener {
    lateinit var binding:ActivityVideoDisplayBinding
    var allpictures=ArrayList<PictureFacer>()
    var foldePath: String? = null
      override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityVideoDisplayBinding.inflate(layoutInflater)
        setContentView(binding.root)
          binding.apply {
             foldername.setText(intent.getStringExtra("folderName"))
              foldePath = intent.getStringExtra("folderPath")
              BannerAd(this@VideoDisplayActivity)
                  .loadAdaptiveBanner(binding.bannerAdId,binding.bannerText)
             bakcImg.setOnClickListener {
                  finish()
              }
              allpictures = ArrayList()
              recycler.addItemDecoration(
                  MarginDecoration(
                      this@VideoDisplayActivity
                  )
              )
              val gridLayoutManager = GridLayoutManager(this@VideoDisplayActivity, 2)
             recycler.setLayoutManager(gridLayoutManager)
              recycler.hasFixedSize()
              if (allpictures!!.isEmpty()) {
                  loader.setVisibility(View.VISIBLE)
                  allpictures = getAllVideosByFolder(foldePath)
                 recycler.setAdapter(
                      PictureAdapter(
                          allpictures,
                          this@VideoDisplayActivity,
                          this@VideoDisplayActivity,
                          "Videos"
                      )
                  )
                 loader.setVisibility(View.GONE)
              } else {
              }
          }
    }
    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {
        val i = Intent(this@VideoDisplayActivity, VideoPlayActivity::class.java)
        i.putExtra("imgPath", pics[position].picturePath)
        i.putExtra("isVideo", true)
        startActivity(i)
    }
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {}
    fun getAllVideosByFolder(path: String?): ArrayList<PictureFacer> {
        var videos = ArrayList<PictureFacer>()
        val allVideosuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.SIZE
        )
        val cursor = this@VideoDisplayActivity.contentResolver.query(
            allVideosuri, projection, MediaStore.Video.Media.DATA + " like ? ", arrayOf(
                "%$path%"
            ), null
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pic = PictureFacer()
                pic.picturName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                pic.picturePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                pic.pictureSize =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE))
                videos.add(pic)
            } while (cursor.moveToNext())
            cursor.close()
            val reSelection = ArrayList<PictureFacer>()
            for (i in videos.size - 1 downTo -1 + 1) {
                reSelection.add(videos[i])
            }
            videos = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return videos
    }
}