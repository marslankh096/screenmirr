package com.sid.testscreenmirr.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.activities.ImagesMediaActivity
import com.sid.testscreenmirr.databinding.GalleryItemViewBinding
import java.io.File

class ImagesRecycleAapter(val context: Context, var list: ArrayList<String?>): RecyclerView.Adapter<ImagesRecycleAapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = GalleryItemViewBinding
            .inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imgFile: File = File(list.get(position))
        if (imgFile.exists()) {
            Glide.with(context).load(imgFile).placeholder(R.drawable.ic_launcher_background)
                .into(holder.binding.image)
//            holder.binding.fileName.setText(imgFile.name)
            holder.itemView.setOnClickListener {
//                MyInterstitialControllerUsman.getInstance()
//                    .showAdmobInterstitialFullApp(context, object : ShowAdmobIntertialCallback() {
//                        fun onAdClosedCallBack() {
                            val i = Intent(context, ImagesMediaActivity::class.java)
                            i.putExtra("imgPath", list.get(position))
                            i.putExtra("isVideo", false)
                            context.startActivity(i)
//                        }
//                    })
//            }
        }
    }
    }
    override fun getItemCount(): Int {
        return list.size
    }
    inner class ViewHolder(val binding: GalleryItemViewBinding):RecyclerView.ViewHolder(binding.root){
    }
}