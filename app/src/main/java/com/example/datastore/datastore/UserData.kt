package com.example.datastore.datastore

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val name: String,
    val age: Int
)
