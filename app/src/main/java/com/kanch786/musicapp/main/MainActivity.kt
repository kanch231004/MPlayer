package com.kanch786.musicapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import kotlinx.android.synthetic.main.activity_main.*
import java.io.Serializable

class MainActivity : AppCompatActivity() {


    private lateinit var musicAdapter : MusicPagerAdapter
    private lateinit var searchSongVM: SearchSongVM
    private var songList = ArrayList<SongListResults>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        searchSongVM = ViewModelProviders.of(this).get(SearchSongVM::class.java)
        musicAdapter = MusicPagerAdapter(supportFragmentManager)
        getSongListResults()

    }

    private fun setUpViewPager( bundle: Bundle) {


        musicAdapter.clearAdapter()
        musicAdapter.addFragment(NewFragmentInstanceList.create(bundle))
        musicAdapter.addFragment(NewFragmentInstanceList.create(bundle))
        musicAdapter.addFragment(NewFragmentInstanceList.create(bundle))

        viewPager.adapter = musicAdapter
        tabLayout.setupWithViewPager(viewPager)


    }

    internal inner class MusicPagerAdapter(fm : FragmentManager ) : FragmentPagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()
       // private var titleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return fragmentList[position]
        }

        override fun getCount(): Int {

            return fragmentList.size
        }

       /* override fun getPageTitle(position: Int): CharSequence? {

            return titleList[position]
        }*/


        fun addFragment(fragment : Fragment) {

            fragmentList.add(fragment)

        }

        fun clearAdapter() {

            fragmentList.clear()
        }

       /* fun addTitleList(passedList : ArrayList<String>) {

            titleList = passedList
        }*/
    }



    private fun getSongListResults() {

        d("getSongResults called")

        searchSongVM.getSongListResults().observe(this, Observer {


            d("songs List $it")
            val bundle = Bundle()
            songList.clear()
            songList = ArrayList(it)
            bundle.putSerializable("songsList",ArrayList(songList) as Serializable )
            setUpViewPager(bundle)


        })
    }




}
