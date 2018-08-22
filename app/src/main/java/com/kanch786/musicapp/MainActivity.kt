package com.kanch786.musicapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var fragmentList = listOf(HomePageFragment(), PlaySongFragment(),FavoriteListFragment())
    private lateinit var musicAdapter : MusicAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        musicAdapter = MusicAdapter(supportFragmentManager,fragmentList)
        setUpViewPager()
    }

    private fun setUpViewPager() {

        viewPager.adapter = musicAdapter
        tabLayout.setupWithViewPager(viewPager)

    }

    private class MusicAdapter(supportFM : FragmentManager, fragmentList : List<Fragment>) : FragmentStatePagerAdapter(supportFM) {



        override fun getItem(position: Int): Fragment? {

            return when (position) {

                0 -> HomePageFragment()
                1 -> PlaySongFragment()
                2 -> FavoriteListFragment()

               else -> {return null}
            }
        }

        override fun getCount() = 3

        override fun getPageTitle(position: Int): CharSequence? {
            when (position) {

                0 -> "MusicPlayer"
                1 -> "MPlayer"
                2 -> "Favourite"
            }

            return null
        }


    }

}
