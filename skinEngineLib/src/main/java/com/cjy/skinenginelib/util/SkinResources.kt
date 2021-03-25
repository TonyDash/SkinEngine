package com.cjy.skinenginelib.util

import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import androidx.core.content.res.ResourcesCompat

class SkinResources {

    private lateinit var skinApkName: String
    private var isDefaultSkin = true

    private var appResources: Resources? = null
    private var skinResources: Resources? = null

    private constructor(context: Context) {
        appResources = context.resources
    }

    companion object {
        @Volatile
        private var instance: SkinResources? = null

        fun init(context: Context){
            if (instance == null) {
                synchronized(SkinResources::class) {
                    if (instance == null) {
                        instance = SkinResources(context)
                    }
                }
            }
        }

        fun getInstance():SkinResources{
            return instance!!
        }
    }

    fun reset() {
        skinResources = null
        skinApkName = ""
        isDefaultSkin = true
    }

    fun applySkin(resources: Resources, apkName: String) {
        skinResources = resources
        skinApkName = apkName
        isDefaultSkin = TextUtils.isEmpty(apkName)
    }

    /***
     * 根据原始APP的resId，获取其名称和类型
     * 以改名称和类型，从皮肤apk包里找同名称类型的resId
     */
    private fun getIdentifier(resId: Int): Int {
        if (isDefaultSkin) return resId
        appResources?.run {
            val resName = this.getResourceEntryName(resId)
            val resType = this.getResourceTypeName(resId)
            return skinResources!!.getIdentifier(resName, resType, skinApkName)
        }
        return 0
    }

    fun getColor(resId: Int): Int {
        if (isDefaultSkin) {
            appResources?.run {
                return ResourcesCompat.getColor(this, resId, null)
            }
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            appResources?.run {
                return ResourcesCompat.getColor(this, resId, null)
            }
        } else {
            skinResources?.run {
                return ResourcesCompat.getColor(this, resId, null)
            }
        }
        return 0
    }

    fun getColorStateList(resId: Int): ColorStateList? {
        if (isDefaultSkin) {
            appResources?.run {
                return ResourcesCompat.getColorStateList(this, resId, null)
            }
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            appResources?.run {
                return ResourcesCompat.getColorStateList(this, resId, null)
            }
        } else {
            skinResources?.run {
                return ResourcesCompat.getColorStateList(this, resId, null)
            }
        }
        return null
    }

    fun getDrawable(resId: Int): Drawable? {
        if (isDefaultSkin) {
            appResources?.run {
                return ResourcesCompat.getDrawable(this, resId, null)
            }
        }
        val skinId = getIdentifier(resId)
        if (skinId == 0) {
            appResources?.run {
                return ResourcesCompat.getDrawable(this, resId, null)
            }
        } else {
            skinResources?.run {
                return ResourcesCompat.getDrawable(this, resId, null)
            }
        }
        return null
    }

    fun getBackground(resId:Int):Any?{
        appResources?.run {
            val resourceTypeName = this.getResourceTypeName(resId)
            return if ("color" == resourceTypeName){
                this@SkinResources.getColor(resId)
            }else{
                this@SkinResources.getDrawable(resId)
            }
        }
        return null
    }
}