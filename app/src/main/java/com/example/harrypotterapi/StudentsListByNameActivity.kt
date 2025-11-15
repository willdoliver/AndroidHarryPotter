package com.example.harrypotterapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentsListByNameActivity : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var editTextId: EditText
    private lateinit var buttonSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var charApi: CharacterApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_list_by_name)

        textViewResult = findViewById(R.id.textViewResultStudentsByName)
        editTextId = findViewById(R.id.editTextStudentsByName)
        buttonSearch = findViewById(R.id.buttonSearchStudentsByName)
        progressBar = findViewById(R.id.progressBarStudentsByName)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        charApi = retrofit.create(CharacterApi::class.java)

        buttonSearch.setOnClickListener {
            val name = editTextId.text.toString()
            if (name.isNotEmpty()) {
                fetchCharactersByName(name)
            } else {
                textViewResult.text = "Por favor, insira um nome."
            }
        }
    }

    private fun fetchCharactersByName(name: String) {
        lifecycleScope.launch {
            try {
                buttonSearch.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                Log.e("StudentsListByNameActivity", "Fetching characters with name: $name")
                val characters = withContext(Dispatchers.IO) {
                    charApi.getAllCharacters()
                }

                var result = ""
                for (char in characters) {
                    if (char.name.contains(name, ignoreCase = true)) {
                        result += "Name: ${char.name}\n"
                    }
                }

                if (result.isEmpty()) {
                    result = "Personagens não encontrados."
                }

                textViewResult.text = result
            } catch (e: Exception) {
                Log.e("StudentsListByNameActivity", "Erro ao obter personagens: ${e.message}")
                textViewResult.text = "Personagens não encontrados ou erro na rede."
            } finally {
                progressBar.visibility = View.GONE
                buttonSearch.visibility = View.VISIBLE
            }
        }
    }
}