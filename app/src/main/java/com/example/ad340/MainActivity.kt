package com.example.ad340

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnItemClickListener {

    private var viewModelForecastRepository = ForecastRepository()

    // Convert Fahrenheit (°F) to Celsius (°C)
    // (32°F − 32) × 5/9 = 0°C

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Block Screen Shoot
        //window.setFlags(WindowManager.LayoutParams.FLAG_SECURE,WindowManager.LayoutParams.FLAG_SECURE)

        viewModelForecastRepository = ViewModelProvider(this)[ForecastRepository::class.java]

        val zipcodeEditText = findViewById<EditText>(R.id.zipcodeEditText)
        val enterButton = findViewById<Button>(R.id.enterButton)

        enterButton.setOnClickListener {
            val zipcode = zipcodeEditText.text.toString()
            if (zipcode.length != 5) {
                Toast.makeText(this, R.string.zipcode_entry_error, Toast.LENGTH_SHORT).show()
            } else {
                viewModelForecastRepository.loadForecast(zipcode)
            }
        }


        val forecastList = findViewById<RecyclerView>(R.id.forecastList)
        viewModelForecastRepository.weeklyForecast.observe(this) {
            // update our list adapter
            forecastList.layoutManager = LinearLayoutManager(this)
            val recyclerViewAdapter = DailyForecastAdapter(it)
            forecastList.adapter = recyclerViewAdapter
            recyclerViewAdapter.onItemClickListener = this
        }


    }

    override fun onItemClick(dailyForecast: DailyForecast) {
        Toast.makeText(
            applicationContext,
            getString(
                R.string.forecast_clicked_format,
                dailyForecast.temp,
                dailyForecast.description
            ),
            Toast.LENGTH_SHORT
        ).show()
    }
}