package com.android.academy

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class DetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        details_btn_trailer.setOnClickListener {
            val trailerUrl = getString(R.string.infinity_war_trailer)
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
            startActivity(browserIntent)
        }
    }

}
