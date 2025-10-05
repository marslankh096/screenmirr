package com.sid.testscreenmirr.adapters

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sid.testscreenmirr.activities.AudioPlayerActivity
import com.sid.testscreenmirr.R
import com.sid.testscreenmirr.databinding.AudioItemsLayoutBinding
import java.io.File
import java.text.SimpleDateFormat

import java.util.*

class AllAudioAdapter(val context: Context, var list: ArrayList<String?>): RecyclerView.Adapter<AllAudioAdapter.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllAudioAdapter.ViewHolder {
       val binding=AudioItemsLayoutBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: AllAudioAdapter.ViewHolder, position: Int) {
        val audioFile: File = File(list.get(position))
        if (audioFile.exists()) {
            holder.binding.fileName.setText(audioFile.name)
//            val st:String=""+audioFile.length()%1048576
//            holder.binding.fileSize.setText(st+" MB")

//            val ratingValue = 52.98929821f
//            val decimalFormat = DecimalFormat("#.##")
//            val twoDigitsFR: Float = java.lang.Float.valueOf(decimalFormat.format(ratingValue))

            holder.binding.fileSize.setText(getFolderSizeLabel(audioFile))

            val mp = MediaPlayer.create(context, Uri.parse(list.get(position)))
         try {
             val date: Date = Date(mp.duration.toLong())
             val formatter = SimpleDateFormat("HH:mm:ss")
             formatter.timeZone = TimeZone.getTimeZone("UTC")
             val fileLenght = formatter.format(date)
             holder.binding.fileDuration.text = fileLenght
         }catch (e:Exception){
             e.printStackTrace()
         }

            val ext = audioFile.name.substring(audioFile.name.lastIndexOf("."))
            if (ext == ".mp3") {
                holder.binding.image.setImageResource(R.drawable.ic_music_icon)
            } else if (ext == ".wav" || ext == ".aud" || ext == ".opus") {
                holder.binding.image.setImageResource(R.drawable.ic_music_icon)
            } else {
                holder.binding.image.setImageResource(R.drawable.ic_music_icon)
            }
            holder.itemView.setOnClickListener {
                val i = Intent(context, AudioPlayerActivity::class.java)
                i.putExtra("imgPath", list.get(position))
                context.startActivity(i)
            }
        }
    }
    override fun getItemCount(): Int {
       return list.size
    }
    inner class ViewHolder(val binding:AudioItemsLayoutBinding):RecyclerView.ViewHolder(binding.root)
    fun getFolderSizeLabel(file: File): String? {
        val size = getFolderSize(file).toDouble() / 1000.0 // Get size and convert bytes into KB.
        return if (size >= 1024) {
            (size / 1024).toString() + " MB"
        } else {
            "$size KB"
        }
    }
    fun getFolderSize(file: File): Long {
        var size: Long = 0
        if (file.isDirectory) {
            for (child in file.listFiles()) {
                size += getFolderSize(child)
            }
        } else {
            size = file.length()
        }
        return size
    }


}