package com.sid.testscreenmirr.ads;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.sid.testscreenmirr.R;
import com.sid.testscreenmirr.interfaces.LoadAdmobIntertialCallback;

public class IntertitialAdController {
    private static IntertitialAdController interstitialController;
    private InterstitialAd admobInterstitialAd;
    boolean checkShowAd=true;
    //    private ProgressDialog progressDialog;
    Dialog progressDialog1;
    public static IntertitialAdController getInstance() {
        if (interstitialController == null) {
            interstitialController = new IntertitialAdController();
        }
        return interstitialController;
    }
    public void initAdMob(Context context) {
        MobileAds.initialize(context);
    }
    public void loadAdmobInterstitialFullApp(Context mContext, LoadAdmobIntertialCallback loadAdmobIntertialCallback) {
        if (admobInterstitialAd == null && isNetworkAvailable(mContext)) {
//            Toast.makeText(mContext, "Request for FullApp IntertialAd", Toast.LENGTH_SHORT).show();

            AdRequest adRequest = new AdRequest.Builder().build();

            InterstitialAd.load(mContext, mContext.getString(R.string.interstitail_id), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    super.onAdLoaded(interstitialAd);
                    admobInterstitialAd = interstitialAd;
                    loadAdmobIntertialCallback.adLoadedOrFailed();
                    Log.d("UsmanTAG", "onAdLoaded");

                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    admobInterstitialAd = null;
                    loadAdmobIntertialCallback.adLoadedOrFailed();
                    Log.d("UsmanTAG", "onAdFailedToLoad: " + loadAdError);

                }
            });

        }
        else {

            loadAdmobIntertialCallback.adLoadedOrFailed();

        }

    }

    public void showAdmobInterstitialFullApp(Activity activity, ShowAdmobIntertialCallback admobIntertialCallback) {

        if ( checkShowAd==true) {
            checkShowAd = false;
            if (admobInterstitialAd != null) {

//Ad Loading Dialog
                showProgress(activity);

                admobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        admobInterstitialAd = null;
                        admobIntertialCallback.onAdClosedCallBack();
                        Log.d("UsmanTAG", "onAdDismissedFullScreenContent");


                        IntertitialAdController.getInstance().loadAdmobInterstitialFullApp(activity, new LoadAdmobIntertialCallback() {
                            @Override
                            public void adLoadedOrFailed() {
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        admobIntertialCallback.onAdClosedCallBack();
                        Log.d("UsmanTAG", "onAdFailedToShowFullScreenContent");


                    }

                    @Override
                    public void onAdImpression() {
                        super.onAdImpression();
                        Log.d("UsmanTAG", "onAdImpression");

                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        Log.d("UsmanTAG", "onAdShowedFullScreenContent");

                    }

                });

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            if (progressDialog1 != null) {
                                progressDialog1.dismiss();
                            }
                            Log.d("TAG", "InterstitialManager: show interstitial");
                            admobInterstitialAd.show(activity);
                        } catch (Exception e) {

                            admobIntertialCallback.onAdClosedCallBack();
                        }

                    }
                }, 3000);

            } else {
                admobIntertialCallback.onAdClosedCallBack();
            }
        }else {
            admobIntertialCallback.onAdClosedCallBack();
            checkShowAd = true;

        }
    }


    private boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) {
                return false;
            } else {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(nw);

                if (capabilities == null) {
                    return false;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            }
        } else {

            NetworkInfo nwInfo = connectivityManager.getActiveNetworkInfo();

            if (nwInfo == null) {
                return false;
            } else {
                return nwInfo.isConnected();
            }

        }
    }

    private void showProgress(Activity activity) {
        progressDialog1 = new Dialog(activity);
        progressDialog1.setContentView(R.layout.loading_ad_layout);
        progressDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog1.setCancelable(false);
        progressDialog1.setCanceledOnTouchOutside(false);
        progressDialog1.show();
    }


}
