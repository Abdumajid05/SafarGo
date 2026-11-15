package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.data.local.SafarGoDatabase
import com.example.data.local.UserPreferencesDataStore
import com.example.data.repository.FirestoreTravelRepository
import com.example.data.repository.SafarGoRepository
import com.example.ui.navigation.SafarGoApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = SafarGoDatabase.getInstance(applicationContext)
        val prefsDataStore = UserPreferencesDataStore(applicationContext)
        val localRepo = SafarGoRepository(
            bookingDao = database.bookingDao(),
            prefsDataStore = prefsDataStore
        )
        val firestoreRepo = FirestoreTravelRepository(applicationContext)

        setContent {
            SafarGoApp(
                firestoreRepo = firestoreRepo,
                localRepo = localRepo
            )
        }
    }
}
