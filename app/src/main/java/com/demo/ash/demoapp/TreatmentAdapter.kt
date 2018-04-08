package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.treatment.view.*

/**
 * Created by ashwin on 3/21/2018.
 */
class TreatmentAdapter(val list: MutableList<Treatment>, val client: String, val patient: String, val tid: MutableList<String>): RecyclerView.Adapter<TreatmentViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TreatmentViewHolder{
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.treatment, parent, false)
        return TreatmentViewHolder(cellForRow)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: TreatmentViewHolder?, position: Int) {
        var ReproductionDesc: MutableList<String>
        ReproductionDesc = mutableListOf()
        ReproductionDesc.add("Artificial Insemination")
        ReproductionDesc.add("Placenta")
        ReproductionDesc.add("Placenta follow-up")
        ReproductionDesc.add("EndoMetritis")
        ReproductionDesc.add("Endometritis follow-up")
        ReproductionDesc.add("Anestrus")
        ReproductionDesc.add("Repeat Bleeder")

        var MedicalDesc: MutableList<String>
        MedicalDesc = mutableListOf()

        var SurgicalDesc: MutableList<String>
        SurgicalDesc = mutableListOf()

        var PreventiveDesc: MutableList<String>
        PreventiveDesc = mutableListOf()

        var OthersDesc: MutableList<String>
        OthersDesc = mutableListOf()

        var TreatmentsList: MutableList<MutableList<String>> = mutableListOf()

        TreatmentsList.add(ReproductionDesc)
        TreatmentsList.add(MedicalDesc)
        TreatmentsList.add(SurgicalDesc)
        TreatmentsList.add(PreventiveDesc)
        TreatmentsList.add(OthersDesc)

        var TreatmentTitle: MutableList<String> = mutableListOf()
        TreatmentTitle.add("Reproduction")
        TreatmentTitle.add("Medical")
        TreatmentTitle.add("Surgical")
        TreatmentTitle.add("Preventive")
        TreatmentTitle.add("Others")

        val row = list.get(position)
        holder?.view?.tDate?.text = row.date.toString()
        holder?.view?.tDoctor?.text = "Doctor: " + row.tDoc
        holder?.view?.tDomain?.text = TreatmentTitle[row.Domain - 1]
        holder?.view?.tDetails?.text = (TreatmentsList[row.Domain - 1])[row.idT-1]
        holder?.view?.tComment?.text = "Comments:\n" + row.tComment

        holder?.view?.Expand?.setOnClickListener{
            holder.view.Expand?.visibility = View.INVISIBLE
            holder.view.Contract?.visibility = View.VISIBLE
            holder.view.tComment?.visibility = View.VISIBLE
            holder.view.direct_to_images.visibility = View.VISIBLE
            holder.view.tComment?.text = "Comments:\n" + row.tComment
        }

        holder?.view?.Contract?.setOnClickListener{
            holder.view.Contract?.visibility = View.INVISIBLE
            holder.view.Expand?.visibility = View.VISIBLE
            holder.view.tComment?.visibility = View.GONE
            holder.view.direct_to_images.visibility = View.GONE
        }

        holder?.view?.direct_to_images?.setOnClickListener {
            var ViewImageIntent = Intent(holder?.view.context, ViewCamera::class.java)
            ViewImageIntent.putExtra("client", client)
            ViewImageIntent.putExtra("patient", patient)
            ViewImageIntent.putExtra("tid", tid[position])
            ViewImageIntent.putExtra("imgcount", row.imgcount)
            holder?.view?.context.startActivity(ViewImageIntent)
        }

        holder?.view?.setOnClickListener {
            if(holder.view.Expand.visibility==View.VISIBLE){
                holder.view.Expand?.visibility = View.GONE
                holder.view.Contract?.visibility = View.VISIBLE
                holder.view.tComment?.visibility = View.VISIBLE
                holder.view.direct_to_images.visibility = View.VISIBLE
                holder.view.tComment?.text = "Comments:\n" + row.tComment
            }
            else if(holder.view.Contract.visibility==View.VISIBLE) {
                holder.view.Contract?.visibility = View.GONE
                holder.view.Expand?.visibility = View.VISIBLE
                holder.view.tComment?.visibility = View.GONE
                holder.view.direct_to_images.visibility = View.GONE
            }
        }
    }
}

class TreatmentViewHolder(val view: View): RecyclerView.ViewHolder(view){

}
