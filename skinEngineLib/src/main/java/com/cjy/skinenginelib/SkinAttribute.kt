package com.cjy.skinenginelib

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.cjy.skinenginelib.util.SkinResources
import com.cjy.skinenginelib.util.SkinThemeUtils

class SkinAttribute {
    private val skinViews = arrayListOf<SkinView>()
    private val attributes = arrayListOf<String>()

    init {
        attributes.add("background")
        attributes.add("src")
        attributes.add("textColor")
        attributes.add("drawableLeft")
        attributes.add("drawableTop")
        attributes.add("drawableRight")
        attributes.add("drawableBottom")
    }

    /***
     * 记录view的属性，
     */
    fun recordViewAttr(view: View, attrs: AttributeSet){
        val skinPairs = mutableListOf<SkinPair>()
        //遍历view的属性
        for (i in 0 until attrs.attributeCount) {
            //获取属性名称
            val attributeName = attrs.getAttributeName(i)
            if (attributes.contains(attributeName)){//判断是否数据需要换肤的属性
                //获取属性的值
                val attributeValue = attrs.getAttributeValue(i)
                if (attributeValue.startsWith("#")){//开头是#，就是颜色值被写死了，直接跳过
                    continue
                }
                var resId:Int = 0
                if (attributeValue.startsWith("?")){//？开头，系统的属性
                    val attrId:Int = attributeValue.substring(1).toInt()
                    resId = SkinThemeUtils.getResId(view.context, intArrayOf(attrId))[0]
                }else{
                    //这里就是@开头的属性
                    resId = attributeValue.substring(1).toInt()
                }
                val skinPair = SkinPair(attributeName,resId)
                skinPairs.add(skinPair)
            }
        }
        if (skinPairs.isNotEmpty()||view is SkinViewSupport){
            val skinView = SkinView(view, skinPairs)
            //加载皮肤资源
            skinView.applySkin()
            skinViews.add(skinView)
        }
    }

    fun applySkin(){
        for (skinView in skinViews){
            skinView.applySkin()
        }
    }

    inner class SkinView {
        var view: View
        private val skinPairs = mutableListOf<SkinPair>()

        constructor(view: View, skinPairs: List<SkinPair>) {
            this.view = view
            this.skinPairs.addAll(skinPairs)
        }

        fun applySkin() {
            applySkinSupport()
            for (skinPair in skinPairs) {
                var left: Drawable? = null
                var top: Drawable? = null
                var right: Drawable? = null
                var bottom: Drawable? = null
                when (skinPair.attributeName) {
                    "background" -> {
                        val background = SkinResources.getInstance().getBackground(skinPair.resId)
                        if (background is Int) {
                            view.setBackgroundColor(background)
                        } else {
                            ViewCompat.setBackground(view, background as Drawable)
                        }
                    }
                    "src" -> {
                        val background = SkinResources.getInstance().getBackground(skinPair.resId)
                        if (background is Int) {
                            (view as ImageView).setImageDrawable(ColorDrawable(background))
                        } else {
                            (view as ImageView).setImageDrawable(background as Drawable)
                        }
                    }
                    "textColor" -> {
                        (view as TextView).setTextColor(
                            SkinResources.getInstance().getColorStateList(
                                skinPair.resId
                            )
                        )
                    }
                    "drawableLeft" -> {
                        left = SkinResources.getInstance().getDrawable(skinPair.resId)
                    }
                    "drawableTop" -> {
                        top = SkinResources.getInstance().getDrawable(skinPair.resId)
                    }
                    "drawableRight" -> {
                        right = SkinResources.getInstance().getDrawable(skinPair.resId)
                    }
                    "drawableBottom" -> {
                        bottom = SkinResources.getInstance().getDrawable(skinPair.resId)
                    }
                }
                if (left!=null||top!=null||right!=null||bottom!=null){
                    (view as TextView).setCompoundDrawablesWithIntrinsicBounds(
                        left,
                        top,
                        right,
                        bottom
                    )
                }
            }
        }

        private fun applySkinSupport() {
            if (view is SkinViewSupport) {
                (view as SkinViewSupport).applySkin()
            }
        }
    }

    inner class SkinPair {
        var attributeName: String = ""
        var resId: Int = 0

        constructor(attributeName: String, resId: Int) {
            this.attributeName = attributeName
            this.resId = resId
        }
    }
}