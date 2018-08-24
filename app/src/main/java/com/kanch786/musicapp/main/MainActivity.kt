package com.kanch786.musicapp.main

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Point
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.util.DisplayMetrics
import android.view.ViewGroup
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.extensions.getHeightInDp
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_songs_list.*
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

    private fun setNumberOfResultsPerScreen() {

        val displayMetrics = resources.displayMetrics
        val screenSize = displayMetrics.heightPixels /displayMetrics.density

        val toolbarheight = toolbar.getHeightInDp(displayMetrics)
        val etSearchHeight = etSearchSong.getHeightInDp(displayMetrics)
        val tvCountHeight = tvCount.getHeightInDp(displayMetrics)

        //offset margin 30 given in xml (sum of all the margins)
        //calculation viewpager available height and dividing with each card size give the no of results which can be accomodated in a screen
        val viewPagerHeight = (screenSize-(toolbarheight+etSearchHeight+tvCountHeight+(resources.getDimension(R.dimen.tabLayoutHeight)/ displayMetrics.density)+30))


        val noOfResult : Int =  ((viewPagerHeight / (resources.getDimension(R.dimen.cvSongsHeightWithMargin)/displayMetrics.density)).toInt())
       setUpViewPager(noOfResult)
    }

    private fun setUpViewPager(noOfResultPerScreen : Int) {

        d("noOfResult $noOfResultPerScreen")

        musicAdapter.clearAdapter()
        val bundle = Bundle()
        val isPerfectDivisible = songList.size % noOfResultPerScreen == 0
        val noOfFragments = if (isPerfectDivisible) songList.size / noOfResultPerScreen else (songList.size / noOfResultPerScreen) +1
        var startIndex = 0
        var endIndex = 0

        for (i in 0 until noOfFragments) {

            startIndex = endIndex
            endIndex = Math.min(songList.size , (endIndex + noOfResultPerScreen))

            musicAdapter.addFragment(NewFragmentInstanceList.create(ArrayList(songList.subList(startIndex,endIndex))))
        }


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

            setNumberOfResultsPerScreen()



        })
    }

}
