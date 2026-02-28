package com.example.flixsterplus

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PersonAdapter
    private val people = mutableListOf<Person>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)

        adapter = PersonAdapter(this, people)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        fetchPopularPeople()
    }

    private fun fetchPopularPeople() {

        val url =
            "https://api.themoviedb.org/3/person/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed"

        val client = AsyncHttpClient()

        client.get(url, object : JsonHttpResponseHandler() {

            override fun onSuccess(
                statusCode: Int,
                headers: Headers?,
                json: JSON?
            ) {

                val results: JSONArray =
                    json?.jsonObject?.optJSONArray("results") ?: return

                // Clear list first (prevents duplicates on reload)
                people.clear()

                for (i in 0 until results.length()) {

                    val personObj = results.getJSONObject(i)

                    val name = personObj.optString("name", "Unknown")
                    val profilePath = personObj.optString("profile_path", "")
                    val popularity = personObj.optDouble("popularity", 0.0)

                    // Handle known_for safely
                    val knownForArray = personObj.optJSONArray("known_for")
                    var knownForTitle = "N/A"

                    if (knownForArray != null && knownForArray.length() > 0) {
                        val firstItem = knownForArray.getJSONObject(0)
                        knownForTitle =
                            firstItem.optString(
                                "title",
                                firstItem.optString("name", "N/A")
                            )
                    }

                    people.add(
                        Person(
                            name = name,
                            profilePath = profilePath,
                            popularity = popularity,
                            knownFor = knownForTitle
                        )
                    )
                }

                adapter.notifyDataSetChanged()
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                errorResponse: String,
                throwable: Throwable?
            ) {
                Log.e("API_ERROR", "Failed to fetch data")
                throwable?.printStackTrace()
            }
        })
    }
}