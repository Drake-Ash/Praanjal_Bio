package com.demo.ash.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_update_user.*
import java.util.*
import kotlin.collections.HashMap

class UpdateUser : AppCompatActivity() {
    lateinit var databaseref : DatabaseReference
    lateinit var ClientsList: MutableMap<String, UserRefToClient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)

        val user = FirebaseAuth.getInstance().getCurrentUser()
        databaseref = FirebaseDatabase.getInstance().getReference("Users")
        databaseref = databaseref.child(user!!.phoneNumber.toString().trim())

        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val u = p0.getValue(User::class.java)

                    ClientsList = HashMap()
                    ClientsList.clear()

                    for(c in p0.child("Clients").children) {
                        val client = c.getValue(UserRefToClient::class.java)
                        ClientsList[client!!.client] = client!!
                    }

                    username.setText(u?.username)
                    fname.setText(u?.fname)
                    lname.setText(u?.lname)
                    postal_code.setText(u?.postal_code)
                    phonenum.setText(u?.phonenum)
                    address.setText(u?.address)

                    //Clients =
                }
            }

        })

        update.setOnClickListener {
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
                Update()
            }
        }
    }

    fun Update(){
        val appDatabase = FirebaseDatabase.getInstance().getReference("Users")
        /*var updateMap: MutableMap<String, Any> = HashMap()

        updateMap.put("phonenum" , phonenum.text.toString().trim())
        updateMap.put("fname", fname.text.toString().trim())
        updateMap.put("lname", lname.text.toString().trim())
        updateMap.put("username", username.text.toString().trim())
        updateMap.put("address", address.text.toString().trim())
        updateMap.put("postal_code", postal_code.text.toString().trim())
        updateMap.put("Clients", ClientsList)

        var updateList: MutableMap<String, Any> = HashMap()

        updateList.put(phonenum.text.toString().trim(), updateMap)
        */

        appDatabase.child("+91"+phonenum.text.toString().trim()).child("phonenum").setValue(phonenum.text.toString().trim())
        appDatabase.child("+91"+phonenum.text.toString().trim()).child("fname").setValue(fname.text.toString().trim())
        appDatabase.child("+91"+phonenum.text.toString().trim()).child("lname").setValue(lname.text.toString().trim())
        appDatabase.child("+91"+phonenum.text.toString().trim()).child("username").setValue(username.text.toString().trim())
        appDatabase.child("+91"+phonenum.text.toString().trim()).child("postal_code").setValue(postal_code.text.toString().trim())
        appDatabase.child("+91"+phonenum.text.toString().trim()).child("address").setValue(address.text.toString().trim())


        //appDatabase.updateChildren(updateList)
        val user = FirebaseAuth.getInstance().currentUser

        //update user phone number.... IMP, re-authentication required

        //appDatabase.child("Clients").setValue()
        finish()
        //child(phonenum.text.toString().trim()).up
    }

}
