package com.example.datastore.datastore.storingobjects

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.datastore.R
import com.example.datastore.ui.dataStore
import com.example.datastore.databinding.ActivityMain2Binding
import com.example.datastore.datastore.keys.PrefKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class StoringObjects : AppCompatActivity() {
    private lateinit var binding: ActivityMain2Binding
    private val scope = lifecycleScope
    private val tag = "MyTag"
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

    private fun displayUserData() {
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