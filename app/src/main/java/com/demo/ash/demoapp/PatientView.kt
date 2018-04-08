package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_patient_view.*

class PatientView : AppCompatActivity() {
    lateinit var client: String
    lateinit var patient: String

    lateinit var databaseref: DatabaseReference

    lateinit var treatmentList: MutableList<Treatment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_view)

        client = intent.getStringExtra("clientID")
        patient = intent.getStringExtra("patientID")

        treatmentList = mutableListOf()

        databaseref = FirebaseDatabase.getInstance().getReference("Clients")
        databaseref = databaseref.child(client).child("Patients").child(patient).child("Treatment")

        addTreatment.setOnClickListener {
            var EntryTreatment = Intent(this, TreatmentEntry::class.java)
            EntryTreatment.putExtra("clientID", client)
            EntryTreatment.putExtra("patientID", patient)

            startActivity(EntryTreatment)
        }


        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    treatmentList.clear()
                    var tidlist: MutableList<String> = mutableListOf()
                    tidlist.clear()
                    for(p in p0.children){
                        val pat = p.getValue(Treatment::class.java)
                        treatmentList.add(pat!!)
                        tidlist.add(p.key)
                    }
                    treatmentslist.layoutManager = LinearLayoutManager(this@PatientView)

                    treatmentslist.adapter = TreatmentAdapter(treatmentList,client, patient, tidlist)
                }
            }

        })

        var databaserefuser = FirebaseDatabase.getInstance().getReference("Clients")
        databaserefuser = databaserefuser.child(client).child("Patients").child(patient)

        databaserefuser.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val u = p0.getValue(Patient::class.java)

                    var x =""
                    if(u?.gender==1)
                        x="male"
                    else if(u?.gender == 2)
                        x="female"

                    PatientId.text = "ID: " + u?.key
                    pGender.text = x
                    pBreed.text = u?.breed
                }
            }

        })


    }
}
