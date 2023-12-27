package com.example.pract2_2

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.pract2_2.databinding.ActivityMainScreenBinding

class MainScreen : Activity() {

    private lateinit var binding: ActivityMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    fun add(view: View) {
        val intent = Intent(this,AddActivity::class.java)
        startActivity(intent)
    }
    fun history(view: View) {
        val intent = Intent(this,HistoryList::class.java)
        startActivity(intent)
    }
}