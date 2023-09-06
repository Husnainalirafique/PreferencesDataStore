package com.example.datastore.datastore.keys

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PrefKeys {
    val LOGIN_KEY = booleanPreferencesKey("login")
    val USER_DATA_KEY = stringPreferencesKey("userdata")
}