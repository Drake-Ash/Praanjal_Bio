package com.demo.ash.demoapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_view_camera.*


class ViewCamera : AppCompatActivity() {
    lateinit var client: String
    lateinit var patient: String
    lateinit var tid: String
    var imgcount: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_camera)

        client = intent.getStringExtra("client")
        patient = intent.getStringExtra("patient")
        tid = intent.getStringExtra("tid")
        imgcount = intent.getIntExtra("imgcount", 0)


        var storage = FirebaseStorage.getInstance()
        var storageReference = storage!!.reference

        var databaseref = FirebaseDatabase.getInstance().getReference("Clients")
        databaseref = databaseref.child(client).child("Patients").child(patient)

        databaseref = databaseref.child("Treatment").child(tid).child("ImageSrc")

        databaseref.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    var imageSrcList: MutableList<String> = mutableListOf()
                    imageSrcList.clear()
                    for(p in p0.children){
                        val pat = p.getValue(TreatmentImage::class.java)
                        imageSrcList.add(pat!!.url)
                    }
                    ImageList.layoutManager = LinearLayoutManager(this@ViewCamera)
                    ImageList.adapter = ImageListAdapter(imageSrcList, client, patient, tid)
                }
            }

        })

    }
}
