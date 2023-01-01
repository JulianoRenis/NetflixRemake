package co.tiagoaguiar.netflixremake.UI

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.adapter.CategoryAdapter
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity(), CategoryTask.Callback {
    /* Model View/Controller - MVC */

    private lateinit var progres: ProgressBar
    private lateinit var adapter: CategoryAdapter
    private val categories = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progres = findViewById(R.id.progress_main)
         adapter = CategoryAdapter(categories){id->
             val intent = Intent (this@MainActivity,MovieActivity::class.java)
            intent.putExtra("id",id)
             startActivity(intent)
         }
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask(this).excute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=379fa363-4924-4e3c-8697-6150e553388a")
    }

    override fun onResult(categories: List<Category>) {
        Log.i("Teste main Activity", categories.toString())
        this.categories.clear()
        this.categories.addAll(categories)
        adapter.notifyDataSetChanged()//  força o adapter chamar o onBindViewHolder
        progres.visibility = View.GONE

    }

    override fun onFailure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        progres.visibility = View.GONE

    }

    override fun onPreExecute() {
        progres.visibility = View.VISIBLE
    }

}
