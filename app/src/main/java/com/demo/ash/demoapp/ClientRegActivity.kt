package com.demo.ash.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_client_reg.*

class ClientRegActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_reg)

        creg.setOnClickListener {
            view: View? ->cprogress.visibility = View.VISIBLE

            var count = 0

            if(cfname.text.isEmpty()){
                cfname.error = "Enter your phone number"
                count = 1
            }
            if(clname.text.isEmpty()){
                clname.error = "Enter your phone number"
                count = 1
            }
            if(cphonenum.text.isEmpty()){
                cphonenum.error = "Enter your phone number"
                count = 1
            }
            if(caddress.text.isEmpty()){
                caddress.error = "Enter your phone number"
                count = 1
            }
            if(cpostal_code.text.isEmpty()){
                cpostal_code.error = "Enter your phone number"
                count = 1
            }

            if(count == 0) {
                store_into_DB()
            }
        }
    }

    private fun store_into_DB() {
        var user = FirebaseAuth.getInstance().getCurrentUser()
        val client = Client(
                cphonenum = cphonenum.text.toString().trim(),
                cfname = cfname.text.toString().trim(),
                clname = clname.text.toString().trim(),
                caddress = caddress.text.toString().trim(),
                cpostal_code = cpostal_code.text.toString().trim()
        )

        var appDatabase = FirebaseDatabase.getInstance().getReference("Clients")
        var temp = appDatabase.push().key
        appDatabase.child(temp).setValue(client)
        appDatabase.child(temp).child("Users").child(user!!.phoneNumber)
                .setValue(ClientReftoUser(user.phoneNumber!!))

        appDatabase = FirebaseDatabase.getInstance().getReference("Users")
        val clientadd = appDatabase.child(user!!.phoneNumber)
                .child("Clients").child(temp).setValue(UserRefToClient(temp))

        finish()
    }

}
