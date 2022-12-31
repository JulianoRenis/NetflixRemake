package co.tiagoaguiar.netflixremake.UI

import android.content.Context
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.adapter.MovieAdapter
import co.tiagoaguiar.netflixremake.model.Movie

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val txtTitle: TextView = findViewById(R.id.movie_text_title)
        val txtDesc: TextView = findViewById(R.id.movie_txt_desc)
        val txtCast: TextView = findViewById(R.id.movie_txt_cast)

        txtTitle.text = "Batman Begins"
        txtDesc.text= "Essa é a descrição do filme do Batman"
        txtCast.text = getString(R.string.cast,"Ator A, Ator B, Ator C, Atriz A, Atriz B")
        val rv:RecyclerView = findViewById(R.id.movie_rv_similar)

        val movies = mutableListOf<Movie>()

        rv.layoutManager = GridLayoutManager(this,3)
        rv.adapter = MovieAdapter(movies,R.layout.movie_item_similar)

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle(null)

        val layerDrawable :LayerDrawable = ContextCompat.getDrawable(this,R.drawable.shadows) as LayerDrawable
        val movieCover = ContextCompat.getDrawable(this,R.drawable.movie_4)
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable,movieCover)
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)
    }
}