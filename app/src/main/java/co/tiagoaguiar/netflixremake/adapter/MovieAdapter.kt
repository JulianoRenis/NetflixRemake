package co.tiagoaguiar.netflixremake.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.util.DownloadImageTask
import com.squareup.picasso.Picasso

//lista Horizontal
class MovieAdapter(private val movies: List<Movie>,
    @LayoutRes private val layoutId: Int,
         private val onItemClickListener:((Int)-> Unit)? = null
    ) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)

    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            val movieImage: ImageView = itemView.findViewById(R.id.movie_id)
            movieImage.setOnClickListener {
                onItemClickListener?.invoke(movie.id)
            }

            /* fazendo na unha sem frameWork - Start*/
            DownloadImageTask(object : DownloadImageTask.Callback {
                override fun onResult(bitmap: Bitmap) {
                    movieImage.setImageBitmap(bitmap)
                }
            }).execute(movie.coverUrl)
            /* fazendo na unha sem frameWork - End*/

        /* Usando picasso consigo fazer o download
        das imagens em apenas uma linha de codigo
         */
//            Picasso.get()
//                .load(movie.coverUrl)
//                .into(movieImage)
        }
    }

}
