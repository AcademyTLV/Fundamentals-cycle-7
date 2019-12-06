package com.android.academy.ui.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.android.academy.R
import kotlinx.android.synthetic.main.activity_details.*

class DetailsActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_ITEM_POSITION = "init-position-data"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        setSectionsPagerAdapter()
    }

    private fun setSectionsPagerAdapter() {
        details_vp_container.adapter = SectionsPagerAdapter(supportFragmentManager)
        val startPosition = intent.getIntExtra(EXTRA_ITEM_POSITION, 0)
        details_vp_container.setCurrentItem(startPosition, false)
    }
}