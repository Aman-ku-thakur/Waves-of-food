package com.example.food

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.Adapter.deliveryAdapter
import com.example.food.databinding.ActivityOutForDeliveryBinding
import com.example.food.model.OrderDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class OutForDelivery : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private var listOfCompleteOrderList : ArrayList<OrderDetail> = arrayListOf()

    private val binding : ActivityOutForDeliveryBinding by lazy {
        ActivityOutForDeliveryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.imageButton.setOnClickListener{
            finish()
        }
//retrieve and display completedOrder
        retrieveCompleteOrder()



    }

    private fun retrieveCompleteOrder() {
        database = FirebaseDatabase.getInstance()
        val completeOrderreference =database.reference.child("CompleteOrder")
            .orderByChild("currentItem")
        completeOrderreference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listOfCompleteOrderList.clear()
                for (orderSnapshot in snapshot.children){
                    val completeOrder = orderSnapshot.getValue(OrderDetail::class.java)
                    completeOrder?.let {
                        listOfCompleteOrderList.add(it) }
                }
//                reverese the list to display letest order first
                listOfCompleteOrderList.reverse()
                setDataIntoRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun setDataIntoRecyclerView() {
//        initialization list of hold customer name and payment status
        val customerNAme = mutableListOf<String>()
        val moneyStatus = mutableListOf<Boolean>()

        for (order in listOfCompleteOrderList){
           order.userName?.let { customerNAme.add(it) }
            moneyStatus.add(order.paymentReceived)
        }
        val adapter =deliveryAdapter(customerNAme,moneyStatus)
        binding.deliveryRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.deliveryRecyclerview.adapter=adapter

    }
}