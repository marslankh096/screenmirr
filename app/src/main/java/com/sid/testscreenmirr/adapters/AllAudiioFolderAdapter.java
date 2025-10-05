package com.sid.testscreenmirr.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sid.testscreenmirr.modelclasses.ImagesFolderItems;
import com.sid.testscreenmirr.ads.IntertitialAdController;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback;
import com.sid.testscreenmirr.interfaces.itemClickListener;

import java.util.ArrayList;

public class AllAudiioFolderAdapter extends RecyclerView.Adapter<AllAudiioFolderAdapter.Vieholder> {

    private ArrayList<ImagesFolderItems> folders;
    private Activity folderContx;
    private String fileType;
    private itemClickListener listenToClick;

    public AllAudiioFolderAdapter(ArrayList<ImagesFolderItems> folders, Activity folderContx, itemClickListener listen, String fileType) {
        this.folders = folders;
        this.folderContx = folderContx;
        this.listenToClick = listen;
        this.fileType = fileType;
    }
    @NonNull
    @Override
    public Vieholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View cell = inflater.inflate(R.layout.picture_folder_item, parent, false);
        return new Vieholder(cell);
    }
    @Override
    public void onBindViewHolder(@NonNull Vieholder holder, int position) {
        final ImagesFolderItems folder = folders.get(position);
        if (!fileType.equals("Audios")) {
            Glide.with(folderContx)
                    .load(folder.getFirstPic())
                    .apply(new RequestOptions().centerCrop())
                    .into(holder.folderPic);
        } else {
            Glide.with(folderContx)
                    .load(R.drawable.ic_music_icon)
                    .into(holder.folderPic);
        }

        String text = "" + folder.getFolderName();
        String folderSizeString = "" + folder.getNumberOfPics() + " Media";
        holder.folderSize.setText(folderSizeString);
        holder.folderName.setText(text);
        holder.folderPic.setOnClickListener(v -> {
            if (fileType.equals("Audios")) {
                listenToClick.onPicClicked(folder.getPath(), folder.getFolderName());

            } else {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(folderContx, new ShowAdmobIntertialCallback() {
                    @Override
                    public void onAdClosedCallBack() {
                listenToClick.onPicClicked(folder.getPath(), folder.getFolderName());

                    }
                });
            }


        });

    }
    @Override
    public int getItemCount() {
        return folders.size();
    }

    public class Vieholder extends RecyclerView.ViewHolder {
        RoundedImageView folderPic;
        TextView folderName;
        TextView folderSize;

        CardView folderCard;

        public Vieholder(@NonNull View itemView) {
            super(itemView);
            folderPic = itemView.findViewById(R.id.folderPic);
            folderName = itemView.findViewById(R.id.folderName);
            folderSize = itemView.findViewById(R.id.folderSize);
            folderCard = itemView.findViewById(R.id.folderCard);
        }
    }
}