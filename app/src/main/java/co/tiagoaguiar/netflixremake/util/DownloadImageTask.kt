package co.tiagoaguiar.netflixremake.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Log
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors


/*fazendo download na unha sem FrameWork -Start */
class DownloadImageTask(private val callback:Callback) {
    private val handler = android.os.Handler(Looper.getMainLooper())
    private val executor = Executors.newSingleThreadExecutor()


    interface Callback {
        fun onResult(bitmap: Bitmap)
    }

    fun execute(url: String) {
        executor.execute {
            var urlConnection: HttpURLConnection? = null
            var stream: InputStream? = null
            try{
                val requestUrl = URL(url) /* abrir a URL */
                urlConnection = requestUrl.openConnection() as HttpURLConnection /* abrir a conxão*/
                urlConnection.readTimeout = 2000 /* limitando tempo de leitura (2)*/
                urlConnection.connectTimeout = 2000 /* limitando tempo de conexão */

                val statusCode: Int = urlConnection.responseCode
                if (statusCode > 400) {
                    throw IOException("Erro na comunicação com o Servidor!")
                }
                stream = urlConnection.inputStream
               val bitmap= BitmapFactory.decodeStream(stream)

                handler.post{
                    callback.onResult(bitmap)
                }
            }catch (e:Exception){
                val message = e.message?: "erro desconhecido"
                Log.e("Teste", message , e)
            } finally {
                urlConnection?.disconnect()
                stream?.close()
            }
        }
    }
}

/*fazendo download na unha sem FrameWork -END */
