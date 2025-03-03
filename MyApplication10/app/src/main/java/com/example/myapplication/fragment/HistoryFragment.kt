package com.example.myapplication.fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myapplication.Adapter.BuyAgainAdapter
import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHistoryBinding
import com.example.myapplication.model.OrderDetails
import com.example.myapplication.recentOrderItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.zip.Inflater


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: BuyAgainAdapter

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItem: MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
//        retrieve and display the order data hostory
        retrieveBuyHistory()



binding.cardView2.setOnClickListener{
    seeItemRecentBuy()
}


        binding.receivedbutton.setOnClickListener {
            updateOrderStatus()
        }
        return binding.root


    }

    private fun updateOrderStatus() {
        val itemPushkey = listOfOrderItem[0].itemPushKey
        val completeOrderReference = database.reference.child("CompleteOrder").child(itemPushkey!!)
        completeOrderReference.child("paymentReceived").setValue(true)


    }

    private fun seeItemRecentBuy() {
listOfOrderItem.firstOrNull()?.let { recentBuy->
    val intent = Intent(requireContext(),recentOrderItem::class.java)
    intent.putExtra("RecentBuyOrderItem",ArrayList(listOfOrderItem))
    startActivity(intent)
}
    }

    private fun retrieveBuyHistory() {
        if (!isAdded) return // Check if fragment is attached

        binding.recentBuyitem.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val buyItemReference: DatabaseReference =
            database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery = buyItemReference.orderByChild("currentItem")

        shortingQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return  // Check again inside the callback to prevent crashes

                listOfOrderItem.clear() // Avoid duplicate items
                for (buySnapshot in snapshot.children) {
                    val buyHistoryItem = buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }

                listOfOrderItem.reverse()
                if (listOfOrderItem.isNotEmpty()) {
                    setDataInRecentBuyItem()
                    setPreviousBuyItemRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseError", "Error fetching buy history: ${error.message}")
            }
        })
    }


    private fun setDataInRecentBuyItem() {
        if (!isAdded || context == null) return // Ensure the fragment is attached

        binding.recentBuyitem.visibility = View.VISIBLE
        val recentorderItem = listOfOrderItem.firstOrNull()
        recentorderItem?.let {
            with(binding) {
                foodNameHistory.text = it.foodNames?.firstOrNull() ?: ""
                foodpricehistory.text = it.foodPrices?.firstOrNull() ?: ""
                val image = it.foodImages?.firstOrNull() ?: ""
                val uri = Uri.parse(image)

                // Safely load image with Glide
                if (isAdded) {
                    Glide.with(requireContext()).load(uri).into(foodImagehistory)
                }
                listOfOrderItem.reverse()
                val irOrderIsAccepted = listOfOrderItem[0].orderAccepted
                Log.d("orderAccepted","setdata in recyclerViewItem:$irOrderIsAccepted")
                if (irOrderIsAccepted){
orderstatus.background.setTint(Color.GREEN)
                    receivedbutton.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun setPreviousBuyItemRecyclerView() {
        if (!isAdded || context == null) return // Ensure fragment is still attached

        val buyAgainFoodName = mutableListOf<String>()
        val buyAgainFoodPrice = mutableListOf<String>()
        val buyAgainFoodImage = mutableListOf<String>()

        for (i in 1 until listOfOrderItem.size) {
            listOfOrderItem[i].foodNames?.firstOrNull()?.let { buyAgainFoodName.add(it) }
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let { buyAgainFoodPrice.add(it) }
            listOfOrderItem[i].foodImages?.firstOrNull()?.let { buyAgainFoodImage.add(it) }
        }

        val rv = binding.buyagainrecyclerview
        rv.layoutManager = LinearLayoutManager(requireContext())

        if (isAdded) {
            adapter = BuyAgainAdapter(
                buyAgainFoodName,
                buyAgainFoodPrice,
                buyAgainFoodImage,
                requireContext()
            )
            rv.adapter = adapter
        }
    }



}