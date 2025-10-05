package com.sid.testscreenmirr.fragment

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.sid.testscreenmirr.ads.BannerAd
import com.sid.testscreenmirr.adapters.AllAudioAdapter
import com.sid.testscreenmirr.databinding.FragmentAllAudioBinding

class AllAudioFragment : Fragment() {
     lateinit var binding: FragmentAllAudioBinding
     var list=ArrayList<String?>()
    lateinit var adapter: AllAudioAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
         binding=FragmentAllAudioBinding.inflate(inflater,container,false)
        binding.apply {
            audioRv.layoutManager=LinearLayoutManager(requireContext())
            adapter= AllAudioAdapter(requireContext(),list)
            audioRv.adapter=adapter
            BannerAd(requireActivity())
                .loadAdaptiveBanner(bannerAdId, bannerText)
        }
        getAudioPAth()
        return binding.root
    }
    private fun getAudioPAth() {
        val isSDPresent = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
        if (isSDPresent) {
            val columns =
                arrayOf(MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.Media.DISPLAY_NAME)
            val orderBy = MediaStore.Audio.Media.DEFAULT_SORT_ORDER
            val cursor = requireActivity().contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                orderBy
            )
            val count = cursor!!.count
            for (i in 0 until count) {
                cursor.moveToPosition(i)
                val dataColumnIndex = cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DATA)
                if (list != null) {
                    list.add(cursor.getString(dataColumnIndex))
                }
            }
            adapter.notifyDataSetChanged()
            cursor.close()
        }
    }
}
