package com.sid.testscreenmirr.interfaces;
import com.sid.testscreenmirr.modelclasses.PicHolder;
import com.sid.testscreenmirr.modelclasses.PictureFacer;
import java.util.ArrayList;

public interface itemClickListener {
    void onPicClicked(PicHolder holder, int position, ArrayList<PictureFacer> pics);
    void onPicClicked(String pictureFolderPath,String folderName);
}
