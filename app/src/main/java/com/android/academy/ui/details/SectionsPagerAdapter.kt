package com.android.academy.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.academy.model.MoviesContent

class SectionsPagerAdapter(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = MoviesContent.movies.size

    override fun getItem(position: Int): Fragment {
        return MovieDetailsFragment.newInstance(MoviesContent.movies[position])
    }
}