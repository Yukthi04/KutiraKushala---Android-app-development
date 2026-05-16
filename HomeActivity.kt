package com.example.kutirakushala

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakushala.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.cardProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }

        binding.cardAddProduct.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }

        binding.cardViewProducts.setOnClickListener {
            startActivity(Intent(this, ProductListActivity::class.java))
        }

        binding.cardUpdateCapacity.setOnClickListener {
            startActivity(Intent(this, CapacityUpdateActivity::class.java))
        }
    }
}
