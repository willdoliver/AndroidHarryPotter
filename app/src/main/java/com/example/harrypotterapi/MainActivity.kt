package com.example.harrypotterapi

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonListSpecificChar = findViewById<Button>(R.id.buttonListSpecificChar)
        val buttonListTeachers = findViewById<Button>(R.id.buttonListTeachers)
        val buttonListStudents = findViewById<Button>(R.id.buttonListStudents)

        buttonListSpecificChar.setOnClickListener {
            val intent = Intent(this, StudentActivity::class.java)
            startActivity(intent)
        }

        buttonListTeachers.setOnClickListener {
            val intent = Intent(this, TeachersListActivity::class.java)
            startActivity(intent)
        }

        buttonListStudents.setOnClickListener {
            val intent = Intent(this, StudentsListActivity::class.java)
            startActivity(intent)
        }

        val buttonExit = findViewById<Button>(R.id.buttonExit)
        buttonExit.setOnClickListener {
            finish()
        }

    }
}