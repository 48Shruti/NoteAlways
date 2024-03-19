package com.shruti.notealways

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.shruti.notealways.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {
    lateinit var binding : ActivitySplashScreenBinding
    lateinit var logoImage :ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivitySplashScreenBinding.inflate(layoutInflater)//initialization of binding//
        setContentView(binding.root)
        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.icon)
        binding.imglogo.startAnimation(sideAnimation)
        binding.btnstart.setOnClickListener {
        Handler(Looper.getMainLooper()).postDelayed({
            var intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            this.finish()
        }, 100)
        }

    }
}