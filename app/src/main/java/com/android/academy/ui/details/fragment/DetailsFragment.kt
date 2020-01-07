package com.android.academy.ui.details.fragment

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.academy.R
import com.android.academy.model.MovieModel
import com.android.academy.networking.NetworkingConstants.YOUTUBE_BASE_URL
import com.android.academy.ui.details.DetailsActivityViewModel
import com.android.academy.utils.logD
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*

class DetailsFragment : Fragment(), View.OnClickListener {
    private var movieModel: MovieModel? = null
    private val picasso = Picasso.get()
    private val activityViewModel: DetailsActivityViewModel by viewModels()
    private val fragmentViewModel: DetailsFragmentViewModel  by viewModels()

    companion object {

        private val ARG_MOVIE = "MovieModel-data"

        fun newInstance(movieModel: MovieModel): DetailsFragment {
            val fragment = DetailsFragment()
            val args = Bundle()
            args.putParcelable(ARG_MOVIE, movieModel)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeState()
        observerOpenTrailer()

        movieModel = arguments?.getParcelable(ARG_MOVIE)
        logD("movieModel: " + movieModel!!)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setClickListeners()
        setMovie()
    }

    private fun setClickListeners() {
        btnDetails.setOnClickListener(this)
        btnDownload.setOnClickListener(this)
    }

    private fun setMovie() {
        movieModel?.let {
            picasso.load(it.imageUrl).into(details_iv_image)
            picasso.load(it.backImageUrl).into(details_iv_back)
            details_tv_title.text = it.name
            details_tv_released_date.text = it.releaseDate
            details_tv_overview_text.text = it.overview
        }
    }

    private fun observeState() {
        fragmentViewModel.getState().observe(this, Observer {
            if (it == null) return@Observer

            logD("State: ${it.name}")

            when (it) {
                State.LOADING -> setButtonLoadingStatus()
                State.LOADED -> resetButtonStatus()
                State.ERROR -> Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun observerOpenTrailer() {
        fragmentViewModel.getOpenTrailer().observe(this, Observer {
            logD("open trailer called in fragment $this")
            val trailerUrl = "${YOUTUBE_BASE_URL}$it"
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
            startActivity(browserIntent)
        })
    }


    override fun onClick(view: View) {
        when (view.id) {
            btnDetails.id -> movieModel?.let { fragmentViewModel.getTrailer(it) }
            btnDownload.id -> downloadImage()
        }
    }

    private fun setButtonLoadingStatus() {
        val context = context ?: return
        val rotateDrawable =
            ContextCompat.getDrawable(context, R.drawable.progress_animation) as RotateDrawable
        val anim = ObjectAnimator.ofInt(rotateDrawable, "level", 0, 10000)
        anim.duration = 1000
        anim.repeatCount = ValueAnimator.INFINITE
        anim.start()
        btnDetails.setText(R.string.details_loading_trailer_text)
        btnDetails.setCompoundDrawablesWithIntrinsicBounds(
            rotateDrawable,
            null,
            null,
            null
        )
    }

    private fun resetButtonStatus() {
        btnDetails.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        btnDetails.setText(R.string.details_trailer_text)
    }

    private fun downloadImage() {
        movieModel?.let { activityViewModel.downloadImageClicked(it) }
    }
}
