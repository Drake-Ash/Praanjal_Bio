package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.demo.ash.demoapp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_patient_list.*

class PatientListActivity : AppCompatActivity() {

    lateinit var client: String

    lateinit var databaseref: DatabaseReference
    lateinit var patientList: MutableList<Patient>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_list)

        client = intent.getStringExtra("clientID")


        var user = FirebaseAuth.getInstance().getCurrentUser()
        patientList = mutableListOf()
        databaseref = FirebaseDatabase.getInstance().getReference("Clients")
        databaseref = databaseref.child(client).child("Patients")

        databaseref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    patientList.clear()
                    for(p in p0.children){
                        val pat = p.getValue(Patient::class.java)
                        patientList.add(pat!!)
                    }
                    PatientRecView.layoutManager = LinearLayoutManager(this@PatientListActivity)
                    PatientRecView.adapter = PatientAdapter(patientList, client)
                }
            }

        })

        addPatient.setOnClickListener{
            var regintent = Intent(this, PatientRegActivity::class.java)

            regintent.putExtra("clientID",client)
            //regintent.putExtra("patientID",patientList.size.toString())

            startActivity(regintent)
        }

        var databaserefuser = FirebaseDatabase.getInstance().getReference("Clients")
        databaserefuser = databaserefuser.child(client)

        databaserefuser.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val u = p0.getValue(Client::class.java)

                    Name.text = u?.cfname + " " + u?.clname
                    uPhnum.text = u?.cphonenum
                }
            }

        })

        EditClient.setOnClickListener{
            var editClient = Intent(this, UpdateClient::class.java)

            editClient.putExtra("clientID", client)

            startActivity(editClient)
        }
    }
}
