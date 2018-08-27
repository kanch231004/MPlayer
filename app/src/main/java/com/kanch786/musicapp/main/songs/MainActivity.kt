package com.kanch786.musicapp.main.songs

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import com.kanch786.musicapp.R
import com.kanch786.musicapp.api.SongListResults
import com.kanch786.musicapp.extensions.d
import com.kanch786.musicapp.extensions.getHeightInDp
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kanch786.musicapp.dataManager.MPlayerDatabase
import com.kanch786.musicapp.dataManager.repo.QueryRepository
import com.kanch786.musicapp.main.SongsViewModelFactory
import com.kanch786.musicapp.main.favorites.FavoriteListActivity
import com.kanch786.musicapp.main.query.QueryVM
import com.kanch786.musicapp.main.query.QueryViewModelFactory
import com.kanch786.musicapp.queryRepo
import com.kanch786.musicapp.songRepo
import kotlinx.android.synthetic.main.layout_songs_list.*
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView


class MainActivity : AppCompatActivity() {

    private lateinit var musicAdapter : MusicPagerAdapter
    private lateinit var songsVM: SongsVM
    private lateinit var queryVM : QueryVM
    private var songList = ArrayList<SongListResults>()
    private lateinit var query : String
    private lateinit var songsViewModelFactory: SongsViewModelFactory
    private lateinit var queryViewModelFactory: QueryViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        songsViewModelFactory = SongsViewModelFactory(songRepo)
        queryViewModelFactory = QueryViewModelFactory(queryRepo)


        songsVM = ViewModelProviders.of(this,songsViewModelFactory).get(SongsVM::class.java)
        queryVM = ViewModelProviders.of(this,queryViewModelFactory).get(QueryVM::class.java)
        musicAdapter = MusicPagerAdapter(supportFragmentManager)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        etSearchSong.isCursorVisible = true

        etSearchSong.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->


            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                progressBar.visibility = View.VISIBLE
                query = etSearchSong.text.toString().trim()
                insertQueryInSuggestionList()
                getSongListResults()
                etSearchSong.dismissDropDown()
                return@OnEditorActionListener true
            }
            false
        })

        setUpSongSuggestion()

    }


    private fun setUpSongSuggestion() {

        queryVM.getSuggestionsList().observe(this, Observer {

            d("suggestions list $it")
            ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,it?.toTypedArray())
                    .also {
                        adapter ->
                        etSearchSong.setAdapter(adapter)

                    }
        })

        etSearchSong.onItemClickListener = AdapterView.OnItemClickListener {
            parent, _, position, _ ->
                query = parent.getItemAtPosition(position) as String
            getSongListResults() }


    }

    private fun insertQueryInSuggestionList() {

        queryVM.insertSuggestions(etSearchSong.text.toString().trim())
                .subscribe({
                    d("Successfully  added")
                },{
                    it.printStackTrace()
                })
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


        val noOfResultPerScreen : Int =  ((viewPagerHeight / (resources.getDimension(R.dimen.cvSongsHeightWithMargin)/displayMetrics.density)).toInt())
        setUpViewPager(noOfResultPerScreen)
        viewPager.adapter = musicAdapter
        tabLayout.setupWithViewPager(viewPager,true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.play_song_menu,menu)

        return true
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {

            android.R.id.home-> finish()
            R.id.favourite -> {
                startActivity(Intent(this, FavoriteListActivity::class.java))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun setUpViewPager(noOfResultPerScreen : Int) {

        d("noOfResult $noOfResultPerScreen")


        musicAdapter.printfragments()
        musicAdapter.clearAdapter()
        musicAdapter.notifyDataSetChanged()

        val bundle = Bundle()
        val isPerfectDivisible = songList.size % noOfResultPerScreen == 0
        val noOfFragments = if (isPerfectDivisible) songList.size / noOfResultPerScreen else (songList.size / noOfResultPerScreen) +1
        var startIndex = 0
        var endIndex = 0

        for (i in 0 until noOfFragments) {

            startIndex = endIndex
            endIndex = Math.min(songList.size , (endIndex + noOfResultPerScreen))

            musicAdapter.addFragment(NewFragmentInstanceList.create(ArrayList(songList.subList(startIndex, endIndex))))
        }



        musicAdapter.printfragments()
        musicAdapter.notifyDataSetChanged()

       // tabLayout.setupWithViewPager(viewPager)


    }

    internal inner class MusicPagerAdapter(fm : FragmentManager ) : FragmentStatePagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()
        var baseId = 31L
        // private var titleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {

            return fragmentList[position]
        }

        override fun getCount(): Int {

            return fragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_NONE
        }

        /* override fun getPageTitle(position: Int): CharSequence? {

             return titleList[position]
         }*/

        fun printfragments(){
            d("fragments ${fragmentList}")
        }

        fun addFragment(fragment: Fragment) {

            fragmentList.add(fragment)

        }

        fun clearAdapter() {

            fragmentList.clear()

        }



    }



    private fun getSongListResults() {

        songsVM.getSongListResults(etSearchSong.text.toString().trim()).observe(this, Observer {


            progressBar.visibility = View.GONE
           if (it != null && it.isNotEmpty()) {

                       songList.clear()
                       songList = ArrayList(it)
                       tvCount.text = "All Songs ${it?.size.toString()}"
                       setNumberOfResultsPerScreen()
                   }



        })
    }

}
