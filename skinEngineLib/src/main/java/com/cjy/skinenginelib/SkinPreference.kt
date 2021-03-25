package com.cjy.skinenginelib

import android.content.Context
import android.content.SharedPreferences

class SkinPreference {
    private var preference:SharedPreferences
    companion object{
        private val SKIN_SHARED:String = "skins"
        private val KEY_SKIN_PATH = "skin_path"
        @Volatile
        private var instance:SkinPreference? = null
        fun init(context: Context){
            if (null== instance){
                synchronized(SkinPreference::class){
                    instance = SkinPreference(context.applicationContext)
                }
            }
        }
        fun getInstance():SkinPreference{
            return instance!!
        }
    }

    private constructor(context: Context){
        preference = context.getSharedPreferences(SKIN_SHARED,Context.MODE_PRIVATE)
    }

    fun setSkin(skinPath:String){
        preference.edit().putString(KEY_SKIN_PATH,skinPath).apply()
    }

    fun reset(){
        preference.edit().remove(KEY_SKIN_PATH).apply()
    }

    fun getSkin():String{
        return preference.getString(KEY_SKIN_PATH,"").toString()
    }
}