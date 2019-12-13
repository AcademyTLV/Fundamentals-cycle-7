package com.android.academy.ui.details

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.drawable.RotateDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.android.academy.R
import com.android.academy.db.AppDatabase
import com.android.academy.download.DownloadActivity
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.TrailerModel
import com.android.academy.networking.NetworkingConstants.YOUTUBE_BASE_URL
import com.android.academy.networking.RestClient
import com.android.academy.networking.TrailersListResult
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsFragment : Fragment(), View.OnClickListener {
    private var movieModel: MovieModel? = null
    private val picasso = Picasso.get()
    private lateinit var detailsViewModel: DetailsViewModel

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
        detailsViewModel = activity?.run {
            ViewModelProviders.of(this)[DetailsViewModel::class.java]
        } ?: throw Exception("Invalid Activity")

        movieModel = arguments?.getParcelable(ARG_MOVIE)
        Log.d(TAG, "movieModel: " + movieModel!!)
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

    override fun onClick(view: View) {
        movieModel?.let {
            when (view.id) {
                btnDetails.id -> {
                    setButtonLoadingStatus()
                    getMovieTrailers(it)
                }
                btnDownload.id -> downloadImage()
            }
        }
    }

    private fun getMovieTrailers(movieModel: MovieModel) {
        if (context == null) return
        val trailerModel: TrailerModel? =
            AppDatabase.getInstance(context!!)?.videoDao()?.getVideo(movieModel.movieId)
        trailerModel?.let {
            resetButtonStatus()
            startActivityWithTrailer(trailerModel.key)
            return
        }
        getTrailersFromServer(movieModel)
    }

    private fun getTrailersFromServer(movieModel: MovieModel) {
        RestClient.moviesService.getTrailers(movieModel.movieId).enqueue(object :
            Callback<TrailersListResult> {
            override fun onFailure(call: Call<TrailersListResult>, t: Throwable) {
                onFailedGettingTrailerFromServer()
            }

            override fun onResponse(call: Call<TrailersListResult>, response: Response<TrailersListResult>) {
                onTrailerReceivedFromServer(response)
            }

        })
    }

    private fun onTrailerReceivedFromServer(response: Response<TrailersListResult>) {
        resetButtonStatus()
        response.body()?.let { result ->
            result.results.firstOrNull()?.key?.let { key ->
                startActivityWithTrailer(key)
                saveTrailerResultToDb(result)
            }
        }
    }

    private fun saveTrailerResultToDb(result: TrailersListResult) {
        val convertedTrailerModel: TrailerModel? = MovieModelConverter.convertTrailerResult(result)
        AppDatabase.getInstance(context!!)?.videoDao()?.insert(convertedTrailerModel)
    }

    private fun onFailedGettingTrailerFromServer() {
        resetButtonStatus()
        Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_SHORT).show()
    }

    private fun startActivityWithTrailer(it: String) {
        val trailerUrl = "${YOUTUBE_BASE_URL}$it"
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl))
        startActivity(browserIntent)
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
        movieModel?.let { detailsViewModel.downloadImageClicked(it) }
    }
}
