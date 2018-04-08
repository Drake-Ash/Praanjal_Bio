package com.demo.ash.demoapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var mAuth : FirebaseAuth

    lateinit var databaseref : DatabaseReference
    lateinit var ClientIDList: MutableList<kotlin.String>
    lateinit var ClientList: MutableList<Client>
    lateinit var CountList: MutableList<Int>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
        SignOut.setOnClickListener{
            view: View? ->  mAuth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            Toast.makeText(this, "Logged Out Successfully", Toast.LENGTH_SHORT).show()
        }

        addClient.setOnClickListener{
            startActivity(Intent(this, ClientRegActivity::class.java))
        }

        var user = FirebaseAuth.getInstance().getCurrentUser()
        ClientList = mutableListOf()
        ClientIDList = mutableListOf()
        CountList = mutableListOf()
        databaseref = FirebaseDatabase.getInstance().getReference("Users")
        databaseref = databaseref.child(user!!.phoneNumber.toString().trim()).child("Clients")

        databaseref.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    ClientIDList.clear()
                    for(c in p0.children){
                        val client = c.getValue(UserRefToClient::class.java)
                        ClientIDList.add(client!!.client)
                    }
                    //ClientRecView.layoutManager = LinearLayoutManager(this@MainActivity)
                    //val adapter = ClientAdapter(this@MainActivity, R.id.ClientListView, ClientList)
                    //ClientRecView.adapter = ClientAdapter(ClientList)
                }
            }

        })

        databaseref = FirebaseDatabase.getInstance().getReference("Clients")

        var q= databaseref.orderByChild("cfname").addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    ClientList.clear()
                    for(c in ClientIDList){
                        val temp = p0.child(c)
                        val client = temp.getValue(Client::class.java)
                        var count = 0
                        for(p in temp.child("Patients").children)
                            count++
                        CountList.add(count)
                        ClientList.add(client!!)
                    }
                    ClientRecView.layoutManager = LinearLayoutManager(this@MainActivity)
                    ClientRecView.adapter = ClientAdapter(ClientList, CountList, ClientIDList)
                }
            }
        })

        EditUser.setOnClickListener{
            var EditIntent = Intent(this, UpdateUser::class.java)
            startActivity(EditIntent)
        }

        var databaserefuser = FirebaseDatabase.getInstance().getReference("Users")
        databaserefuser = databaserefuser.child(user!!.phoneNumber.toString().trim())

        databaserefuser.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {

            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    val u = p0.getValue(User::class.java)

                    UserName.text = u?.username
                    Name.text = u?.fname + " " + u?.lname
                    uPhnum.text = u?.phonenum
                }
            }

        })

        var test = FirebaseDatabase.getInstance().getReference("Clients")

        var query = test.orderByChild("cfname")

        query.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(p0: DataSnapshot?) {
                if(p0!!.exists()){
                    p0
                    p0
                }
            }

        })



    }



    override fun onStart(){
        super.onStart()
    }
}
