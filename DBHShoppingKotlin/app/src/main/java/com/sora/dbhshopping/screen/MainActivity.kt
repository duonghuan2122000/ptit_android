package com.sora.dbhshopping.screen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sora.dbhshopping.R
import com.sora.dbhshopping.adapter.MainViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var adapter: MainViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = MainViewPagerAdapter(this)

        viewPager = findViewById(R.id.main_view_pager)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                when(position){
                    0 -> {
                        bottomNav.menu.getItem(0).isChecked = true
                    }
                    1 -> {
                        bottomNav.menu.getItem(1).isChecked = true
                    }
                }
            }
        })

        bottomNav = findViewById(R.id.main_bottom_nav)

        bottomNav.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.nav_home -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.nav_user -> {
                    viewPager.currentItem = 1
                    true
                }
                else -> true
            }
        }
    }
}