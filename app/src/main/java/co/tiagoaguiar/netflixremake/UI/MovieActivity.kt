package co.tiagoaguiar.netflixremake.UI

import android.content.Context
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import co.tiagoaguiar.netflixremake.R

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val toolbar: Toolbar = findViewById(R.id.movie_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /* busquie o desenhavel (Layer-List) */
        val layerDrawable :LayerDrawable = ContextCompat.getDrawable(this,R.drawable.shadows) as LayerDrawable

        /* busquei um novo filme */
        val movieCover = ContextCompat.getDrawable(this,R.drawable.movie_4)

        /* atribui a esse layer-list um novo filme*/
        layerDrawable.setDrawableByLayerId(R.id.cover_drawable,movieCover)

        /* set no imageView*/
        val coverImg: ImageView = findViewById(R.id.movie_img)
        coverImg.setImageDrawable(layerDrawable)
    }
}