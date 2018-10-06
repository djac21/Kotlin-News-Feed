package com.djac21.kotlin.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.djac21.kotlin.model.NewsModel
import org.ocpsoft.prettytime.PrettyTime
import java.text.SimpleDateFormat
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.customtabs.CustomTabsIntent
import android.support.v4.content.ContextCompat
import java.util.TimeZone
import java.text.ParseException
import java.util.Date
import java.util.Locale
import android.view.View
import com.bumptech.glide.request.RequestOptions
import com.djac21.kotlin.R
import com.djac21.kotlin.customTabs.CustomTabActivityHelper
import com.djac21.kotlin.customTabs.WebViewActivity

class NewsAdapter(var news: List<NewsModel>, private val rowLayout: Int, private val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(rowLayout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        simpleDateFormat.timeZone = TimeZone.getTimeZone("UTC")
        var time: Long = 0
        try {
            time = simpleDateFormat.parse(news[position].getDate()).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        val prettyTime = PrettyTime(Locale.getDefault())

        holder.title.text = news[position].getTitle()
        holder.author.text = news[position].getAuthor()
        holder.description.text = news[position].getDescription()
        holder.date.text = prettyTime.format(Date(time))
        Glide.with(context)
                .load(news[position].getImage())
                .apply(RequestOptions()
                        .placeholder(R.drawable.kotlin)
                        .error(R.drawable.kotlin)
                        .skipMemoryCache(true)
                        .fitCenter())
                .into(holder.image)
    }

    override fun getItemCount(): Int {
        return news.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        internal var title: TextView = view.findViewById(R.id.title)
        internal var author: TextView = view.findViewById(R.id.author)
        internal var description: TextView = view.findViewById(R.id.description)
        internal var date: TextView = view.findViewById(R.id.date)
        internal var image: ImageView = view.findViewById(R.id.image)

        init {
            view.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = adapterPosition
            val url = news[position].getUrl()

            if (validateUrl(url)) {
                val uri = Uri.parse(url)
                if (uri != null) {
                    openCustomChromeTab(uri)
                }
            } else {
                Toast.makeText(context, "Error with link", Toast.LENGTH_SHORT).show()
            }
        }

        private fun validateUrl(url: String?): Boolean {
            return url != null && url.isNotEmpty() && (url.startsWith("http://") || url.startsWith("https://"))
        }

        private fun openCustomChromeTab(uri: Uri) {
            val intentBuilder = CustomTabsIntent.Builder()
            val customTabsIntent = intentBuilder.build()

            intentBuilder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary))
            intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(context, R.color.colorPrimaryDark))

            CustomTabActivityHelper().openCustomTab(context, customTabsIntent, uri, object : CustomTabActivityHelper.CustomTabFallback {
                override fun openUri(activity: Context, uri: Uri) {
                    openWebView(uri)
                }
            })
        }

        private fun openWebView(uri: Uri) {
            val webViewIntent = Intent(context, WebViewActivity::class.java)
            webViewIntent.putExtra("extra.url", uri.toString())
            webViewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(webViewIntent)
        }
    }
}