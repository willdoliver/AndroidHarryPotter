package com.example.harrypotterapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TeachersListActivity : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var charApi: CharacterApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_teachers_list)

        textViewResult = findViewById(R.id.textViewResultTeachers)
        progressBar = findViewById(R.id.progressBarTeachers)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        charApi = retrofit.create(CharacterApi::class.java)

        fetchCharacters()
    }

    private fun fetchCharacters() {
        lifecycleScope.launch {
            try {
                progressBar.visibility = View.VISIBLE

                Log.e("TeachersListActivity", "Buscando professores")
                val characters = withContext(Dispatchers.IO) {
                    charApi.getStaffs()
                }

                var result = ""
                for (char in characters) {
                    result += "Name: ${char.name}\n"
                }
                textViewResult.text = result
            } catch (e: Exception) {
                Log.e("TeachersListActivity", "Erro ao obter professores: ${e.message}")
                textViewResult.text = "Erro ao obter professores."
            } finally {
                progressBar.visibility = View.GONE
            }
        }
    }
}