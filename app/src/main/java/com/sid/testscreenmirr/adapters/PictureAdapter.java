package com.sid.testscreenmirr.adapters;

import static androidx.core.view.ViewCompat.setTransitionName;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.testscreenmirr.modelclasses.PicHolder;
import com.sid.testscreenmirr.ads.IntertitialAdController;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback;
import com.sid.testscreenmirr.interfaces.itemClickListener;
import com.sid.testscreenmirr.modelclasses.PictureFacer;
import java.util.ArrayList;

public class PictureAdapter extends RecyclerView.Adapter<PicHolder> {
    private ArrayList<PictureFacer> pictureList;
    private Activity pictureContx;
    private String fileType;
    private final itemClickListener picListerner;
    public PictureAdapter(ArrayList<PictureFacer> pictureList, Activity pictureContx, itemClickListener picListerner,String fileType) {
        this.pictureList = pictureList;
        this.pictureContx = pictureContx;
        this.picListerner = picListerner;
        this.fileType = fileType;
    }
    @NonNull
    @Override
    public PicHolder onCreateViewHolder(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View cell;
        if(fileType.equals("Audios")) {
            cell = inflater.inflate(R.layout.audio_item_view, container, false);
        }
        else {
            cell = inflater.inflate(R.layout.pic_holder_item, container, false);
        }
        return new PicHolder(cell);
    }
    @Override
    public void onBindViewHolder(@NonNull final PicHolder holder, @SuppressLint("RecyclerView") final int position) {
        final PictureFacer image = pictureList.get(position);
        String filename=image.getPicturePath().substring(image.getPicturePath().lastIndexOf("/")+1);
        holder.fileName.setText(""+filename);
        if(fileType.equals("Audios")){
            Glide.with(pictureContx)
                    .load(R.drawable.ic_music_icon)
                    .into(holder.picture);
        } else if(fileType.equals("Videos")){
            holder.startVideo.setVisibility(View.VISIBLE);
            Glide.with(pictureContx)
                    .load(image.getPicturePath())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.picture);
        }
        else {
//            holder.startVideo.setVisibility(View.GONE);
            Glide.with(pictureContx)
                    .load(image.getPicturePath())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.picture);
        }
        setTransitionName(holder.picture, String.valueOf(position) + "_image");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileType.equals("Audios")){
                    picListerner.onPicClicked(holder,position, pictureList);
                }
                else {
                    IntertitialAdController.getInstance().showAdmobInterstitialFullApp(pictureContx, new ShowAdmobIntertialCallback() {
                        @Override
                        public void onAdClosedCallBack() {
                            picListerner.onPicClicked(holder,position, pictureList);
                        }
                    });
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return pictureList.size();
    }
}
