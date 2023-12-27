package com.example.pract2_2

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.pract2_2.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var login: EditText
    lateinit var password: EditText
    lateinit var shar: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        login = findViewById(R.id.login)
        password = findViewById(R.id.password)
        shar = getSharedPreferences("data", MODE_PRIVATE)
        var correctlogin = shar.getString("login", null)
        var correctpassword = shar.getString("password", null)
        if (correctlogin != null && correctpassword != null)
        {
            login.setText(correctlogin)
            password.setText(correctpassword)
        }
    }

    fun toScreen(view: View) {
        var log = login.text.toString()
        var pass = password.text.toString()
        shar = getSharedPreferences("data", MODE_PRIVATE)
        if (log.isEmpty() || pass.isEmpty())
        {
            Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show()
        }
        else if (pass.length < 5)
        {
            Toast.makeText(this, "Для ввода пароля минимум 5 символов", Toast.LENGTH_SHORT).show()
        }
        else {
            shar = getSharedPreferences("data", MODE_PRIVATE)
            var correctlogin = shar.getString("login", null)
            var correctpassword = shar.getString("password", null)
            if (correctlogin != null && correctpassword != null) {
                if (correctlogin == log && correctpassword == pass) {
                    val intent = Intent(this, MainScreen::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Неверный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            } else {
                var edit = shar.edit()
                edit.putString("login", log)
                edit.putString("password", pass)
                edit.apply()
            }
        }
    }
}