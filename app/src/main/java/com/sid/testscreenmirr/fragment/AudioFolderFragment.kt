package com.sid.testscreenmirr.fragment

import com.sid.testscreenmirr.interfaces.itemClickListener
import com.sid.testscreenmirr.modelclasses.ImagesFolderItems
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.modelclasses.MarginDecoration
import android.os.Build
import com.sid.testscreenmirr.adapters.AllAudiioFolderAdapter
import android.widget.Toast
import android.provider.MediaStore
import com.sid.testscreenmirr.modelclasses.PicHolder
import com.sid.testscreenmirr.modelclasses.PictureFacer
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sid.testscreenmirr.activities.SingleAudioPlayActivity
import com.sid.testscreenmirr.databinding.FragmentAudioFolderBinding
import java.lang.Exception
import java.util.ArrayList

class AudioFolderFragment : Fragment(), itemClickListener {
    lateinit var binding: FragmentAudioFolderBinding
    var folds= ArrayList<ImagesFolderItems>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudioFolderBinding.inflate(inflater, container, false)
        binding.apply {
            BannerAd(requireActivity()).loadAdaptiveBanner(binding.bannerAdId, binding.bannerText)
            binding.audioRv.addItemDecoration(MarginDecoration(requireActivity()))
            val gridLayoutManager = GridLayoutManager(requireActivity(), 2)
            binding.audioRv.layoutManager = gridLayoutManager
            binding.audioRv.hasFixedSize()
            folds = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                audioPathsUpToOreo
            } else {
                audioPathsBelowOreo
            }
            if (!folds.isEmpty()) {
                val folderAdapter: RecyclerView.Adapter<*> =
                    AllAudiioFolderAdapter(folds, requireActivity(), this@AudioFolderFragment, "Audios")
                binding.audioRv.adapter = folderAdapter
            } else {
                Toast.makeText(requireActivity(), "This folder is empty", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview//String folderpaths =  datapath.replace(name,"");

    //        Toast.makeText(requireActivity(), "getAudioPathsBelowOreo", Toast.LENGTH_LONG).show();
    private val audioPathsBelowOreo: ArrayList<ImagesFolderItems>
        private get() {
//        Toast.makeText(requireActivity(), "getAudioPathsBelowOreo", Toast.LENGTH_LONG).show();
            val audioFolders = ArrayList<ImagesFolderItems>()
            val audioPaths = ArrayList<String>()
            val allAudiossuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val cursor =
                requireActivity().contentResolver.query(allAudiossuri, projection, null, null, null)
            try {
                cursor?.moveToFirst()
                do {
                    val folds = ImagesFolderItems()
                    val name =
                        cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val folder =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM))
                    val datapath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                    //String folderpaths =  datapath.replace(name,"");
                    var folderpaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                    folderpaths = "$folderpaths$folder/"
                    if (!audioPaths.contains(folderpaths)) {
                        audioPaths.add(folderpaths)
                        folds.path = folderpaths
                        folds.folderName = folder
                        folds.firstPic =
                            datapath //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                        folds.addpics()
                        audioFolders.add(folds)
                    } else {
                        for (i in audioFolders.indices) {
                            if (audioFolders[i].path == folderpaths) {
                                audioFolders[i].firstPic = datapath
                                audioFolders[i].addpics()
                            }
                        }
                    }
                } while (cursor!!.moveToNext())
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (i in audioFolders.indices) {
                Log.d(
                    "picture folders",
                    audioFolders[i].folderName + " and path = " + audioFolders[i].path + " " + audioFolders[i].numberOfPics
                )
            }
            return audioFolders
        }//if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview//String folderpaths =  datapath.replace(name,"");

    //        Toast.makeText(requireActivity(), "getAudioPathsUpToOreo", Toast.LENGTH_LONG).show();
    private val audioPathsUpToOreo: ArrayList<ImagesFolderItems>
        private get() {

//        Toast.makeText(requireActivity(), "getAudioPathsUpToOreo", Toast.LENGTH_LONG).show();
            val audioFolders = ArrayList<ImagesFolderItems>()
            val audioPaths = ArrayList<String>()
            val allAudiossuri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val projection = arrayOf(
                MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Audio.Media.BUCKET_ID
            )
            val cursor =
                requireActivity().contentResolver.query(allAudiossuri, projection, null, null, null)
            try {
                cursor?.moveToFirst()
                do {
                    val folds = ImagesFolderItems()
                    val name =
                        cursor!!.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME))
                    val folder =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.BUCKET_DISPLAY_NAME))
                    val datapath =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA))

                    //String folderpaths =  datapath.replace(name,"");
                    var folderpaths = datapath.substring(0, datapath.lastIndexOf("$folder/"))
                    folderpaths = "$folderpaths$folder/"
                    if (!audioPaths.contains(folderpaths)) {
                        audioPaths.add(folderpaths)
                        folds.path = folderpaths
                        folds.folderName = folder
                        folds.firstPic =
                            datapath //if the folder has only one picture this line helps to set it as first so as to avoid blank image in itemview
                        folds.addpics()
                        audioFolders.add(folds)
                    } else {
                        for (i in audioFolders.indices) {
                            if (audioFolders[i].path == folderpaths) {
                                audioFolders[i].firstPic = datapath
                                audioFolders[i].addpics()
                            }
                        }
                    }
                } while (cursor!!.moveToNext())
                cursor.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            for (i in audioFolders.indices) {
                Log.d(
                    "picture folders",
                    audioFolders[i].folderName + " and path = " + audioFolders[i].path + " " + audioFolders[i].numberOfPics
                )
            }
            return audioFolders
        }

    override fun onPicClicked(holder: PicHolder, position: Int, pics: ArrayList<PictureFacer>) {}
    override fun onPicClicked(pictureFolderPath: String, folderName: String) {
        val move = Intent(requireActivity(), SingleAudioPlayActivity::class.java)
        move.putExtra("folderPath", pictureFolderPath)
        move.putExtra("folderName", folderName)
        startActivity(move)
    }
}