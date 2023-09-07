package com.example.datastore.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.R
import com.example.datastore.databinding.ActivityMainBinding
import com.example.datastore.datastore.keys.PrefKeys
import com.example.datastore.datastore.storingobjects.StoringObjects
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("key")

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val scope = lifecycleScope
    private val tag = "MyTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //Methods
        setOnClickListeners()
        workingWithCoroutines()
    }


    private fun setOnClickListeners() {
        binding.btnClick.setOnClickListener {
            scope.launch(Dispatchers.IO) {
                setLoginDetails()
            }
        }
        binding.btnActivity2.setOnClickListener {
            startActivity(Intent(this@MainActivity, StoringObjects::class.java))
        }
        binding.btnThemeSavingActivity.setOnClickListener {
            startActivity(Intent(this@MainActivity, ThemeActivity::class.java))
        }
    }

    private fun workingWithCoroutines(){
        scope.launch {
            getLoginDetails().collect {
                if (it) {
                    Log.d(tag, "Loged In")
                } else {
                    Log.d(tag, "Not Loged In")
                }
            }
        }
    }
    private suspend fun setLoginDetails() {
        dataStore.edit { preference ->
            preference[PrefKeys.LOGIN_KEY] = true
        }
    }

    private fun getLoginDetails(): Flow<Boolean> {
        return dataStore.data.map { preference ->
            preference[PrefKeys.LOGIN_KEY] ?: false
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}
