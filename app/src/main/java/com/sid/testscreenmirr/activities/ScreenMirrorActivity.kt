package com.sid.testscreenmirr.activities

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager.*
import com.sid.testscreenmirr.helper.SharePreference
import com.sid.testscreenmirr.fragment.SecondfFragment
import com.sid.testscreenmirr.fragment.ThirdFragment
import com.sid.testscreenmirr.databinding.ActivityScreenMirrorBinding
import com.sid.testscreenmirr.fragment.FirstFragment

class ScreenMirrorActivity : AppCompatActivity() {
    lateinit var binding: ActivityScreenMirrorBinding
    lateinit var sharedPreferences: SharePreference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityScreenMirrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences= SharePreference(this)
       binding.apply {
           val adapter2 = ViewPagerAdapter2(supportFragmentManager)
           adapter2.addFragment(FirstFragment(), "firstpage")
           adapter2.addFragment(SecondfFragment(), "SecondPage")
           adapter2.addFragment(ThirdFragment(), "ThirdPage")
           viewpager.setAdapter(adapter2)
           dotsIndicator.setViewPager(viewpager)
           viewpager.setOnPageChangeListener(object : OnPageChangeListener {
               override fun onPageScrolled(
                   position: Int,
                   positionOffset: Float,
                   positionOffsetPixels: Int
               ) {
                   if (position == 0) {
//                       checkbox.visibility = GONE
//                       tvStart.visibility = GONE
                   } else if (position == 1) {
//                       checkbox.visibility = GONE
//                       tvStart.visibility = GONE
                   } else if (position == 2) {
//                       checkbox.visibility= VISIBLE
                       tvStart.visibility= VISIBLE
                   }
               }
               override fun onPageSelected(position: Int) {
               }
               override fun onPageScrollStateChanged(state: Int) {
               }
           })
           tvStart.setOnClickListener {

//        sharedPreferences.setEnabledBoolean(checkbox.isChecked)

              enablingWiFiDisplay(this@ScreenMirrorActivity)
               finish()
           }
           imgBackArrow.setOnClickListener {
               onBackPressed()
           }
//           skipButn.setOnClickListener {
//               enablingWiFiDisplay(this@ScreenMirrorActivity)
//               finish()
//           }
       }
    }
    internal class ViewPagerAdapter2(supportFragmentManager: FragmentManager?) :
        FragmentPagerAdapter(supportFragmentManager!!) {
        private val fragmentList: MutableList<Fragment> = ArrayList()
        private val mtitlelist: MutableList<String> = ArrayList()
        override fun getItem(i: Int): Fragment {
            return fragmentList[i]
        }
        override fun getCount(): Int {
            return fragmentList.size
        }
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            mtitlelist.add(title)
        }
        override fun getPageTitle(position: Int): CharSequence? {
            return mtitlelist[position]
        }
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
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

}