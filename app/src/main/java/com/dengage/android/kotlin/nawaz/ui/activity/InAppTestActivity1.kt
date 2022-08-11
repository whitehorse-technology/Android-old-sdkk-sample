package com.dengage.android.kotlin.nawaz.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.dengage.android.kotlin.nawaz.R
import com.dengage.android.kotlin.nawaz.ui.base.BaseActivity

class InAppTestActivity1 : BaseActivity() {

    lateinit var tvName: TextView
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_app_test1)

        tvName = findViewById(R.id.textView)
        tvName.text = javaClass.name
        button = findViewById(R.id.button);
        button.setOnClickListener {

            startActivity(Intent(this@InAppTestActivity1, InAppTestActivity2::class.java))
        }


    }
}