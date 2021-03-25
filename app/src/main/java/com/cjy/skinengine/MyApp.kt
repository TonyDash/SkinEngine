package com.cjy.skinengine

import android.app.Application
import com.cjy.skinenginelib.SkinManager

class MyApp: Application() {

    override fun onCreate() {
        super.onCreate()
        SkinManager.init(this)
    }
}