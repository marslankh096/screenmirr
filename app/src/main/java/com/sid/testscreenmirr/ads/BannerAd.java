package com.sid.testscreenmirr.ads;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.ads.MyPrefHelper;


import org.jetbrains.annotations.NotNull;
public class BannerAd {
    private final Context context;
    private AdView adView;
    public BannerAd(Context appCompatActivity) {
        context = appCompatActivity;
    }
    public void loadAdaptiveBanner(LinearLayout adContainerView, TextView textView) {
        // Create an ad request.

        if (MyPrefHelper.getPrefIns(context).getAppPurchased()) {
            adContainerView.setVisibility(View.GONE);
            return;
        }
        adView = new AdView(context);
        adView.setAdUnitId(context.getString(R.string.banner_id));

        AdSize adSize = AdSize.BANNER;
        adView.setAdSize(adSize);
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(@NonNull @NotNull LoadAdError loadAdError) {
                adContainerView.setVisibility(View.GONE);
                textView.setText("Space reserved for ad");
            }
            @Override
            public void onAdLoaded() {
                adContainerView.removeAllViews();
                adContainerView.addView(adView);
            }
        });
    }
    private AdSize getAdSize(LinearLayout adContainerView) {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = context.getApplicationContext().getDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = outMetrics.density;
        float adWidthPixels = adContainerView.getWidth();
        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }
        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(context, adWidth);
    }
    public void destroyView() {

        if (adView != null) {
            adView.destroy();
        }

    }
}
