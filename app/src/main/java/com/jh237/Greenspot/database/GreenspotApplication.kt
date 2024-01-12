package com.jh237.Greenspot.database

import android.app.Application

class GreenspotApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SpotRepo.initialize(this)
    }
}
