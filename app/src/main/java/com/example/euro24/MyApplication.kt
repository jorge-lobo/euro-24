package com.example.euro24

import android.app.Application
import android.content.Context
import com.jakewharton.threetenabp.AndroidThreeTen

class MyApplication: Application() {

    companion object {
        private lateinit var sInstance: MyApplication
        //var database: AppDataBase? = null

        fun getAppContext(): Context {
            return sInstance.applicationContext
        }

        var userPermission = ""

        //val BASE_URL = ""
    }

    override fun onCreate() {
        super.onCreate()
        sInstance = this

        AndroidThreeTen.init(this)
    }
}