package com.example.harrypotterapi

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class StudentsListActivity : AppCompatActivity() {

    private lateinit var textViewResult: TextView
    private lateinit var radioGroupHouses: RadioGroup
    private lateinit var buttonSearch: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var charApi: CharacterApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_students_list)

        textViewResult = findViewById(R.id.textViewResultStudents)
        radioGroupHouses = findViewById(R.id.radioGroupHouses)
        buttonSearch = findViewById(R.id.buttonSearchByHouse)
        progressBar = findViewById(R.id.progressBarStudents)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://hp-api.onrender.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        charApi = retrofit.create(CharacterApi::class.java)

        buttonSearch.setOnClickListener {
            val selectedRadioButtonId = radioGroupHouses.checkedRadioButtonId

            if (selectedRadioButtonId == -1) {
                Toast.makeText(this, "Por favor, selecione uma opção.", Toast.LENGTH_SHORT).show()
            } else {
                val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
                val houseName = selectedRadioButton.text.toString()
                fetchCharactersByHouse(houseName)
            }
        }
    }

    private fun fetchCharactersByHouse(house: String) {
        lifecycleScope.launch {
            try {
                buttonSearch.visibility = View.GONE
                progressBar.visibility = View.VISIBLE

                Log.e("StudentsListActivity", "Fetching characters in house: $house")
                val characters = withContext(Dispatchers.IO) {
                    charApi.getStudentsByHouse(house)
                }
                Log.e("StudentsListActivity", "Characters fetched: $characters")


                var result = ""
                for (char in characters) {
                    result += "Name: ${char.name}\n"
                }
                textViewResult.text = result
            } catch (e: Exception) {
                Log.e("StudentsListActivity", "Erro ao obter personagens: ${e.message}")
                textViewResult.text = "Personagens não encontrados ou erro na rede."
            } finally {
                progressBar.visibility = View.GONE
                buttonSearch.visibility = View.VISIBLE
            }
        }
    }

    private fun fetchAllCharactersOnce() {
        lifecycleScope.launch {
            try {
                buttonSearch.isEnabled = false
                progressBar.visibility = View.VISIBLE
                textViewResult.text = "Carregando todos os personagens..."

                // Chama a nova função da API
                val allCharacters = withContext(Dispatchers.IO) {
                    charApi.getAllCharacters()
                }

                // Avisa que o carregamento terminou
                textViewResult.text = "Pronto! Selecione uma casa e clique em buscar."
                Toast.makeText(this@StudentsListActivity, "Dados carregados.", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                Log.e("StudentsListActivity", "Erro ao carregar todos os personagens: ${e.message}")
                textViewResult.text = "Falha ao carregar dados da API. Verifique a conexão."
            } finally {
                // Libera o botão e esconde o progresso
                progressBar.visibility = View.GONE
                buttonSearch.isEnabled = true
            }
        }
    }

}