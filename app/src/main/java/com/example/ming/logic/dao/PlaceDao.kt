package com.example.ming.logic.dao

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import com.example.ming.MingApplication
import com.example.ming.logic.network.Place
import com.google.gson.Gson

object PlaceDao {
    fun savePlace(place: Place) {
        sharedPreferences().edit {
            putString("place", Gson().toJson(place))
        }
    }

    fun getSavedPlace(): Place {
        val placeJson = sharedPreferences().getString("place", "")
        return Gson().fromJson(placeJson, Place::class.java)
    }

    fun isPlaceSaved() = sharedPreferences().contains("place")

    private fun sharedPreferences() = MingApplication.context.getSharedPreferences("ming", Context.MODE_PRIVATE)
}