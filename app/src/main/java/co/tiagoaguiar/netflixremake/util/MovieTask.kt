package co.tiagoaguiar.netflixremake.util

import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import co.tiagoaguiar.netflixremake.model.MovieDetail
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.logging.Handler

class MovieTask(private val callback: Callback){

    private val handler = android.os.Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

        interface  Callback{
            fun onResult(movieDetail: MovieDetail)
            fun onFailure(message: String)
            fun onPreExecute()

        }
    fun excute(url: String) {
        /*nesse momento, estamos utlizando a UI-thread (1)*/
        callback.onPreExecute()
        executor.execute {
            var urlConnection :HttpURLConnection?= null
            var buffer: BufferedInputStream? = null
            var stream : InputStream? = null

            try {
                /*nesse momento, estamos utlizando a NOVA-thread [Processo paralelo](2)*/
                val requestUrl = URL(url) /* abrir a URL */
                urlConnection = requestUrl.openConnection() as HttpURLConnection /* abrir a conxão*/
                urlConnection.readTimeout = 2000 /* limitando tempo de leitura (2)*/
                urlConnection.connectTimeout = 2000 /* limitando tempo de conexão */

                val statusCode: Int = urlConnection.responseCode

                if (statusCode==400){
                    stream = urlConnection.errorStream
                    buffer = BufferedInputStream(stream)
                    val jsonAsString = toString(buffer)

                    val json = JSONObject(jsonAsString)
                    json.getString("message")

                } else if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o Servidor!")
                }


                // forma 1 : simples e rádida
             /*   val stream = urlconnection.inputStream // sequencias de bytes
                val jsonAsString = stream.bufferedReader().use { it.readText() }
                Log.i("Teste", jsonAsString) */


                /* forma2 : byte -> string */
                 stream = urlConnection.inputStream
                 buffer = BufferedInputStream(stream)
                val jsonAsString = toString(buffer)

                val movieDetail = toMovieDetail(jsonAsString)

                handler.post{
                    callback.onResult(movieDetail)
                }

            } catch (e: IOException) {
                val message = e.message?: "erro desconhecido"
                Log.e("Teste", message , e)

                handler.post{
                    callback.onFailure(message)
                }

            }finally {
                urlConnection?.disconnect()
                stream?.close()
                buffer?.close()
            }
        }
    }

    private fun toMovieDetail(jsonAsString: String): MovieDetail{
        val json = JSONObject(jsonAsString)

        val id = json.getInt("id")
        val title = json.getString("title")
        val desc = json.getString("desc")
        val cast = json.getString("cast")
        val coverUrl = json.getString("cover_url")
        val jsonMovies = json.getJSONArray("movie")

        val similars = mutableListOf<Movie>()
        for(i in 0 until jsonMovies.length()){
            val jsonMovie = jsonMovies.getJSONObject(i)

            val similarId = jsonMovie.getInt("id")
            val similarCoverUrl = jsonMovie.getString("cover_url")

            val m = Movie(similarId,similarCoverUrl)
            similars.add(m)
        }

        val movie = Movie(id, coverUrl ,title,desc,cast )
        return MovieDetail(movie,similars )
    }



    private fun toString(stream: InputStream): String {
        val bytes = ByteArray(1024)
        val baos = ByteArrayOutputStream()
        var read: Int

        while(true){
            read = stream.read(bytes)
            if (read <= 0){
                break
            }
            baos.write(bytes,0,read)
        }
        return String(baos.toByteArray())
    }
}