package com.cjy.skinengine

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.cjy.skinenginelib.SkinManager

class SecondActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val btnNew: Button = findViewById(R.id.btnSkinChange)
        val btnDefault: Button = findViewById(R.id.btnSkinDefault)
        btnNew.setOnClickListener(this)
        btnDefault.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btnSkinChange ->{
                change(v)
            }
            R.id.btnSkinDefault ->{
                restore(v)
            }
        }
    }

    private fun change(view: View?) {
        //换肤，收包裹，皮肤包是独立的apk包，可以来自网络下载
        SkinManager.getInstance().loadSkin("/data/data/com.cjy.skinengine/skin/skinapk-debug.apk")
    }

    private fun restore(view: View?) {
        SkinManager.getInstance().loadSkin("")
    }
}