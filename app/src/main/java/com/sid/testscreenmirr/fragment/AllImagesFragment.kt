package com.sid.testscreenmirr.fragment

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.adapters.ImageRecyclerViewAdapter
import com.sid.testscreenmirr.databinding.FragmentAllImagesBinding

class AllImagesFragment : Fragment() {

    lateinit var binding:FragmentAllImagesBinding
    lateinit var aDapter: ImageRecyclerViewAdapter
    var filePaths=ArrayList<String>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentAllImagesBinding.inflate(inflater,container,false)
        BannerAd(requireContext())
            .loadAdaptiveBanner(binding.bannerAdId,binding.bannerText)
        filePaths=ArrayList<String>()
        binding.apply {
            rv.layoutManager=GridLayoutManager(context,3)
            aDapter= ImageRecyclerViewAdapter(
                requireActivity(),
                filePaths
            )
            rv.adapter=aDapter
        }
        getImagePath()
        return binding.root
    }
    private fun getImagePath() {
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        if (isSDPresent) {
            val columns = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DATE_TAKEN
            )
            val orderBy = MediaStore.Images.Media.DATE_TAKEN + " DESC"
            val cursor = requireActivity().contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
            )
            val count = cursor!!.count
            for (i in 0 until count) {
                cursor.moveToPosition(i)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                filePaths?.add(cursor.getString(dataColumnIndex))
            }
            aDapter.notifyDataSetChanged()
            cursor.close()
        }
    }
}