package com.demo.ash.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_patient_reg.*

class PatientRegActivity : AppCompatActivity() {
    lateinit var client: String
    var pid: Int = 0

    var gender: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_patient_reg)

        client = intent.getStringExtra("clientID")

        pGender.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListOf<String>("---Select Gender---","Male","Female"))

        pGender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                gender = 0
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                gender = p2
            }
        }

        var count = 0
        pReg.setOnClickListener{
            if(pBreed.text.isEmpty()){
                pBreed.error = "Enter Breed details"
                count = 1
            }
            if(gender == 0){
                count = 1
            }
            if(count == 0){
                enterPatientDetails()
            }
        }
    }

    private fun enterPatientDetails() {
        var user = FirebaseAuth.getInstance().currentUser
        val appDatabase = FirebaseDatabase.getInstance().getReference()

        val patientadd = appDatabase.child("Clients")
                .child(client).child("Patients").push().key


        val patient = Patient(
                breed = pBreed.text.toString().trim(),
                gender = gender,
                age = pAge.text.toString().trim(),
                key = patientadd.toString()
        )

        appDatabase.child("Clients")
                .child(client).child("Patients").child(patientadd).setValue(patient)

        finish()
    }
}
