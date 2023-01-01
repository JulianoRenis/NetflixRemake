package co.tiagoaguiar.netflixremake.util

import android.os.Looper
import android.util.Log
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.model.Movie
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.logging.Handler

class CategoryTask(private val callback: Callback){

    private val handler = android.os.Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()

        interface  Callback{
            fun onResult(categories: List<Category>)
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
                if (statusCode > 400) {
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
                val categories = toCategories(jsonAsString)

                handler.post{
                    callback.onResult(categories)
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

    private fun toCategories(jsonAsString: String) : List<Category>{
        val categories = mutableListOf<Category>()
        val jsonRoot = JSONObject(jsonAsString)
        val jsonCategories = jsonRoot.getJSONArray("category")

        for(i in 0 until  jsonCategories.length()){
           val jsonCategory =  jsonCategories.getJSONObject(i)
            val title = jsonCategory.getString("title")
            val jsonMovies = jsonCategory.getJSONArray("movie")

            val movies = mutableListOf<Movie>()
            for(j in 0 until jsonMovies.length() ){
               val jsonMovie= jsonMovies.getJSONObject(j)
                val id  = jsonMovie.getInt("id")
                val coverUrl  = jsonMovie.getString("cover_url")

                movies.add(Movie(id,coverUrl))
            }
            categories.add(Category(title,movies))

        }
        return categories
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