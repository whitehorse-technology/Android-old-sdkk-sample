package com.dengage.android.kotlin.nawaz.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.ui.base.BaseActivity

class InAppTestActivity2 : BaseActivity() {


    lateinit var tvName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_test2)

        tvName = findViewById(R.id.textView)
        tvName.text = javaClass.name
    }
}