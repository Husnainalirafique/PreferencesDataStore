package com.example.datastore

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.example.datastore.databinding.ActivityMain2Binding
import com.example.datastore.datastore.PrefKeys
import com.example.datastore.datastore.UserData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.math.log

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("key")

class MainActivity2 : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val scope = lifecycleScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main2)
        val data = UserData("Husnain", 22)
        scope.launch(Dispatchers.IO) {
            saveUserData(data)
        }

        displayUserData()

    }


    private suspend fun saveUserData(userData: UserData) {
        val userJsonData = Json.encodeToString(userData)
        dataStore.edit {
            it[PrefKeys.USER_DATA_KEY] = userJsonData
        }
    }

    private  fun displayUserData() {
        dataStore.data.map { preferences ->
            val userDataJson = preferences[PrefKeys.USER_DATA_KEY]
            val userData = userDataJson?.let {
                Json.decodeFromString<UserData>(it)
            }
            Log.d("MyTag", "Name: ${userData?.name}, Age: ${userData?.age}")
        }.launchIn(scope)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}