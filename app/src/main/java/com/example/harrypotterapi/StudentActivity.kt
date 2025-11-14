package com.example.harrypotterapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentActivity : AppCompatActivity() {
    private lateinit var textViewResult: TextView
    private lateinit var editTextId: EditText
    private lateinit var buttonSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var charApi: CharacterApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student)

        textViewResult = findViewById(R.id.textViewResultStudent)
        editTextId = findViewById(R.id.editTextId)
        buttonSearch = findViewById(R.id.buttonSearch)
        progressBar = findViewById(R.id.progressBarStudent)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        charApi = retrofit.create(CharacterApi::class.java)

        buttonSearch.setOnClickListener {
            val characterId = editTextId.text.toString()
            if (characterId.isNotEmpty()) {
                fetchCharacterById(characterId)
            } else {
                textViewResult.text = "Por favor, insira um ID."
            }
        }
    }

    private fun fetchCharacterById(charId: String) {
        lifecycleScope.launch {
            try {
                buttonSearch.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                Log.e("StudentActivity", "Fetching character with ID: $charId")
                val characters = withContext(Dispatchers.IO) {
                    charApi.getCharById(charId)
                }

                var result = ""
                for (char in characters) {
                    result += "Name: ${char.name}\nHouse: ${char.house}\n"
                }
                textViewResult.text = result
            } catch (e: Exception) {
                Log.e("StudentActivity", "Erro ao obter personagem: ${e.message}")
                textViewResult.text = "Personagem não encontrado ou erro na rede."
            } finally {
                progressBar.visibility = View.GONE
                buttonSearch.visibility = View.VISIBLE
            }
        }
    }
}

