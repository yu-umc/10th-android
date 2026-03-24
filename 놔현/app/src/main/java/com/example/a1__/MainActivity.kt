package com.example.a1__

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.a1__.databinding.ActivityMainBinding
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgHappy.setOnClickListener {
            resetColors()
            binding.tvHappy.setTextColor(Color.parseColor("#FFB300"))
        }

        binding.imgExcited.setOnClickListener {
            resetColors()
            binding.tvExcited.setTextColor(Color.parseColor("#42A5F5"))
        }

        binding.imgNormal.setOnClickListener {
            resetColors()
            binding.tvNormal.setTextColor(Color.parseColor("#78909C"))
        }

        binding.imgAnxious.setOnClickListener {
            resetColors()
            binding.tvAnxious.setTextColor(Color.parseColor("#AB47BC"))
        }

        binding.imgAngry.setOnClickListener {
            resetColors()
            binding.tvAngry.setTextColor(Color.parseColor("#EF5350"))
        }
    }

    private fun resetColors() {
        val defaultColor = Color.BLACK
        binding.tvHappy.setTextColor(defaultColor)
        binding.tvExcited.setTextColor(defaultColor)
        binding.tvNormal.setTextColor(defaultColor)
        binding.tvAnxious.setTextColor(defaultColor)
        binding.tvAngry.setTextColor(defaultColor)
    }
}