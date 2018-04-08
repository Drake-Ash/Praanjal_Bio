package com.demo.ash.demoapp

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import kotlinx.android.synthetic.main.login_activity.*
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.login.*
import kotlinx.android.synthetic.main.register.*
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    lateinit var mAuth: FirebaseAuth
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    var usercheck = 0
    var phnum = ""
    var verificationId = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.get_started)

        window.setBackgroundDrawableResource(R.drawable.background)

        //supportActionBar?.hide()
        /*

        tabHost.setup()

        //Login tab
        var logintab = tabHost.newTabSpec("login")
        logintab.setContent(R.id.login)
        logintab.setIndicator("Login")
        tabHost.addTab(logintab)

        //Register tab
        var regtab = tabHost.newTabSpec("register")
        regtab.setContent(R.id.register)
        regtab.setIndicator("Register")
        tabHost.addTab(regtab)

        */

        mAuth = FirebaseAuth.getInstance()
/*
        RegViewButton.setOnClickListener{ view: View->
            login.visibility = View.GONE
            register.visibility = View.VISIBLE
        }

        reg.setOnClickListener{
            view: View? ->progress.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
            progress1.visibility = View.VISIBLE

            var count = 0

            if(fname.text.isEmpty()){
                fname.error = "Enter your phone number"
                count = 1
            }
            if(lname.text.isEmpty()){
                lname.error = "Enter your phone number"
                count = 1
            }
            if(username.text.isEmpty()){
                username.error = "Enter your phone number"
                count = 1
            }
            if(phonenum.text.isEmpty()){
                phonenum.error = "Enter your phone number"
                count = 1
            }
            if(address.text.isEmpty()){
                address.error = "Enter your phone number"
                count = 1
            }
            if(postal_code.text.isEmpty()){
                postal_code.error = "Enter your phone number"
                count = 1
            }

            if(count == 0) {
                usercheck = 0
                phnum = phonenum.text.toString()
                verify()
            }
        }
        loginButton.setOnClickListener{
            view: View? ->progress.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
            progress1.visibility = View.VISIBLE

            var count = 0

            if(PhoneNum.text.isEmpty()){
                PhoneNum.error = "Enter your phone number"
                count = 1
            }
            if(count == 0) {
                usercheck = 1
                phnum = PhoneNum.text.toString()
                verify()
            }
        }
  */
    }

    private fun verificationCallbacks () {
        mCallbacks = object: PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                progress.visibility = View.INVISIBLE
                signIn(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException?) {
                toast("Cannot login, try again")
            }

            override fun onCodeSent(verfication: String?, p1: PhoneAuthProvider.ForceResendingToken?) {
                super.onCodeSent(verfication, p1)
                verificationId = verfication.toString()
                progress.visibility = View.INVISIBLE
            }

        }
    }


    private fun verify () {

        verificationCallbacks()

        phnum = "+91" + phnum

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phnum,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks
        )
    }

    private fun signIn (credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener {
                    task: Task<AuthResult> ->
                    if (task.isSuccessful) {
                        toast("Logged in Successfully :)")
                        if(usercheck == 0) {
                            val appDatabase = FirebaseDatabase.getInstance().getReference("Users")
                            val userId = appDatabase.push().child(phonenum.text.toString().trim())
                            val user = User(phonenum = phonenum.text.toString().trim(),
                                    fname = fname.text.toString().trim(),
                                    lname = lname.text.toString().trim(),
                                    username = username.text.toString().trim(),
                                    address = address.text.toString().trim(),
                                    postal_code = postal_code.text.toString().trim())
                            appDatabase.child("+91"+phonenum.text.toString().trim()).setValue(user).addOnCompleteListener {
                                toast("added user to database")
                            }
                            //val client = appDatabase.child(phonenum.text.toString().trim())
                            //client.child("patient").setValue(Client(fname = "ashwin",
                            //        lname = "raaaman"))
                        }
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
    }

    private fun toast (msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun authenticate () {
        val verifiNo = verifiTxt.text.toString()

        val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, verifiNo)

        signIn(credential)
    }

}
