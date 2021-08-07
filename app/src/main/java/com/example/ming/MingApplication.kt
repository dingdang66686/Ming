package com.example.ming

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MingApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        //TODO: ADD TOKEN HERE
        const val TOKEN = ""
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}