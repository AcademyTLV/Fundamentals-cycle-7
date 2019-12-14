package com.android.academy.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.academy.model.MovieModel
import com.android.academy.ui.details.fragment.DetailsFragment

class SectionsPagerAdapter(
    fm: FragmentManager, private val movies: List<MovieModel>
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getCount() = movies.size

    override fun getItem(position: Int): Fragment {
        return DetailsFragment.newInstance(movies[position])
    }
}