package com.sid.testscreenmirr.fragment

import com.sid.testscreenmirr.interfaces.itemClickListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sid.testscreenmirr.activities.VideoDisplayActivity
import com.sid.testscreenmirr.adapters.VideoFolderAdapter
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.databinding.FragmentVideoFoldersBinding
import com.sid.testscreenmirr.modelclasses.ImagesFolderItems
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import com.sid.testscreenmirr.modelclasses.PicHolder
import java.lang.Exception
import java.util.ArrayList

class FragmentVideoFolders : Fragment(), itemClickListener {
    lateinit var binding: FragmentVideoFoldersBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentVideoFoldersBinding.inflate(inflater,container,false)
        binding.apply {
            BannerAd(requireContext())
                .loadAdaptiveBanner(bannerAdId,bannerText)
           videofolderRecycler.addItemDecoration(
                MarginDecoration(
                    requireActivity()
                )
            )
            val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
           videofolderRecycler.setLayoutManager(gridLayoutManager)
           videofolderRecycler.hasFixedSize()
            val folds = videoPaths
            if (!folds.isEmpty()) {
                val folderAdapter: RecyclerView.Adapter<*> =
                    VideoFolderAdapter(
                        folds,
                        requireActivity(),
                        this@FragmentVideoFolders,
                        "Videos"
                    )
               videofolderRecycler.setAdapter(folderAdapter)
            } else {
            }
        }

        return binding.root
    }
    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {}
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {
        val move = Intent(requireActivity(), VideoDisplayActivity::class.java)
        move.putExtra("folderPath", pictureFolderPath)
        move.putExtra("folderName", folderName)
        startActivity(move)
    }
    //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
    private val videoPaths: ArrayList<ImagesFolderItems>
        private get() {
            val videoFolders = ArrayList<ImagesFolderItems>()
            val videoPaths = ArrayList<String>()
            val allVideossuri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.BUCKET_ID
            )
            val cursor =
                requireActivity().contentResolver.query(allVideossuri, projection, null, null, null)
            try {
                cursor?.moveToFirst()
                do {
                    val folds =
                        ImagesFolderItems()
                    val name =
                        cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME))
                    val folder =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME))
                    val datapath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA))
                    var folderpaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                    folderpaths = "$folderpaths$folder/"
                    if (!videoPaths.contains(folderpaths)) {
                        videoPaths.add(folderpaths)
                        folds.path = folderpaths
                        folds.folderName = folder
                        folds.firstPic =
                            datapath //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                        folds.addpics()
                        videoFolders.add(folds)
                    } else {
                        for (i in videoFolders.indices) {
                            if (videoFolders[i].path == folderpaths) {
                                videoFolders[i].firstPic = datapath
                                videoFolders[i].addpics()
                            }
                        }
                    }
                } while (cursor!!.moveToNext())
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (i in videoFolders.indices) {
                Log.d(
                    "picture folders",
                    videoFolders[i].folderName + " and path = " + videoFolders[i].path + " " + videoFolders[i].numberOfPics
                )
            }
            return videoFolders
        }
}