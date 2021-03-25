package com.cjy.skinenginelib

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.ArrayMap
import android.view.LayoutInflater
import androidx.core.view.LayoutInflaterCompat
import com.cjy.skinenginelib.util.SkinThemeUtils
import java.lang.reflect.Field
import java.util.*


class ApplicationActivityLifecycle : Application.ActivityLifecycleCallbacks {

    private var observable: Observable? = null
    private val layoutInflaterFactories: ArrayMap<Activity, SkinLayoutInflaterFactory> = ArrayMap()

    constructor(observable: Observable) {
        this.observable = observable
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        /**
         *  更新状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity);
        /**
         * 更新布局视图
         */
        //获得Activity的布局加载器
        val layoutInflater = activity.layoutInflater
        try {
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false 版本支持最多28
            val field: Field = LayoutInflater::class.java.getDeclaredField("mFactorySet")
            field.isAccessible = true
            field.setBoolean(layoutInflater, false)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val skinLayoutInflaterFactory = SkinLayoutInflaterFactory(activity)
        LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory)
        layoutInflaterFactories[activity] = skinLayoutInflaterFactory
        observable?.addObserver(skinLayoutInflaterFactory)
    }

    override fun onActivityStarted(activity: Activity) {

    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        val observer: SkinLayoutInflaterFactory? = layoutInflaterFactories.remove(activity)
        SkinManager.getInstance().deleteObserver(observer)
    }
}