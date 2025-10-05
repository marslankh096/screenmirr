package com.sid.testscreenmirr.activities

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.VideoController.VideoLifecycleCallbacks
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView
import com.sid.testscreenmirr.*
import com.sid.testscreenmirr.ads.IntertitialAdController
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback
import com.sid.testscreenmirr.databinding.ActivityMainBinding
import com.sid.testscreenmirr.helper.SharePreference
import com.sid.testscreenmirr.webcast.ui.activity.AppActivity

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
     var result_act_native_ad: NativeAd?=null
    lateinit var sharePreference: SharePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreference= SharePreference(this)
        loading_native_ad()
        binding.apply {
           addContainer.imgArrowGo.setOnClickListener {
              val intent=Intent(this@MainActivity, ScreenMirrorActivity::class.java)
               startActivity(intent)
           }
            addContainer.clMid.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@MainActivity,object :ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        openScreenMirrorActivity()
                    }

                })

//                if(sharePreference.booleanVAlue){
//                    enablingWiFiDisplay(this@MainActivity)                }
//                else{
//                    addContainer.castingImg.setClickable(false)
//                    addContainer.castingImg.setEnabled(false)

//                }

            }

            addContainer.castingImg.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@MainActivity,object :ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        openScreenMirrorActivity()
                    }

                })
//                if(sharePreference.booleanVAlue){
//                    enablingWiFiDisplay(this@MainActivity)                }
//                else{
//                    addContainer.castingImg.setClickable(false)
//                    addContainer.castingImg.setEnabled(false)

//                }
            }
            addContainer.clImages.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@MainActivity,object :ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        val intent = Intent(this@MainActivity, ImagesMediaActivity::class.java)
                        intent.putExtra("fileType", "Images")
                        startActivity(intent)
                    }
                })
            }
            addContainer.clVideo.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@MainActivity,object:ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        val intent = Intent(this@MainActivity, ImagesMediaActivity::class.java)
                        intent.putExtra("fileType", "Videos")
                        startActivity(intent)
                    }
                })
        }
            addContainer.clAudio.setOnClickListener {
//                disableButtons()
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(
                    this@MainActivity,
                    object : ShowAdmobIntertialCallback {
                        override fun onAdClosedCallBack() {
//                            enableButtons()
                            val intent = Intent(this@MainActivity, ImagesMediaActivity::class.java)
                            intent.putExtra("fileType", "Audios")
                            startActivity(intent)
                        }
                    })
            }
                addContainer.imgMenu.setOnClickListener {
                    drawerlayout2.openDrawer(GravityCompat.START)
                }
                sideMenu.liShareAp.setOnClickListener {
                    shareApp()
                }
                sideMenu.liRateus.setOnClickListener {
                    openExitAppDialogue()
                }
               sideMenu.liHowToUse.setOnClickListener {
                   binding.drawerlayout2.closeDrawer(GravityCompat.START)
                    val intent=Intent(this@MainActivity,ScreenMirrorActivity::class.java)
                    startActivity(intent)
                }
              sideMenu.liMoreApps.setOnClickListener {
                  binding.drawerlayout2.closeDrawer(GravityCompat.START)
                  startActivity(
                      Intent(
                          Intent.ACTION_VIEW,
                          Uri.parse("https://play.google.com/store/apps/developer?id=app+tech+master")
                      )
                  )
                }
                sideMenu.liPrivacyPolicy.setOnClickListener {
                    binding.drawerlayout2.closeDrawer(GravityCompat.START)
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://apptechmaster567.blogspot.com/2022/11/privacy-policy.html")
                        )
                    )


        }

            addContainer.clBrowser.setOnClickListener {
                IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@MainActivity,object :ShowAdmobIntertialCallback{
                    override fun onAdClosedCallBack() {
                        val intent=Intent(this@MainActivity, AppActivity::class.java)
                        startActivity(intent)
                    }
                })
            }
    }
 }

        fun shareApp() {
            binding.drawerlayout2.closeDrawer(GravityCompat.START)
            val appPackageName = BuildConfig.APPLICATION_ID
            val appName = this.getString(R.string.app_name)
            val shareBodyText = "https://play.google.com/store/apps/details?id=$appPackageName"
            val sendIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TITLE, appName)
                putExtra(Intent.EXTRA_TEXT, shareBodyText)
            }
          startActivity(Intent.createChooser(sendIntent, null))
    }
    private fun openExitAppDialogue() {
            binding.drawerlayout2.closeDrawer(GravityCompat.START)
            val dialog = Dialog(this@MainActivity)
            dialog.setContentView(R.layout.exit_dialogue_layouit)
            val dBYes = dialog.findViewById<View>(R.id.exit) as Button
            val dBNo = dialog.findViewById<View>(R.id.rate_us) as Button
            val stars = dialog.findViewById<View>(R.id.img_star) as RatingBar
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val window = dialog.window
            val wlp = window!!.attributes
            wlp.gravity = Gravity.BOTTOM
            window!!.attributes = wlp
            dialog.create()
            dialog.show()
            dBNo.setOnClickListener {
                dialog.dismiss() }
            dBYes.setOnClickListener {
                dialog.dismiss()
                finishAffinity()
            }
            stars.onRatingBarChangeListener =
                OnRatingBarChangeListener { ratingBar: RatingBar?, v: Float, b: Boolean ->
                    if (v < 3 && v > 0) {
                        shareTextToEmail(
                            this@MainActivity,
                            arrayOf("uzeegar@gmail.com"),
                            "Screen Mirror App Feedback",
                            ""
                        )
                        dialog.dismiss()
                        dialog.dismiss()
                    } else {
                        val appPackageNameCl = packageName
                        try {
                            dialog.dismiss()
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW, Uri
                                        .parse("market://details?id=$appPackageNameCl")
                                )
                            )
                        } catch (anfe: ActivityNotFoundException) {
                            dialog.dismiss()
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=$appPackageNameCl")

                                )
                            )
                        }
                    }
                }
        }
    fun shareTextToEmail(
        context: Context,
        email: Array<String?>?,
        subject: String?,
        text: String?
    ) {
        binding.drawerlayout2.closeDrawer(GravityCompat.START)
        val selectorIntent = Intent(Intent.ACTION_SENDTO)
        selectorIntent.data = Uri.parse("mailto:")
        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email)
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, text)
        emailIntent.selector = selectorIntent
        try {
            context.startActivity(
                Intent.createChooser(
                    emailIntent,
                    context.getString(R.string.EmailAddress)
                )
            )
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No apps found for this action", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loading_native_ad() {
        if (result_act_native_ad == null) {
            val ad_builder = AdLoader.Builder(this, resources.getString(R.string.native_id))
            // OnLoadedListener implementation.
            ad_builder.forNativeAd { nativeAd: NativeAd ->
                val native_adView =
                    layoutInflater.inflate(R.layout.unified_ad_main, null) as NativeAdView
                populateNativeAdView(nativeAd, native_adView)
                binding.addContainer.framlayout.removeAllViews()
                binding.addContainer.framlayout.addView(native_adView)
                result_act_native_ad = nativeAd
            }
            val adLoader = ad_builder.withAdListener(
                object : AdListener() {
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    }

                    override fun onAdLoaded() {
                        super.onAdLoaded()
                    }
                })
                .build()
            adLoader.loadAd(AdRequest.Builder().build())
        } else {
            val native_adView =
                layoutInflater.inflate(R.layout.unified_ad_main, null) as NativeAdView
            populateNativeAdView(result_act_native_ad!!, native_adView)
            binding.addContainer.framlayout.removeAllViews()
            binding.addContainer.framlayout.addView(native_adView)
        }
    }
    fun openScreenMirrorActivity() {
        val intent = Intent(this, ScreenMirrorActivity::class.java)
        startActivity(intent)
    }

    private fun populateNativeAdView(my_native: NativeAd, native_adView: NativeAdView) {
        native_adView.setMediaView(native_adView.findViewById(R.id.media_ad))
        native_adView.setHeadlineView(native_adView.findViewById(R.id.headline_ad))
        native_adView.setBodyView(native_adView.findViewById(R.id.body_ad))
        native_adView.callToActionView = native_adView.findViewById(R.id.call_to_action_ad)
        native_adView.setIconView(native_adView.findViewById(R.id.app_icon_ad))
        native_adView.setPriceView(native_adView.findViewById(R.id.price_ad))
        native_adView.setStarRatingView(native_adView.findViewById(R.id.stars_ad))
        native_adView.setStoreView(native_adView.findViewById(R.id.store_ad))
        native_adView.setAdvertiserView(native_adView.findViewById(R.id.advertiser_ad))
        (native_adView.headlineView as TextView).text = my_native.headline
        native_adView.mediaView?.setMediaContent(my_native.mediaContent)
        if (my_native.body == null) {
            native_adView.bodyView?.visibility = View.INVISIBLE
        } else {
            native_adView.bodyView?.visibility = View.VISIBLE
            (native_adView.bodyView as TextView).text = my_native.body
        }
        if (my_native.callToAction == null) {
            native_adView.callToActionView?.visibility = View.INVISIBLE
        }
        else {
            native_adView.callToActionView?.visibility = View.VISIBLE
            (native_adView.callToActionView as Button).text = my_native.callToAction
        }
        if (my_native.icon == null) {
            native_adView.iconView?.visibility = View.GONE
        } else {
            (native_adView.iconView as ImageView).setImageDrawable(
                my_native.icon?.drawable
            )
            native_adView.iconView?.visibility = View.VISIBLE
        }
        if (my_native.price == null) {
            native_adView.priceView?.visibility = View.INVISIBLE
        } else {
            native_adView.priceView?.visibility = View.GONE
            (native_adView.priceView as TextView).text = my_native.price
        }
        if (my_native.store == null) {
            native_adView.storeView?.visibility = View.INVISIBLE
        } else {
            native_adView.storeView?.visibility = View.GONE
            (native_adView.storeView as TextView).text = my_native.store
        }
        if (my_native.starRating == null) {
            native_adView.starRatingView?.visibility = View.INVISIBLE
        } else {
            (native_adView.starRatingView as RatingBar).rating = my_native.starRating!!.toFloat()
            native_adView.starRatingView?.visibility = View.VISIBLE
        }
        if (my_native.advertiser == null) {
            native_adView.advertiserView?.visibility = View.INVISIBLE
        } else {
            (native_adView.advertiserView as TextView).text = my_native.advertiser
            native_adView.advertiserView?.visibility = View.GONE
        }
        native_adView.setNativeAd(my_native)
        val videoController = my_native.mediaContent?.videoController
        if (videoController!!.hasVideoContent()) {
            videoController.setVideoLifecycleCallbacks(object : VideoLifecycleCallbacks() {
                override fun onVideoEnd() {
                    super.onVideoEnd()
                }
            })
        } else {
        }
    }
    override fun onBackPressed() {
        openExitAppDialogue()
    }

    fun enablingWiFiDisplay(context: Context?) {
        if (isNetworkAvailable(context!!)) {
            try {
                startActivity(Intent("android.settings.WIFI_DISPLAY_SETTINGS"))
                return
            } catch (activitynotfoundexception: ActivityNotFoundException) {
                activitynotfoundexception.printStackTrace()
            }
            try {
                startActivity(Intent("android.settings.CAST_SETTINGS"))
                return
            } catch (exception1: Exception) {
                Toast.makeText(applicationContext, "Device not supported", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Connect to WIFI first!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork
            if (nw == null) {
                false
            } else {
                val capabilities = connectivityManager.getNetworkCapabilities(nw)
                if (capabilities!!.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    true
                } else false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo
            nwInfo?.isConnected ?: false
        }
    }
    }

