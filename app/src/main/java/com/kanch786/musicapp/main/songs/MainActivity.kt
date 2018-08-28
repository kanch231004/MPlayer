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
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import android.widget.TextView
import com.kanch786.musicapp.main.SongsViewModelFactory
import com.kanch786.musicapp.main.favorites.FavoriteListActivity
import com.kanch786.musicapp.main.query.QueryVM
import com.kanch786.musicapp.main.query.QueryViewModelFactory
import com.kanch786.musicapp.queryRepo
import com.kanch786.musicapp.songRepo
import android.widget.AdapterView
import android.widget.Toast
import com.kanch786.musicapp.Constants.LayoutOffset
import com.kanch786.musicapp.extensions.hideKeyboard
import com.kanch786.musicapp.R.id.tabLayout
import android.support.design.widget.TabLayout




class MainActivity : AppCompatActivity() {

    private lateinit var musicAdapter : MusicPagerAdapter
    private lateinit var songsVM: SongsVM
    private lateinit var queryVM : QueryVM
    private var songList = ArrayList<SongListResults>()
    private lateinit var query : String
    private lateinit var songsViewModelFactory: SongsViewModelFactory
    private lateinit var queryViewModelFactory: QueryViewModelFactory
    private var noOfResultPerScreen = -1
    private var seletectedTabPos = 0

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
        setNumberOfResultsPerScreen()

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putInt("tabPosition", seletectedTabPos)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
       tabLayout.getTabAt(savedInstanceState?.getInt("tabPosition") ?: 1)?.select()


    }


    private fun setUpSongSuggestion() {

        queryVM.getSuggestionsList().observe(this, Observer {

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

        val viewPagerHeight = (screenSize-(toolbarheight+etSearchHeight+tvCountHeight+(resources.getDimension(R.dimen.tabLayoutHeight)/ displayMetrics.density)+ LayoutOffset))

        noOfResultPerScreen = ((viewPagerHeight / (resources.getDimension(R.dimen.cvSongsHeightWithMargin)/displayMetrics.density)).toInt())
        setUpViewPager()


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

    private fun setUpViewPager() {


        musicAdapter.clearAdapter()
        musicAdapter.notifyDataSetChanged()

        val isPerfectDivisible = songList.size % noOfResultPerScreen == 0
        val noOfFragments = if (isPerfectDivisible) songList.size / noOfResultPerScreen else (songList.size / noOfResultPerScreen) +1
        var startIndex = 0
        var endIndex = 0

        for (i in 0 until noOfFragments) {

            startIndex = endIndex
            endIndex = Math.min(songList.size , (endIndex + noOfResultPerScreen))

            musicAdapter.addFragment(NewFragmentInstanceList.create(ArrayList(songList.subList(startIndex, endIndex))))
        }

        viewPager.adapter = musicAdapter
        tabLayout.setupWithViewPager(viewPager,true)
        viewPager.currentItem = tabLayout.selectedTabPosition
        musicAdapter.notifyDataSetChanged()
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
              seletectedTabPos = tab?.position ?: 0
            }


        })



    }

    internal inner class MusicPagerAdapter(fm : FragmentManager ) : FragmentStatePagerAdapter(fm) {

        private val fragmentList = ArrayList<Fragment>()


        override fun getItem(position: Int): Fragment {

            return fragmentList[position]
        }

        override fun getCount(): Int {

            return fragmentList.size
        }

        override fun getItemPosition(`object`: Any): Int {
            return PagerAdapter.POSITION_UNCHANGED

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

            d("results ${it}")

            when {


               it != null && it.isNotEmpty() -> {

                    hideKeyboard()
                    songList.clear()
                    songList = ArrayList(it)
                    tvCount.text = "All Songs ${it?.size.toString()}"
                    setUpViewPager()
                }

                it != null && it.isEmpty() -> {

                    musicAdapter.clearAdapter()
                    musicAdapter.notifyDataSetChanged()
                    tvCount.text = resources.getString(R.string.no_results)
                }

                else -> { d("do nothing")}
            }

        })
    }

}
