package com.sid.testscreenmirr.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.activities.ImageDisplayActivity;
import com.sid.testscreenmirr.ads.IntertitialAdController;
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback;

import java.io.File;
import java.util.ArrayList;

public class ImageRecyclerViewAdapter extends RecyclerView.Adapter<ImageRecyclerViewAdapter.RecyclerViewHolder> {
    private final Activity context;
    private final ArrayList<String> imagePathArrayList;
    public ImageRecyclerViewAdapter(Activity context, ArrayList<String> imagePathArrayList) {
        this.context = context;
        this.imagePathArrayList = imagePathArrayList;
    }
    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_item_view, parent, false);
        return new RecyclerViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, @SuppressLint("RecyclerView") int position) {
        File imgFile = new File(imagePathArrayList.get(position));
        if (imgFile.exists()) {
            Glide.with(context).load(imgFile).placeholder(R.drawable.ic_launcher_background).into(holder.imageIV);
//            holder.nameTv.setText(imgFile.getName());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    IntertitialAdController.getInstance().showAdmobInterstitialFullApp(context, new ShowAdmobIntertialCallback() {
                        @Override
                        public void onAdClosedCallBack() {
                            Intent i = new Intent(context, ImageDisplayActivity.class);
                            i.putExtra("imgPath", imagePathArrayList.get(position));
                            i.putExtra("isVideo",false);
                            context.startActivity(i);
                        }
                    });

                }
            });
        }
    }
    @Override
    public int getItemCount() {
        return imagePathArrayList.size();
    }
    public static class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageIV;
        private final TextView nameTv;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIV = itemView.findViewById(R.id.image);
            nameTv = itemView.findViewById(R.id.fileName);
        }
    }
}
