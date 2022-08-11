package com.dengage.android.kotlin.nawaz.ui.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.navigation.findNavController
import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    lateinit var tvName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewFromChild(R.layout.activity_main)
        //DengageManager.getInstance(this).setNavigation(this@MainActivity)
        tvName = findViewById(R.id.textView)
        tvName.text = javaClass.name

        Log.d("oops","$intent");

        val bundle = intent.extras
        if (bundle != null) {
            for (key in bundle.keySet()) {
                Log.d("oops", key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean =
        findNavController(R.id.navigationHostFragment).navigateUp()

}