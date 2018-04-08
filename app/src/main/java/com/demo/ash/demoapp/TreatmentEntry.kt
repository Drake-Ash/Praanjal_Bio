package com.demo.ash.demoapp

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.activity_treatment_entry.*
import java.io.File
import java.io.FileFilter
import java.io.FilenameFilter
import java.util.*

class TreatmentEntry : AppCompatActivity() {

    var domain: Int = 0
    var domaindesc: Int = 0

    lateinit var Key: String
    lateinit var databaseref: DatabaseReference

    lateinit var client: String
    lateinit var patient: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_treatment_entry)

        var context: Context = this

        patient = intent.getStringExtra("patientID")
        client = intent.getStringExtra("clientID")


        var ReproductionDesc: MutableList<String>
        ReproductionDesc = mutableListOf()
        ReproductionDesc.add("----------------Select Description----------------")
        ReproductionDesc.add("Artificial Insemination")
        ReproductionDesc.add("Placenta")
        ReproductionDesc.add("Placenta follow-up")
        ReproductionDesc.add("EndoMetritis")
        ReproductionDesc.add("Endometritis follow-up")
        ReproductionDesc.add("Anestrus")
        ReproductionDesc.add("Repeat Bleeder")

        var MedicalDesc: MutableList<String>
        MedicalDesc = mutableListOf()
        MedicalDesc.add("----------------Select Description----------------")

        var SurgicalDesc: MutableList<String>
        SurgicalDesc = mutableListOf()
        SurgicalDesc.add("----------------Select Description----------------")

        var PreventiveDesc: MutableList<String>
        PreventiveDesc = mutableListOf()
        PreventiveDesc.add("----------------Select Description----------------")

        var OthersDesc: MutableList<String>
        OthersDesc = mutableListOf()
        OthersDesc.add("----------------Select Description----------------")

        var DefaultDesc: MutableList<String>
        DefaultDesc = mutableListOf()
        DefaultDesc.add("---------------Select Domain first---------------")

        var TreatmentsList: MutableList<MutableList<String>> = mutableListOf()
        TreatmentsList.add(DefaultDesc)
        TreatmentsList.add(ReproductionDesc)
        TreatmentsList.add(MedicalDesc)
        TreatmentsList.add(SurgicalDesc)
        TreatmentsList.add(PreventiveDesc)
        TreatmentsList.add(OthersDesc)


        DomainSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                arrayListOf<String>("---------------------Select Domain---------------------", "Reproduction", "Medical", "Surgical", "Preventive", "Others")) as SpinnerAdapter?

        DomainDescSpinner.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                ArrayList(TreatmentsList[domain]))

        DomainSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                domain = 0
                domaindesc = 0

                DomainDescSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                        ArrayList(TreatmentsList[domain]))
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                domain = p2
                domaindesc = 0
                DomainDescSpinner.adapter = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1,
                        ArrayList(TreatmentsList[domain]))
            }
        }


        DomainDescSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
                domaindesc = 0
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                domaindesc = p2
            }
        }


        databaseref = FirebaseDatabase.getInstance().getReference("Clients")
        databaseref = databaseref.child(client).child("Patients").child(patient)

        Key = databaseref.push().key

        ImageCapture.setOnClickListener{
            var camera_intent = Intent(this,CameraActivity::class.java)
            camera_intent.putExtra("Key", Key)
            startActivity(camera_intent)
        }

        submit.setOnClickListener {
            var c = 0
            if(domain == 0){
                c=1
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select valid Domain")
                builder.setPositiveButton("Okay",{ dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }
            if(domaindesc == 0){
                c=1
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Select valid Description")
                builder.setPositiveButton("Okay",{ dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }
            if(comment.text.isBlank()){
                c=1
                val builder = AlertDialog.Builder(context)
                builder.setTitle("Enter Proper Comment")
                builder.setPositiveButton("Okay",{ dialogInterface: DialogInterface, i: Int -> })
                builder.show()
            }

            if(c==0) {
                submitCase()
            }
        }

    }

    fun submitCase() {
        val path = Environment.getExternalStorageDirectory()
        val dir = File(path.toString() + "/pranjal/" /*+ Key + "/"*/)

        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Updating case file...")
        progressDialog.show()

        var user = FirebaseAuth.getInstance().currentUser

        val treatment = Treatment(
                date = Date(),
                Domain = domain,
                idT = domaindesc,
                tComment = comment.text.toString(),
                tDoc = user?.phoneNumber.toString(),
                imgcount = 0
        )

        databaseref.child("Treatment").child(Key).setValue(treatment)


        var imageList: MutableList<File> = mutableListOf<File>()

        if (dir.exists()) {
            val temp = dir.list()
            for (t in temp) {
                if (t.toString().endsWith(".png"))
                    imageList.add(File(dir.toString() + "/" + t))
            }

            var storage: FirebaseStorage? = FirebaseStorage.getInstance()
            var StorageReference: StorageReference? = storage!!.reference

            val bitmap = BitmapFactory.decodeFile(imageList[0].toString())

            val imageRef = StorageReference!!.child("Clients").child(client).child("Patients").child(patient)
                    .child("Treatment").child(Key)
            var ind = 0
            var id = 0
            for (item in imageList) {
                imageRef.child(ind.toString() + ".png").putFile(Uri.fromFile(item))
                        .addOnSuccessListener{ taskSnapshot->
                            databaseref.child("Treatment").child(Key).child("ImageSrc")
                                    .child(id.toString()).setValue(TreatmentImage(id.toString(),
                                            taskSnapshot.downloadUrl.toString()))
                            id++
                }

                ind++
            }

            databaseref.child("Treatment").child(Key).child("imgcount").setValue(imageList.size)
        }

        val dirdelete = File(path.toString() + "/pranjal/" + Key + "/")
        dirdelete.delete()

        progressDialog.dismiss()

        finish()
    }
}
