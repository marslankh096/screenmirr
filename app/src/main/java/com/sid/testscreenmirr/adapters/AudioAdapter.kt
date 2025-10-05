package com.sid.testscreenmirr.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sid.testscreenmirr.fragment.AudioFolderFragment
import com.sid.testscreenmirr.fragment.AllAudioFragment

class AudioAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentAllAudios=
        AllAudioFragment()
    var AudioFolderFragment=
        AudioFolderFragment()
    private val fragments: Array<Fragment> = arrayOf(
        fragmentAllAudios,
        AudioFolderFragment
    )
    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        if (position == 0) {
            title = "All"
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