package com.android.academy.list

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.academy.R
import com.android.academy.model.MovieModel
import kotlinx.android.synthetic.main.activity_movies.*

class MoviesActivity : AppCompatActivity(), OnMovieClickListener {

    private val movies: MutableList<MovieModel> = mutableListOf()

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
        val movieModel = movies[itemPosition]
        if (movieModel.name.isEmpty()) return
        Toast.makeText(this, movieModel.name, Toast.LENGTH_SHORT).show()
    }

    private fun loadMovies() {
        movies.add(
            MovieModel(
                "Jurassic World - Fallen Kingdom",
                R.drawable.jurassic_world_fallen_kingdom,
                "Three years after the demise of Jurassic World, a volcanic eruption threatens the remaining dinosaurs on the isla Nublar, so Claire Dearing, the former park manager, recruits Owen Grady to help prevent the extinction of the dinosaurs once again"
            )
        )
        movies.add(
            MovieModel(
                "The Meg",
                R.drawable.the_meg,
                "A deep sea submersible pilot revisits his past fears in the Mariana Trench, and accidentally unleashes the seventy foot ancestor of the Great White Shark believed to be extinct"
            )
        )
        movies.add(
            MovieModel(
                "The First Purge",
                R.drawable.the_first_purge,
                "To push the crime rate below one percent for the rest of the year, the New Founding Fathers of America test a sociological theory that vents aggression for one night in one isolated community. But when the violence of oppressors meets the rage of the others, the contagion will explode from the trial-city borders and spread across the nation"
            )
        )
        movies.add(
            MovieModel(
                "Deadpool 2",
                R.drawable.deadpool_2,
                "Wisecracking mercenary Deadpool battles the evil and powerful Cable and other bad guys to save a boy's life"
            )
        )
        movies.add(
            MovieModel(
                "Black Panther",
                R.drawable.black_panther,
                "King T'Challa returns home from America to the reclusive, technologically advanced African nation of Wakanda to serve as his country's new leader. However, T'Challa soon finds that he is challenged for the throne by factions within his own country as well as without. Using powers reserved to Wakandan kings, T'Challa assumes the Black Panther mantel to join with girlfriend Nakia, the queen-mother, his princess-kid sister, members of the Dora Milaje (the Wakandan 'special forces') and an American secret agent, to prevent Wakanda from being dragged into a world war"
            )
        )
        movies.add(
            MovieModel(
                "Ocean's Eight",
                R.drawable.ocean_eight,
                "Debbie Ocean, a criminal mastermind, gathers a crew of female thieves to pull off the heist of the century at New York's annual Met Gala"
            )
        )
        movies.add(
            MovieModel(
                "Interstellar",
                R.drawable.interstellar,
                "Interstellar chronicles the adventures of a group of explorers who make use of a newly discovered wormhole to surpass the limitations on human space travel and conquer the vast distances involved in an interstellar voyage"
            )
        )
        movies.add(
            MovieModel(
                "Thor - Ragnarok",
                R.drawable.thor_ragnarok,
                "Thor is on the other side of the universe and finds himself in a race against time to get back to Asgard to stop Ragnarok, the prophecy of destruction to his homeworld and the end of Asgardian civilization, at the hands of an all-powerful new threat, the ruthless Hela"
            )
        )
        movies.add(
            MovieModel(
                "Guardians of the Galaxy",
                R.drawable.guardians_of_the_galaxy,
                "Light years from Earth, 26 years after being abducted, Peter Quill finds himself the prime target of a manhunt after discovering an orb wanted by Ronan the Accuser"
            )
        )
    }
}