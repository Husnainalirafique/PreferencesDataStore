package com.example.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val dataStore: DataStore<Preferences> by preferencesDataStore("key")
    private val loginKey = booleanPreferencesKey("login")
    private val scope = lifecycleScope
    private val tag = "MyTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.btnClick.setOnClickListener {
            scope.launch(Dispatchers.IO) {
                setLoginDetails(true)
            }
        }
        scope.launch {
            getLoginDetails().collect { loginStatus ->
                if (loginStatus) {
                    Log.d(tag, "Set to True")
                } else {
                    Log.d(tag, "Set to false")
                }
            }
        }
    }

    private suspend fun setLoginDetails(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[loginKey] = value
        }
    }

    private fun getLoginDetails(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[loginKey] ?: false
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}