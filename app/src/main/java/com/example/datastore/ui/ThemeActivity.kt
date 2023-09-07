package com.example.datastore.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.example.datastore.R
import com.example.datastore.databinding.ActivityThemeBinding
import com.example.datastore.datastore.keys.PrefKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class ThemeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityThemeBinding
    private var scope = lifecycleScope
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_theme)

        scope.launch{
            settingTheme()
            gettingTheme()
        }

    }

    private suspend fun settingTheme(){
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
           when(isChecked){
                true -> {
                    binding.mainLayout.setBackgroundColor(resources.getColor(R.color.teal_700,null))
                }
                false -> {
                   setTheme(R.style.Theme_DataStore)
                    recreate()
               }
            }
            scope.launch(Dispatchers.IO){
                dataStore.edit {
                    it[PrefKeys.COLOR_KEY] = isChecked
                }
            }
        }
    }

    private fun gettingTheme(){
        dataStore.data.map {
            binding.switchTheme.isChecked = it[PrefKeys.COLOR_KEY] ?: false
        }.launchIn(scope)
    }
    override fun onDestroy() {
        super.onDestroy()
        binding.unbind()
    }
}