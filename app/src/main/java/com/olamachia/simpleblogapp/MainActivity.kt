package com.olamachia.simpleblogapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.olamachia.simpleblogapp.mvvm.ui.MVVMActivity

class MainActivity : AppCompatActivity() {

    private lateinit var mvvmLaunchButton:Button
    private lateinit var mvcLaunchButton:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mvvmLaunchButton = findViewById(R.id.mvvm_button)
        mvcLaunchButton =findViewById(R.id.mvc_button)

        mvvmLaunchButton.setOnClickListener {
             val mvvmIntent = Intent(this@MainActivity,MVVMActivity::class.java)
            startActivity(mvvmIntent)}

        mvcLaunchButton.setOnClickListener {
            val mvcIntent = Intent(this@MainActivity,MVVMActivity::class.java)
            startActivity(mvcIntent)}
    }

}