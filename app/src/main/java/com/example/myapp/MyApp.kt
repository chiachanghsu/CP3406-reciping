package com.example.myapp

import android.app.Application
import com.example.myapp.data.Repository

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Repository.init(this)
    }
}
