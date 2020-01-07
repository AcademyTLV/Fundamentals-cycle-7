package com.android.academy.ui.details

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.academy.R
import com.android.academy.download.DownloadActivity
import com.android.academy.model.MovieModel
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM_POSITION = "init-position-data"
    }

    private val viewModel: DetailsActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        observeMovies()
        observerDownloadImage()
    }

    private fun observeMovies() {
        viewModel.getMovies().observe(this, Observer {
            it?.let { setSectionsPagerAdapter(it) }
        })
    }

    private fun observerDownloadImage() {
        viewModel.getDownloadImage().observe(this, Observer {
            DownloadActivity.startActivity(this, it)
        })
    }

    private fun setSectionsPagerAdapter(it: List<MovieModel>) {
        details_vp_container.adapter = SectionsPagerAdapter(supportFragmentManager, it)
        val startPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0)
        details_vp_container.setCurrentItem(startPosition, false)
    }
}