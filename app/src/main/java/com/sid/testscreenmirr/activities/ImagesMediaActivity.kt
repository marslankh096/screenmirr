package com.sid.testscreenmirr.activities

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sid.testscreenmirr.helper.SharePreference
import com.sid.testscreenmirr.adapters.VideoAdapter
import com.sid.testscreenmirr.adapters.AudioAdapter
import com.sid.testscreenmirr.adapters.ImagesADapter
import com.sid.testscreenmirr.ads.IntertitialAdController
import com.sid.testscreenmirr.ads.ShowAdmobIntertialCallback
import com.sid.testscreenmirr.databinding.ActivityImagesMediaBinding

class ImagesMediaActivity : AppCompatActivity() {
    lateinit var binding: ActivityImagesMediaBinding
    private val PERMISSION_REQUEST_CODE = 200
    lateinit var sharePreference: SharePreference
    var permissionCounter = 0
    var getfileType = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityImagesMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharePreference= SharePreference(this)
        getfileType= intent.getStringExtra("fileType").toString()
         binding.apply {
             tvTitle.text=""+getfileType
             castBtn.setOnClickListener {
                 IntertitialAdController.getInstance().showAdmobInterstitialFullApp(this@ImagesMediaActivity,object :ShowAdmobIntertialCallback{
                     override fun onAdClosedCallBack() {
                         openScreenMirrorActivity()
                     }

                 })
//                 if(sharePreference.booleanVAlue){
//                     enablingWiFiDisplay(this@ImagesMediaActivity)
//                 }else {
//                     castBtn!!.setClickable(false)
//                     castBtn!!.setEnabled(false)
//                     openScreenMirrorActivity()
//                 }
             }
             backBtn.setOnClickListener {
                 onBackPressed()
             }

         }
        binding.askPermissionAgain!!.setOnClickListener{ requestPermission() }
        requestPermissions()
    }
    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestPermissions() {
        if (checkPermission()) {
            setUpViewPager()
        } else {
            requestPermission()
        }
    }
    private fun setUpViewPager() {
        if(getfileType.equals("Images")){
            binding.viewpagerPictuires.adapter = ImagesADapter(supportFragmentManager)
        }
        else if(getfileType.equals("Videos")){
           binding.viewpagerPictuires.adapter = VideoAdapter(supportFragmentManager)
        }
        else{
            binding.viewpagerPictuires.adapter = AudioAdapter(supportFragmentManager)
        }
        binding.viewpagerPictuires.currentItem = 0
        binding.tabPictures.setupWithViewPager(binding.viewpagerPictuires)
    }
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            PERMISSION_REQUEST_CODE
        )
    }
    override fun onResume() {
        super.onResume()
        binding.castBtn!!.isClickable = true
       binding.castBtn!!.isEnabled = true
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val storageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                if (storageAccepted) {
                    binding.noPermissionContainer!!.visibility = View.GONE

                    setUpViewPager()
                } else {
                    if (permissionCounter >= 2) {
                        Toast.makeText(
                            this,
                            "Turn On required permissions from settings to use this feature!",
                            Toast.LENGTH_LONG
                        ).show()
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                        val uri = Uri.fromParts("package", packageName, null)
                        intent.data = uri
                        startActivityForResult(intent, 999)
                    } else {
                        permissionCounter++
                        binding.noPermissionContainer!!.visibility = View.VISIBLE
                        Toast.makeText(
                            this,
                            "Permissions denined, Permissions are required to use the app..",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }
    fun openScreenMirrorActivity() {
        val intent = Intent(this@ImagesMediaActivity, ScreenMirrorActivity::class.java)
        startActivity(intent)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
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