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
import com.sid.testscreenmirr.adapters.GalleryVideoAdapter
import com.sid.testscreenmirr.databinding.FragmentAllVideoBinding

class AllVideoFragment : Fragment() {
    lateinit var binding: FragmentAllVideoBinding
    lateinit var adapter: GalleryVideoAdapter
    var list= ArrayList<String?>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         binding=FragmentAllVideoBinding.inflate(inflater,container,false)
        binding.apply {
            BannerAd(requireContext())
                .loadAdaptiveBanner(bannerAdId,bannerText)
            videosRv.layoutManager=GridLayoutManager(requireContext(),2)
            adapter= GalleryVideoAdapter(requireActivity(), list)
            videosRv.adapter=adapter
        }
        getVideoPath()
        return binding.root
    }
    private fun getVideoPath() {
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        if (isSDPresent) {
            val columns =
                arrayOf(MediaStore.Video.VideoColumns.DATA, MediaStore.Video.Media.DISPLAY_NAME)
            val orderBy = MediaStore.Video.Media.DATE_TAKEN + " DESC"
            val cursor = requireActivity().contentResolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
            )
            val count = cursor!!.count
            for (i in 0 until count) {
                cursor!!.moveToPosition(i)
                val dataColumnIndex = cursor!!.getColumnIndex(MediaStore.Video.VideoColumns.DATA)
                if (list != null) {
                    list.add(cursor!!.getString(dataColumnIndex))
                }
            }
            adapter.notifyDataSetChanged()
            cursor!!.close()
        }
    }
}
