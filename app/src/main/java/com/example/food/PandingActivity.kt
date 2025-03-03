package com.example.food

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.Adapter.PandingOrderAdapter
import com.example.food.databinding.ActivityPandingBinding
import com.example.food.model.OrderDetail
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PandingActivity : AppCompatActivity(), PandingOrderAdapter.OnItemClicked {

    private var listNAme: MutableList<String> = mutableListOf()
    private var listOfTotslPrice: MutableList<String> = mutableListOf()
    private var listOfImageFirstFoodOrder: MutableList<String> = mutableListOf()
    private var listOfOrderItem: ArrayList<OrderDetail> = arrayListOf()

    private lateinit var database: FirebaseDatabase
    private lateinit var databaseOrderDetail: DatabaseReference
    private val binding: ActivityPandingBinding by lazy {
        ActivityPandingBinding.inflate(layoutInflater)
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
//        initializing database
        database = FirebaseDatabase.getInstance()
        databaseOrderDetail = database.reference.child("OrderDetails")
        getOrderDetails()


        binding.imageButton.setOnClickListener {
            finish()
        }
    }

    private fun getOrderDetails() {
//        retireve order details from firebase database
        databaseOrderDetail.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children) {
                    val orderDetails = orderSnapshot.getValue(OrderDetail::class.java)
                    orderDetails?.let {
                        listOfOrderItem.add(it)
                    }
                }
                addDataToListForRecyclerView()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun addDataToListForRecyclerView() {
        for (orderItem in listOfOrderItem) {
            orderItem.userName?.let { listNAme.add(it) }
            orderItem.totalPrice?.let { listOfTotslPrice.add(it) }
            orderItem.foodImages?.filterNot { it.isEmpty() }?.forEach {
                listOfImageFirstFoodOrder.add(it)
            }
        }
        setAdapter()
    }


    private fun setAdapter() {
        binding.pandingRecyclerView.layoutManager = LinearLayoutManager(this)
        val adapter =
            PandingOrderAdapter(this, listNAme, listOfTotslPrice, listOfImageFirstFoodOrder, this)
        binding.pandingRecyclerView.adapter = adapter
    }

    override fun onItemClickListner(position: Int) {

        val intent = Intent(this, OrderDetailsActivity::class.java)
        val userOrderDetails = listOfOrderItem[position]
        intent.putExtra("UserOrderDetails", userOrderDetails)
        startActivity(intent)
    }

    override fun onItemAcceptClickListner(position: Int) {
//        handle item acceptation and update database
        val childItemPushkey = listOfOrderItem[position].itemPushKey
        val clickItemOrderReference = childItemPushkey?.let {
            database.reference.child("OrderDetails").child(it)
        }
        clickItemOrderReference?.child("orderAccepted")?.setValue(true)
        updateOrderAcceotStatus(position)
    }


    override fun onItemDispatchClickListner(position: Int) {
//Handle item dispatch and update databse
        val dispatchItemPushKey = listOfOrderItem[position].itemPushKey
        val dispatchItemOrderReference = database.reference.child("CompleteOrder").child(dispatchItemPushKey!!)
        dispatchItemOrderReference.setValue(listOfOrderItem[position])
            .addOnSuccessListener {
                deleyteThisItemFromOrderDetail(dispatchItemPushKey)
            }
    }

    private fun deleyteThisItemFromOrderDetail(dispatchItemPushKey: String) {

        val orderDetailsItemReference = database.reference.child("OrderDetails").child(dispatchItemPushKey)
        orderDetailsItemReference.removeValue()
            .addOnSuccessListener {
                Toast.makeText(this, "Order is dispatched", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Order is not dispatched", Toast.LENGTH_SHORT).show()
            }
    }


    private fun updateOrderAcceotStatus(position: Int) {
// update user acceptance in user's BuyHistory and OrderDetails
        val userIdOfClickedItem = listOfOrderItem[position].userUid
        val pushKeyOfClickedItem = listOfOrderItem[position].itemPushKey
        val byuHistoryReference =
            database.reference.child("user").child(userIdOfClickedItem!!).child("BuyHistory")
                .child(pushKeyOfClickedItem!!)
        byuHistoryReference.child("orderAccepted").setValue(true)
        databaseOrderDetail.child(pushKeyOfClickedItem).child("orderAccepted").setValue(true)
    }


}