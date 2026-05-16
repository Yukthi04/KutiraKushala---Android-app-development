package com.example.kutirakushala.models

data class BusinessProfile(
    val id: String = "",
    val businessName: String = "",
    val ownerName: String = "",
    val skillArea: String = "",
    val location: String = "",
    val phone: String = "",
    val capacity: String = "",
    val currentCapacityStatus: String = "Normal"
)
