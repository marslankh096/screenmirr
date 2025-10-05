package com.sid.testscreenmirr.activities

import androidx.appcompat.app.AppCompatActivity
import com.sid.testscreenmirr.interfaces.itemClickListener
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.os.Bundle
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import com.sid.testscreenmirr.adapters.PictureAdapter
import com.sid.testscreenmirr.modelclasses.PicHolder
import android.content.Intent
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.sid.testscreenmirr.databinding.ActivityNewImageDisActivtyBinding
import java.lang.Exception
import java.util.ArrayList

class NewImageDisActivty : AppCompatActivity(), itemClickListener {
    var allpictures= ArrayList<PictureFacer>()
    var foldePath: String? = null
    lateinit var binding:ActivityNewImageDisActivtyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityNewImageDisActivtyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.apply {
            BannerAd(this@NewImageDisActivty).loadAdaptiveBanner(bannerAdId,bannerText)
           foldername.setText(intent.getStringExtra("folderName"))
            foldePath = intent.getStringExtra("folderPath")
            allpictures = ArrayList()
           recycler.addItemDecoration(MarginDecoration(this@NewImageDisActivty))
            val gridLayoutManager = GridLayoutManager(this@NewImageDisActivty, 2)
          recycler.setLayoutManager(gridLayoutManager)
           recycler.hasFixedSize()
            if (allpictures!!.isEmpty()) {
                loader.setVisibility(View.VISIBLE)
                allpictures = getAllImagesByFolder(foldePath)
                recycler.setAdapter(PictureAdapter(allpictures,
                        this@NewImageDisActivty, this@NewImageDisActivty, "Images"))
               loader.setVisibility(View.GONE)
            } else {
            }
           bakcImg.setOnClickListener{ view: View? -> finish() }
        }
    }
    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {
        val i = Intent(this@NewImageDisActivty, ImageDisplayActivity::class.java)
        i.putExtra("imgPath", "" + pics[position].picturePath)
        i.putExtra("isVideo", false)
        startActivity(i)
    }
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {}
    fun getAllImagesByFolder(path: String?): ArrayList<PictureFacer> {
        var images = ArrayList<PictureFacer>()
        val allVideosuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
            MediaStore.Images.Media.SIZE
        )
        val cursor = this@NewImageDisActivty.contentResolver.query(
            allVideosuri, projection, MediaStore.Images.Media.DATA + " like ? ", arrayOf(
                "%$path%"
            ), null
        )
        try {
            cursor!!.moveToFirst()
            do {
                val pic = PictureFacer()
                pic.picturName =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                pic.picturePath =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))
                pic.pictureSize =
                    cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE))
                images.add(pic)
            } while (cursor.moveToNext())
            cursor.close()
            val reSelection = ArrayList<PictureFacer>()
            for (i in images.size - 1 downTo -1 + 1) {
                reSelection.add(images[i])
            }
            images = reSelection
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return images
    }
}