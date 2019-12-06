package com.android.academy.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.academy.model.MovieModel

class SectionsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = 0 // TODO MoviesContent.movies.size

    override fun getItem(position: Int): Fragment {
        return MovieDetailsFragment.newInstance(
            MovieModel(0, "","", "",
                "", "", 0.0))
//            TODO MoviesContent.movies[position]
    }
}