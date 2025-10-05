package com.sid.testscreenmirr.modelclasses;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sid.testscreenmirr.R;


public class PicHolder extends RecyclerView.ViewHolder{

    public ImageView picture,startVideo;
    public TextView fileName;

    public PicHolder(@NonNull View itemView) {
        super(itemView);

        picture = itemView.findViewById(R.id.image);
        startVideo = itemView.findViewById(R.id.startVideo);
        fileName = itemView.findViewById(R.id.fileName);
    }
}
