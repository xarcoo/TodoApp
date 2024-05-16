package com.ubaya.todoapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ubaya.todoapp.R
import com.ubaya.todoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}