package com.sid.testscreenmirr.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.activities.VideoPlayActivity
import com.sid.testscreenmirr.ads.IntertitialAdController
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback
import com.sid.testscreenmirr.databinding.VideoItemsLayoutBinding
import java.io.File

class GalleryVideoAdapter(val context: Activity, var list: ArrayList<String?>): RecyclerView.Adapter<GalleryVideoAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
       val binding=VideoItemsLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val videoFile: File = File(list.get(position))
        if (videoFile.exists()) {
            Glide.with(context).load(videoFile).placeholder(R.drawable.ic_launcher_background)
                .into(holder.binding.image)
            holder.binding.fileName.setText(videoFile.name)
            holder.itemView.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(context,object:ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        val i = Intent(context, VideoPlayActivity::class.java)
                        i.putExtra("imgPath", list.get(position))
                        i.putExtra("isVideo", true)
                        context.startActivity(i)
                    }
                })
            }
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(val binding: VideoItemsLayoutBinding):RecyclerView.ViewHolder(binding.root)

}