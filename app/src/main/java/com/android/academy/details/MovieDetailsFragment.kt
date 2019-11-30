package com.android.academy.details

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
import com.android.academy.R
import com.android.academy.db.AppDatabase
import com.android.academy.model.MovieModel
import com.android.academy.model.MovieModelConverter
import com.android.academy.model.VideoModel
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
            picasso.load(it.imageUrl).into(details_iv_image)
            picasso.load(it.backImageUrl).into(details_iv_back)
            details_tv_title.text = it.name
            details_tv_released_date.text = it.releaseDate
            details_tv_overview_text.text = it.overview
        }
    }

    override fun onClick(view: View) {
        movieModel?.let { movieModel ->
            setButtonLoadingStatus()

            context?.let {
                val videoModel: VideoModel? =
                    AppDatabase.getInstance(context!!)?.videoDao()?.getVideo(movieModel.movieId)
                videoModel?.let {
                    resetButtonStatus()
                    startActivityWithTrailer(videoModel.key)
                    return
                }
            }
            getTrailersFromServer(movieModel)
        }
    }

    private fun getTrailersFromServer(it: MovieModel) {
        RestClient.moviesService.getTrailers(it.movieId).enqueue(object :
            Callback<TrailersListResult> {
            override fun onFailure(call: Call<TrailersListResult>, t: Throwable) {
                resetButtonStatus()
                Toast.makeText(
                    context,
                    R.string.something_went_wrong,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<TrailersListResult>,
                response: Response<TrailersListResult>
            ) {
                resetButtonStatus()
                response.body()?.let { result ->
                    val convertedVideoModel: VideoModel? =
                        MovieModelConverter.convertVideoResult(result)
                    result.results.firstOrNull()?.key?.let {
                        startActivityWithTrailer(it)
                        AppDatabase.getInstance(context!!)?.videoDao()?.insert(convertedVideoModel)
                    }
                }
            }

        })
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
        details_btn_trailer.setText(R.string.details_loading_trailer_text)
        details_btn_trailer.setCompoundDrawablesWithIntrinsicBounds(
            rotateDrawable,
            null,
            null,
            null
        )
    }

    private fun resetButtonStatus() {
        details_btn_trailer.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        details_btn_trailer.setText(R.string.details_trailer_text)
    }
}
