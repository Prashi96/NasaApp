package com.pp.nasaapp.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class HomeScreenAdapter(supportFragmentManager: FragmentManager, hasFullScreenWidth: Boolean ) : FragmentStatePagerAdapter(supportFragmentManager,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    private val mFragmentList = ArrayList<Fragment>()
    private val mFragmentTitleList = ArrayList<String>()
    private var hasFullScreenWidth:Boolean=false

    init {
        this.hasFullScreenWidth=hasFullScreenWidth
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    override fun getPageWidth(position: Int): Float {
        return if (hasFullScreenWidth) 1f else 0.85f
    }

    fun addFrag(fragment: Fragment?) {
        mFragmentList.add(fragment!!)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList[position]
    }
}