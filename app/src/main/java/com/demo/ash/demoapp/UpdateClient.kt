package com.demo.ash.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_update_client.*

class UpdateClient : AppCompatActivity() {
    lateinit var databaseref : DatabaseReference
    lateinit var PatientsList: MutableMap<String, Patient>
    lateinit var user: FirebaseUser
    lateinit var client: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_client)

        client = intent.getStringExtra("clientID")
        user = FirebaseAuth.getInstance().getCurrentUser()!!
        databaseref = FirebaseDatabase.getInstance().getReference("Clients").child(client)

        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val u = p0.getValue(Client::class.java)
                    PatientsList = HashMap()
                    PatientsList.clear()

                    for(c in p0.child("Patients").children) {
                        val p = c.getValue(Patient::class.java)
                        PatientsList.put(c.key, p!!)
                    }

                    cfname.setText(u?.cfname)
                    clname.setText(u?.clname)
                    cpostal_code.setText(u?.cpostal_code)
                    cphonenum.setText(u?.cphonenum)
                    caddress.setText(u?.caddress)
                }
            }

        })

        cupdate.setOnClickListener {
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
                Update()
            }
        }
    }

    fun Update(){
        val appDatabase = FirebaseDatabase.getInstance().getReference("Clients")
                .child(client)

        appDatabase.child("cphonenum").setValue(cphonenum.text.toString().trim())
        appDatabase.child("cfname").setValue(cfname.text.toString().trim())
        appDatabase.child("clname").setValue(clname.text.toString().trim())
        appDatabase.child("caddress").setValue(caddress.text.toString().trim())
        appDatabase.child("cpostal_code").setValue(cpostal_code.text.toString().trim())

        finish()
        //child(phonenum.text.toString().trim()).up
        //.child(user.phoneNumber.toString().trim()).child("Clients")
    }
}

