package com.example.newsly

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.littlemango.stacklayoutmanager.StackLayoutManager
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class MainActivity : AppCompatActivity() {

    lateinit var adapter: NewsAdapter
    lateinit var newsList: RecyclerView
    lateinit var container: RelativeLayout
    private var articles = mutableListOf<Article>()
    var pageNum = 1
    var totalResults = -1
    val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        newsList = findViewById(R.id.newsList)
        container = findViewById(R.id.container)

        adapter = NewsAdapter(this@MainActivity, articles)
        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(this@MainActivity)

        val layoutManager = StackLayoutManager(StackLayoutManager.ScrollOrientation.BOTTOM_TO_TOP)
        layoutManager.setPagerMode(true)
        layoutManager.setPagerFlingVelocity(3000)
        layoutManager.setItemChangedListener(object : StackLayoutManager.ItemChangedListener {
            override fun onItemChanged(position: Int) {

                container.setBackgroundColor(Color.parseColor(ColorPicker.getColor()))

                Log.d(TAG, "First Visible Item - ${layoutManager.getFirstVisibleItemPosition()}")
                Log.d(TAG, "Total Count - ${layoutManager.itemCount}")

                if (totalResults > layoutManager.itemCount &&
                    layoutManager.getFirstVisibleItemPosition() >=
                    layoutManager.itemCount - 5) {
                    //next page data
                    pageNum++
                    getNews()
                }

            }

        })
        newsList.layoutManager = layoutManager

        getNews()

    }

    private fun getNews() {
        Log.d(TAG, "Request sent for - ${pageNum}")
        val news = NewsService.newsInstance.getHeadlines("in", pageNum)
        news.enqueue(object : retrofit2.Callback<News> {
            override fun onResponse(call: Call<News>, response: Response<News>) {
                val news = response.body()
                if (news != null) {
                    Log.d("news", news.toString())
                    totalResults = news.totalResults
                    articles.addAll(news.articles)
                    adapter.notifyDataSetChanged()
                }
            }

            override fun onFailure(call: Call<News>, t: Throwable) {
                Toast.makeText(applicationContext, "Error in fetching news", Toast.LENGTH_LONG)
                    .show()
            }
        })
    }
}