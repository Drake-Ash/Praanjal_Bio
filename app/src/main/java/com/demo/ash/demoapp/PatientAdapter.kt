package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.patient.view.*

/**
 * Created by ashwin on 3/6/2018.
 */
class PatientAdapter(val list: MutableList<Patient>, var client: String): RecyclerView.Adapter<PatientViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PatientViewHolder {
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.patient, parent, false)
        return PatientViewHolder(cellForRow,"","")
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PatientViewHolder?, position: Int) {
        val row = list.get(position)
        holder?.view?.Breed?.text = row.breed
        holder?.view?.Age?.text = row.age
        holder?.view?.PatientId?.text = "#" + position
        holder?.patientID = row.key
        holder?.clientID = client
    }
}

class PatientViewHolder(val view: View, var patientID: String,var clientID: String): RecyclerView.ViewHolder(view){
    init {
        view.setOnClickListener {
            var pInfoIntent = Intent(view.context, PatientView::class.java)
            pInfoIntent.putExtra("patientID", patientID)
            pInfoIntent.putExtra("clientID", clientID)
            view.context.startActivity(pInfoIntent)
        }
    }
}
