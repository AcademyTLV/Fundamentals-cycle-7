package com.android.academy.details

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.android.academy.R
import com.android.academy.model.MovieModel
import kotlinx.android.synthetic.main.fragment_details.*

class MovieDetailsFragment : Fragment(), View.OnClickListener {
    private var movieModel: MovieModel? = null

    companion object {

        private val TAG = "MovieDetailsFragment"
        private val ARG_MOVIE = "MovieModel-data"

        fun newInstance(movieModel: MovieModel): MovieDetailsFragment {
            val fragment = MovieDetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movieModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieModel = arguments?.getParcelable(ARG_MOVIE)
        Log.d(TAG, "movieModel: " + movieModel!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        details_btn_trailer.setOnClickListener(this)

        setMovie()
    }

    private fun setMovie() {
        movieModel?.let {
            details_iv_image.setImageResource(it.imageRes)
            details_iv_back.setImageResource(it.backImageRes)
            details_tv_title.text = it.name
            details_tv_released_date.text = it.releaseDate
            details_tv_overview_text.text = it.overview
        }
    }

    override fun onClick(view: View) {
        movieModel?.trailerUrl?.let { trailerUrl ->
            if (trailerUrl.isNotBlank()) {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
                startActivity(browserIntent)
            }
        }
    }
}
