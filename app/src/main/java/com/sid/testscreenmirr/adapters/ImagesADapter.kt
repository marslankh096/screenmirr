package com.sid.testscreenmirr.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.sid.testscreenmirr.fragment.ImagesFolderFragment
import com.sid.testscreenmirr.fragment.AllImagesFragment

class ImagesADapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var fragmentAllImages=
        AllImagesFragment()
    var fragmentImageFolders=
        ImagesFolderFragment()


    private val fragments: Array<Fragment> = arrayOf(
        fragmentAllImages,
        fragmentImageFolders
    )

    override fun getPageTitle(position: Int): CharSequence? {
        var title = ""
        if (position == 0) {
            title = "All Photos"
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