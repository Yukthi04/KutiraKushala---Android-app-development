package com.example.kutirakushala

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kutirakushala.databinding.ActivityProfileBinding
import com.example.kutirakushala.models.BusinessProfile
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // initialize View Binding to access UI elements
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Save button listener
        binding.btnSaveProfile.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        // Collect data from Input fields
        val name = binding.etBusinessName.text.toString()
        val owner = binding.etOwnerName.text.toString()
        val skill = binding.etSkillArea.text.toString()
        val loc = binding.etLocation.text.toString()
        val phone = binding.etPhone.text.toString()
        val cap = binding.etCapacity.text.toString()

        // Simple validation
        if (name.isEmpty() || owner.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create data model object
        val profile = BusinessProfile(
            id = "default_profile",
            businessName = name,
            ownerName = owner,
            skillArea = skill,
            location = loc,
            phone = phone,
            capacity = cap
        )

        // Save to Firestore under 'profiles' collection
        db.collection("profiles").document("default_profile")
            .set(profile)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Saved Successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
