package co.tiagoaguiar.netflixremake.UI

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.tiagoaguiar.netflixremake.R
import co.tiagoaguiar.netflixremake.adapter.CategoryAdapter
import co.tiagoaguiar.netflixremake.model.Category
import co.tiagoaguiar.netflixremake.util.CategoryTask

class MainActivity : AppCompatActivity() {
    /* Model View/Controller - MVC */


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val categories = mutableListOf<Category>()

        val adapter = CategoryAdapter(categories)
        val rv: RecyclerView = findViewById(R.id.rv_main)
        rv.layoutManager = LinearLayoutManager(this)
        rv.adapter = adapter

        CategoryTask().excute("https://api.tiagoaguiar.co/netflixapp/home?apiKey=379fa363-4924-4e3c-8697-6150e553388a")
    }

}
