package com.dengage.android.kotlin.nawaz.ui.base;

import android.content.Context
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log
import com.dengage.sdk.DengageManager

abstract class BaseActivity : AppCompatActivity() {
    var context: Context? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = this

    }


    fun setContentViewFromChild(layoutResID: Int) {
        super.setContentView(layoutResID)

    }

    override fun onResume() {
        super.onResume()
        this.let { context ->
            (context as AppCompatActivity).let {
                DengageManager.getInstance(applicationContext).setNavigation(it, it.javaClass.name)
                Log.d("oops", "" + it.javaClass.name)
            }
        }
    }

}