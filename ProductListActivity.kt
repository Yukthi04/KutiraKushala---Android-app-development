package com.example.kutirakushala

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kutirakushala.adapters.ProductAdapter
import com.example.kutirakushala.databinding.ActivityProductListBinding
import com.example.kutirakushala.models.Product
import com.google.firebase.firestore.FirebaseFirestore

class ProductListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductListBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var adapter: ProductAdapter
    private var allProducts = listOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupFilter()
        fetchProducts()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(emptyList())
        binding.rvProducts.layoutManager = LinearLayoutManager(this)
        binding.rvProducts.adapter = adapter
    }

    private fun setupFilter() {
        val categories = arrayOf("All", "Food", "Craft", "Handicraft")
        val filterAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        filterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilter.adapter = filterAdapter

        binding.spinnerFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val filter = categories[position]
                if (filter == "All") {
                    adapter.updateData(allProducts)
                } else {
                    val filtered = allProducts.filter { it.category == filter }
                    adapter.updateData(filtered)
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun fetchProducts() {
        binding.progressBar.visibility = View.VISIBLE
        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                binding.progressBar.visibility = View.GONE
                allProducts = result.toObjects(Product::class.java)
                adapter.updateData(allProducts)
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error fetching: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
