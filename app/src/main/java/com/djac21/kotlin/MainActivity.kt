package com.djac21.kotlin

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.djac21.kotlin.adapter.NewsAdapter
import com.djac21.kotlin.api.APIClient
import com.djac21.kotlin.api.APIInterface
import com.djac21.kotlin.model.NewsModel
import com.djac21.kotlin.model.NewsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val tag = MainActivity::class.java.simpleName
    private val apiKey = "212c1dceeac8453d99337f0062e998f3"
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var progressBar: ProgressBar
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBar)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(baseContext)
        val dialogShown = sharedPreferences.getBoolean("dialogShown", false)
        if (!dialogShown) {
            aboutDialog("Welcome!", false)
            sharedPreferences.edit().putBoolean("dialogShown", true).apply()
        }

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout)
        swipeRefreshLayout.setOnRefreshListener {
            swipeRefreshLayout.isRefreshing = true
            GetNews().execute()
        }

        GetNews().execute()
    }

    override fun onResume() {
        super.onResume()
        GetNews().execute()
    }

    @SuppressLint("StaticFieldLeak")
    inner class GetNews : AsyncTask<Void, Void, Void>() {

        override fun onPreExecute() {
            super.onPreExecute()
            progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg params: Void): Void? {
            val apiInterface = APIClient().getClient()?.create<APIInterface>(APIInterface::class.java)
            val call = apiInterface?.getNews(apiKey)
            call?.enqueue(object : Callback<NewsResponse> {
                override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                    if (response.isSuccessful) {
                        val news: List<NewsModel>? = response.body()?.getResults()
                        recyclerView.adapter = NewsAdapter(news!!, R.layout.item_view, applicationContext)

                        for (model in news) {
                            Log.d(tag, "Author: " + model.getAuthor())
                            Log.d(tag, "Title: " + model.getTitle())
                            Log.d(tag, "Description: " + model.getDescription())
                            Log.d(tag, "Image URL: " + model.getImage())
                            Log.d(tag, "URL: " + model.getUrl())
                            Log.d(tag, "Published At: " + model.getDate())
                            Log.d(tag, "----------------------------------")
                        }
                    } else {
                        Log.d(tag, "Response not successful $response")
                    }
                }

                override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                    val builder = AlertDialog.Builder(applicationContext)
                            .setTitle("Error!")
                            .setCancelable(false)
                            .setMessage("The was an error on displaying the data, please be sure that you have internet access")
                            .setNegativeButton("Cancel", null)
                            .setPositiveButton("Retry") { _, _ ->
                                GetNews().execute()
                            }
                    builder.create().show()
                }
            })
            return null
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
            swipeRefreshLayout.isRefreshing = false
            progressBar.visibility = View.GONE
        }
    }

    private fun aboutDialog(title: String, cancelable: Boolean) {
        val builder = AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(R.string.about_dialog)
                .setCancelable(cancelable)
                .setPositiveButton(android.R.string.ok, null)
        builder.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_about)
            aboutDialog("About", true)
        else if (id == R.id.action_refresh)
            GetNews().execute()

        return super.onOptionsItemSelected(item)
    }
}