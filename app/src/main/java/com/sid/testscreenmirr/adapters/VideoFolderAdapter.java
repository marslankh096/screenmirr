package com.sid.testscreenmirr.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.ads.IntertitialAdController;
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback;
import com.sid.testscreenmirr.interfaces.itemClickListener;
import com.sid.testscreenmirr.modelclasses.ImagesFolderItems;

import java.util.ArrayList;

public class VideoFolderAdapter extends RecyclerView.Adapter<VideoFolderAdapter.FolderHolder>{
    private ArrayList<ImagesFolderItems> folders;
    private Activity folderContx;
    private String fileType;
    private itemClickListener listenToClick;
    public VideoFolderAdapter(ArrayList<ImagesFolderItems> folders, Activity folderContx, itemClickListener listen,String fileType) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
        this.fileType = fileType;
    }
    @NonNull
    @Override
    public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new FolderHolder(cell);
    }
    @Override
    public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
        final ImagesFolderItems folder = folders.get(position);
        if(!fileType.equals("Audios")){
            Glide.with(folderContx)
                    .load(folder.getFirstPic())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.folderPic);
        }
        else {
            Glide.with(folderContx)
                    .load(R.drawable.ic_music_icon)
                    .into(holder.folderPic);
        }
        String text = ""+folder.getFolderName();
        String folderSizeString=""+folder.getNumberOfPics()+" Media";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);
        holder.folderPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileType.equals("Audios")){
                    listenToClick.onPicClicked(folder.getPath(),folder.getFolderName());
                }else {
                    IntertitialAdController.getInstance().showAdmobInterstitialFullApp(folderContx, new ShowAdmobIntertialCallback() {
                        @Override
                        public void onAdClosedCallBack() {
                            listenToClick.onPicClicked(folder.getPath(),folder.getFolderName());

                        }
                    });
                }


            }
        });

    }

    @Override
    public int getItemCount() {
        return folders.size();
    }


    public class FolderHolder extends RecyclerView.ViewHolder{
        ImageView folderPic;
        TextView folderName;
        TextView folderSize;

        CardView folderCard;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize=itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
        }
    }

}