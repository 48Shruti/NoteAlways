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
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val sideAnimation = AnimationUtils.loadAnimation(this,R.anim.icon)
        binding.imglogo.startAnimation(sideAnimation)
        Handler(Looper.getMainLooper()).postDelayed({
            binding.btnstart.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
           finish()
            }
        },2000)

    }
}