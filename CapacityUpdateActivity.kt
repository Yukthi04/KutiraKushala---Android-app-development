package com.example.kutirakushala

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakushala.databinding.ActivityCapacityUpdateBinding
import com.google.firebase.firestore.FirebaseFirestore

class CapacityUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCapacityUpdateBinding
    private val db = FirebaseFirestore.getInstance()
    private var ownerPhone = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCapacityUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fetchProfile()

        binding.btnUpdateCapacity.setOnClickListener {
            updateCapacity()
        }

        binding.btnCallOwner.setOnClickListener {
            if (ownerPhone.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$ownerPhone")
                startActivity(intent)
            } else {
                Toast.makeText(this, "Phone number not found", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchProfile() {
        db.collection("profiles").document("default_profile")
            .get()
            .addOnSuccessListener { doc ->
                ownerPhone = doc.getString("phone") ?: ""
            }
    }

    private fun updateCapacity() {
        val msg = binding.etCapacityMessage.text.toString()
        if (msg.isEmpty()) return

        db.collection("profiles").document("default_profile")
            .update("currentCapacityStatus", msg)
            .addOnSuccessListener {
                Toast.makeText(this, "Status Updated", Toast.LENGTH_SHORT).show()
                
                // Update all products' status too (or you could fetch dynamically in listing)
                updateProductStatuses(msg)
            }
    }

    private fun updateProductStatuses(msg: String) {
        db.collection("products").get().addOnSuccessListener { result ->
            for (doc in result) {
                db.collection("products").document(doc.id).update("capacityStatus", msg)
            }
            finish()
        }
    }
}
