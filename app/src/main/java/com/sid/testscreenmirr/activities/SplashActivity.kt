package com.sid.testscreenmirr.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sid.testscreenmirr.ads.IntertitialAdController
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback
import com.sid.testscreenmirr.databinding.ActivitySplashBinding
import com.sid.testscreenmirr.interfaces.LoadAdmobIntertialCallback


class SplashActivity : AppCompatActivity() {

    lateinit var binding:ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        IntertitialAdController.getInstance().initAdMob(this)

        binding.apply {
            IntertitialAdController.getInstance()
                .loadAdmobInterstitialFullApp(this@SplashActivity, LoadAdmobIntertialCallback {
                        butnGetStarted.visibility=View.VISIBLE
                        lottieAnim.visibility=View.GONE
                })
            butnGetStarted.setOnClickListener {
                IntertitialAdController.getInstance()
                    .showAdmobInterstitialFullApp(this@SplashActivity, object :
                        ShowAdmobIntertialCallback {
                        override
                        fun onAdClosedCallBack() {
                            val intent=Intent(this@SplashActivity,MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    })

            }
        }
    }
}