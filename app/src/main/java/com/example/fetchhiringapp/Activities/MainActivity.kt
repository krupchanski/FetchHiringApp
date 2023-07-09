package com.example.fetchhiringapp.Activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fetchhiringapp.*
import com.example.fetchhiringapp.Adapters.FetchItemAdapter
import com.example.fetchhiringapp.Data.FetchDataItem
import com.example.fetchhiringapp.Apis.IFetchApi
import com.example.fetchhiringapp.Database.DBHelper
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val btnLoad = findViewById<Button>(R.id.btnLoad)
        val checkBox = findViewById<CheckBox>(R.id.checkbox)
        var items: ArrayList<FetchDataItem>
        val db = DBHelper(this)
        if(!db.checkIfTableExists(db.getTableName())) {
            getItemsFromApi(this)
        }

        btnLoad.setOnClickListener {
            val isGroupByListId = checkBox.isChecked
            items = db.getFilteredItemsFromDB(isGroupByListId)
            recyclerView.adapter = FetchItemAdapter(items)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.setHasFixedSize(true)
        }
    }


    private fun getItemsFromApi(context: Context) {
        val fetchApi = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://fetch-hiring.s3.amazonaws.com/")
            .build()
            .create(IFetchApi::class.java)

        GlobalScope.launch(Dispatchers.IO) {
            val response = fetchApi.getItems()
            val dbHelper = DBHelper(context)
            var queryStatus: Long = 0
            if(response.isSuccessful) {
                for(item in response.body()!!) {
                    queryStatus = dbHelper.addItemToDB(item)
                    if(queryStatus > -1) {
                        Log.d("MY_TAG", "Successful add to DB + " + item.id)
                    } else {
                        Log.d("MY_TAG", "Failed to add to db")
                    }
                }
            }
        }
    }
}
