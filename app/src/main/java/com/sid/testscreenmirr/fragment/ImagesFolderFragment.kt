package com.sid.testscreenmirr.fragment

import com.sid.testscreenmirr.interfaces.itemClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import com.sid.testscreenmirr.modelclasses.ImagesFolderItems
import com.sid.testscreenmirr.adapters.PictureFolderAdapter
import android.provider.MediaStore
import com.sid.testscreenmirr.modelclasses.PicHolder
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sid.testscreenmirr.activities.NewImageDisActivty
import com.sid.testscreenmirr.databinding.FragmentImagesFolderBinding
import java.lang.Exception
import java.util.ArrayList

class ImagesFolderFragment : Fragment(), itemClickListener {
    lateinit var binding: FragmentImagesFolderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImagesFolderBinding.inflate(inflater, container, false)
        binding.apply {
            BannerAd(requireContext()).loadAdaptiveBanner(bannerAdId,bannerText)
           imagefolderRecycler.addItemDecoration(MarginDecoration(requireActivity()))
            val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
           imagefolderRecycler.layoutManager = gridLayoutManager
           imagefolderRecycler.hasFixedSize()
            val folds = picturePaths
            if (!folds.isEmpty()) {
                val folderAdapter: RecyclerView.Adapter<*> =
                    PictureFolderAdapter(folds, requireActivity(), this@ImagesFolderFragment, "Images")
              imagefolderRecycler.adapter = folderAdapter
            }
        }

        return binding.root
    }//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview

    //String folderpaths =  datapath.replace(name,"");
    private val picturePaths: ArrayList<ImagesFolderItems>
        private get() {
            val picFolders = ArrayList<ImagesFolderItems>()
            val picPaths = ArrayList<String>()
            val allImagesuri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Images.ImageColumns.DATA, MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.BUCKET_ID
            )
            val cursor =
                requireActivity().contentResolver.query(allImagesuri, projection, null, null, null)
            try {
                cursor?.moveToFirst()
                do {
                    val folds = ImagesFolderItems()
                    val name =
                        cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))
                    val folder =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME))
                    val datapath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA))

                    //String folderpaths =  datapath.replace(name,"");
                    var folderpaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                    folderpaths = "$folderpaths$folder/"
                    if (!picPaths.contains(folderpaths)) {
                        picPaths.add(folderpaths)
                        folds.path = folderpaths
                        folds.folderName = folder
                        folds.firstPic =
                            datapath //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                        folds.addpics()
                        picFolders.add(folds)
                    } else {
                        for (i in picFolders.indices) {
                            if (picFolders[i].path == folderpaths) {
                                picFolders[i].firstPic = datapath
                                picFolders[i].addpics()
                            }
                        }
                    }
                } while (cursor!!.moveToNext())
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (i in picFolders.indices) {
                Log.d(
                    "picture folders",
                    picFolders[i].folderName + " and path = " + picFolders[i].path + " " + picFolders[i].numberOfPics
                )
            }
            return picFolders
        }

    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {}
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {
        val move = Intent(requireActivity(), NewImageDisActivty::class.java)
        move.putExtra("folderPath", pictureFolderPath)
        move.putExtra("folderName", folderName)
        startActivity(move)
    }
}