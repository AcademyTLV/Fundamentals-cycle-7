package com.android.academy.list

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.details.DetailsActivity
import com.android.academy.model.MovieModel
import com.android.academy.model.MoviesContent
import com.android.academy.model.MoviesContent.movies
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movies)

        loadMovies()
        with(movies_rv_list) {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@MoviesActivity)
            adapter = MoviesViewAdapter(movies, this@MoviesActivity, this@MoviesActivity)
        }
    }

    override fun onMovieClicked(itemPosition: Int) {
        if (itemPosition < 0 || itemPosition >= movies.size) return

        val intent = Intent(this, DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.EXTRA_ITEM_POSITION, itemPosition)
        startActivity(intent)
    }


    private fun loadMovies() {
        MoviesContent.addMovie(
            MovieModel(
                name = "Jurassic World - Fallen Kingdom",
                imageRes = R.drawable.jurassic_world_fallen_kingdom,
                trailerUrl = "https://www.youtube.com/watch?v=vn9mMeWcgoM)",
                releaseDate = "2018-06-06",
                backImageRes = R.drawable.jurassic_world_back,
                overview = "Three years after the demise of Jurassic World, a volcanic eruption threatens the remaining dinosaurs on the isla Nublar, so Claire Dearing, the former park manager, recruits Owen Grady to help prevent the extinction of the dinosaurs once again"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "The Meg",
                imageRes = R.drawable.the_meg,
                trailerUrl = "https://www.youtube.com/watch?v=bsLk0NPRFAc",
                backImageRes = R.drawable.the_meg_back,
                releaseDate = "2018-08-09",
                overview = "A deep sea submersible pilot revisits his past fears in the Mariana Trench, and accidentally unleashes the seventy foot ancestor of the Great White Shark believed to be extinct"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "The First Purge",
                imageRes = R.drawable.the_first_purge,
                trailerUrl = "https://www.youtube.com/watch?v=UL29y0ah92w",
                releaseDate = "2018-07-04",
                backImageRes = R.drawable.the_first_purge_back,
                overview = "To push the crime rate below one percent for the rest of the year, the New Founding Fathers of America test a sociological theory that vents aggression for one night in one isolated community. But when the violence of oppressors meets the rage of the others, the contagion will explode from the trial-city borders and spread across the nation"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Deadpool 2",
                imageRes = R.drawable.deadpool_2,
                trailerUrl = "https://www.youtube.com/watch?v=20bpjtCbCz0",
                releaseDate = "2018-05-15",
                backImageRes = R.drawable.deadpool_2_back,
                overview = "Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Black Panther",
                imageRes = R.drawable.black_panther,
                trailerUrl = "https://www.youtube.com/watch?v=xjDjIWPwcPU",
                releaseDate = "2018-02-13",
                backImageRes = R.drawable.black_panther_back,
                overview = "King T'Challa returns home from America to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne by factions within his own country as well as without. Using powers reserved to Wakandan kings, T'Challa assumes the Black Panther mantel to join with girlfriend Nakia, the queen-mother, his princess-kid sister, members of the Dora Milaje (the Wakandan 'special forces') and an American secret agent, to prevent Wakanda from being dragged into a world war"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Ocean's Eight",
                imageRes = R.drawable.ocean_eight,
                trailerUrl = "https://www.youtube.com/watch?v=n5LoVcVsiSQ",
                releaseDate = "2018-06-07",
                backImageRes = R.drawable.ocean_eight_back,
                overview = "Debbie Ocean, a criminal mastermind, gathers a crew of female thieves to pull off the heist of the century at New York's annual Met Gala"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Interstellar",
                imageRes = R.drawable.interstellar,
                trailerUrl = "https://www.youtube.com/watch?v=zSWdZVtXT7E",
                releaseDate = "2014-11-05",
                backImageRes = R.drawable.interstellar_back,
                overview = "Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Thor - Ragnarok",
                imageRes = R.drawable.thor_ragnarok,
                trailerUrl = "https://www.youtube.com/watch?v=ue80QwXMRHg",
                releaseDate = "2017-10-25",
                backImageRes = R.drawable.thor_ragnarok_back,
                overview = "Thor is on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, the prophecy of destruction to his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela"
            )
        )
        MoviesContent.addMovie(
            MovieModel(
                name = "Guardians of the Galaxy",
                imageRes = R.drawable.guardians_of_the_galaxy,
                trailerUrl = "https://www.youtube.com/watch?v=2LIQ2-PZBC8",
                releaseDate = "2014-07-30",
                backImageRes = R.drawable.guardians_of_the_galaxy_back,
                overview = "Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser"
            )
        )
    }
}