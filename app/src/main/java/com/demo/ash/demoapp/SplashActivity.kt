package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.*
import com.google.firebase.auth.FirebaseUser

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onStart(){
        super.onStart()
        Thread.sleep(2000)
        var user = FirebaseAuth.getInstance()
        if(user.currentUser!=null){
            startActivity(Intent(this, MainActivity::class.java))
        }
        else{
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
