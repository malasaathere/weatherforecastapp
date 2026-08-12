package com.example.weatherforecastapp

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val cityEditText = findViewById<EditText>(R.id.cityEditText)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val errorTextView = findViewById<TextView>(R.id.errorTextView)

        fun validateCity(): Boolean {
            val city = cityEditText.text.toString().trim()
            val isValid = city.isNotEmpty()

            cityEditText.error = if (isValid) null else getString(R.string.empty_city_error)
            errorTextView.text = if (isValid) "" else getString(R.string.empty_city_error)
            errorTextView.visibility = if (isValid) View.GONE else View.VISIBLE

            return isValid
        }

        searchButton.setOnClickListener {
            if (validateCity()) {
                // Member 3 will call the weather API here and display its response.
            }
        }

        cityEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchButton.performClick()
                true
            } else {
                false
            }
        }
    }
}
