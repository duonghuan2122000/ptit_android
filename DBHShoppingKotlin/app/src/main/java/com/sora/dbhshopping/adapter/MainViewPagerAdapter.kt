package com.sora.dbhshopping.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sora.dbhshopping.fragment.HomeFragment
import com.sora.dbhshopping.fragment.UserFragment
import javax.inject.Inject

class MainViewPagerAdapter @Inject constructor(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment
        = when(position){
            0 -> HomeFragment()
            1 -> UserFragment()
            else -> HomeFragment()
        }
}