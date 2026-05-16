package com.example.kutirakushala

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakushala.databinding.ActivityAddProductBinding
import com.example.kutirakushala.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding
    private val db = FirebaseFirestore.getInstance()
    private var selectedCategory = "Food"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup dropdown for product categories
        setupSpinner()

        binding.btnAddProduct.setOnClickListener {
            saveProduct()
        }
    }

    private fun setupSpinner() {
        val categories = arrayOf("Food", "Craft", "Handicraft", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = adapter

        binding.spinnerCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedCategory = categories[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun saveProduct() {
        val name = binding.etProductName.text.toString()
        val priceStr = binding.etPrice.text.toString()
        val desc = binding.etDescription.text.toString()
        val img = binding.etImageUrl.text.toString()

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please fill name and price", Toast.LENGTH_SHORT).show()
            return
        }

        val price = priceStr.toDoubleOrNull() ?: 0.0
        val productId = db.collection("products").document().id

        // Fetch current capacity status from business profile to show it with the product
        db.collection("profiles").document("default_profile").get()
            .addOnSuccessListener { doc ->
                val status = doc.getString("currentCapacityStatus") ?: "Available"
                
                val product = Product(
                    id = productId,
                    name = name,
                    category = selectedCategory,
                    price = price,
                    description = desc,
                    imageUrl = img,
                    capacityStatus = status
                )

                // Save product to 'products' collection
                db.collection("products").document(productId)
                    .set(product)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Product Added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
            }
    }
}
