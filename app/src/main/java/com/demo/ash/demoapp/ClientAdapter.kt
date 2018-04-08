package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.client.view.*

/**
 * Created by ashwin on 3/5/2018.
 */
class ClientAdapter(val list: MutableList<Client>, val countList: MutableList<Int>, val clientIDList:MutableList<String>): RecyclerView.Adapter<ClientViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ClientViewHolder {
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.client, parent, false)
        return ClientViewHolder(cellForRow,"")
    }


    override fun onBindViewHolder(holder: ClientViewHolder?, position: Int) {
        val row = list.get(position)
        val count = countList.get(position)
        holder?.view?.NameView?.text = row.cfname + " " + row.clname
        holder?.view?.PhnumView?.text = row.cphonenum
        holder?.view?.PCountView?.text = "Patients' visited: " + count.toString()

        holder?.clientID = clientIDList[position]
    }

}

class ClientViewHolder(val view: View, var clientID: String): RecyclerView.ViewHolder(view) {
    init{
        view.setOnClickListener {
            val intent = Intent(view.context, PatientListActivity::class.java)
            intent.putExtra("clientID",clientID)
            view.context.startActivity(intent)
        }
    }
}