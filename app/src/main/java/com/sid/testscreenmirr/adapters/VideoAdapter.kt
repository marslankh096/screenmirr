package com.sid.testscreenmirr.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sid.testscreenmirr.fragment.FragmentVideoFolders
import com.sid.testscreenmirr.fragment.AllVideoFragment

class VideoAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentAllVideos=
        AllVideoFragment()
    var fragmentVideoFolders=
       FragmentVideoFolders()
    private val fragments: Array<Fragment> = arrayOf(
        fragmentAllVideos,
        fragmentVideoFolders
    )
    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        if (position == 0) {
            title = "All Videos"
        } else if (position == 1) {
            title = "Albums"
        }
        return title
    }
    override fun getItem(i: Int): Fragment {
        return fragments[i]
    }
    override fun getCount(): Int {
        return fragments.size
    }
}