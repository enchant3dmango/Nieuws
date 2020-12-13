package com.example.nieuws

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.nieuws.adapter.NewsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val BASE_URL = "https://newsapi.org"

class MainActivity : AppCompatActivity() {

    lateinit var countDownTimer: CountDownTimer

    private var titlesList = mutableListOf<String>()
    private var descsList = mutableListOf<String>()
    private var authorList = mutableListOf<String>()
    private var linksList = mutableListOf<String>()
    private var imagesList = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        hideActionBar()
        setContentView(R.layout.activity_main)

        makeAPIRequest()
    }

    private fun initView() {
        rv_recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        rv_recyclerView.adapter =
            NewsAdapter(titlesList, descsList, authorList, linksList, imagesList)
    }

    private fun hideActionBar() {
        try {
            this.supportActionBar!!.hide()
        } catch (e: java.lang.NullPointerException) { }

        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
    }

    private fun fadeInFromBlack() {
        v_blackScreen.animate().apply {
            alpha(0f)
            duration = 3000
        }.start()
    }

    private fun addItemtoView(
        title: String,
        description: String,
        author: String,
        link: String,
        image: String
    ) {
        titlesList.add(title)
        descsList.add(description)
        authorList.add(author)
        linksList.add(link)
        imagesList.add(image)
    }

    private fun makeAPIRequest() {
        progressBar.visibility = View.VISIBLE
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIRequest::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            try {
                val response = api.getNews()

                for (article in response.articles) {
                    addItemtoView(
                        article.title,
                        article.content,
                        article.author,
                        article.url,
                        article.urlToImage
                    )
                }

                withContext(Dispatchers.Main) {
                    initView()
                    fadeInFromBlack()
                    progressBar.visibility = View.GONE
                }
            } catch (e: Exception) {
                Log.i("MainActivity", e.toString())

                withContext(Dispatchers.Main) {
                    attemptRequestAgain()
                }
            }
        }
    }

    private fun attemptRequestAgain() {
        countDownTimer = object: CountDownTimer(5*1000, 1000) {
            override fun onFinish() {
                makeAPIRequest()
                countDownTimer.cancel()
            }

            override fun onTick(millisUntilFinished: Long) {
                Log.i("MainActivity", "Could not retrieve data... Trying again in ${millisUntilFinished/1000} secs")
            }
        }
        countDownTimer.start()
    }
}