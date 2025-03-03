package com.example.myapplication.fragment


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.cartadapter

import com.example.myapplication.PayOutActivity

import com.example.myapplication.databinding.FragmentCartBinding
import com.example.myapplication.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class cartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var foodImageUrl: MutableList<String>
    private lateinit var foodIngridients: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    lateinit var cartAdapter: cartadapter

    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        retrievecartItem()





        binding.button.setOnClickListener {
//            get order items details before proceeding to check out
            getorderitemdetails()

        }

        return binding.root
    }

    private fun getorderitemdetails() {
        val orderIdReferece: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItem")

        val foodNAme = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodimage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngridients = mutableListOf<String>()
//        for get item quantity
        val foodQuantity = cartAdapter.getItemQuantities()

        orderIdReferece.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
//                    get the cart item to respective list
                    val orderItem = foodSnapshot.getValue(CartItem::class.java)
//                    add item detail on the list
                    orderItem?.foodName?.let { foodNAme.add(it) }
                    orderItem?.foodPrice?.let { foodPrice.add(it) }
                    orderItem?.foodImage?.let { foodimage.add(it) }
                    orderItem?.foodDescription?.let { foodDescription.add(it) }
                    orderItem?.foodIngridient?.let { foodIngridients.add(it) }

                }
                orderNow(
                    foodNAme,
                    foodPrice,
                    foodDescription,
                    foodimage,
                    foodQuantity,
                    foodIngridients
                )

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    requireContext(),
                    "OrderMaking failed please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun orderNow(
        foodNAme: MutableList<String>,
        foodPrice: MutableList<String>,
        foodDescription: MutableList<String>,
        foodimage: MutableList<String>,
        foodQuantity: MutableList<Int>,
        foodIngridients: MutableList<String>

    ) {
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("FoodItemName", foodNAme as ArrayList<String>)
            intent.putExtra("FoodItemImage", foodimage as ArrayList<String>)
            intent.putExtra("FoodItemPrice", foodPrice as ArrayList<String>)
            intent.putExtra("FoodItemDescription", foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngridients", foodIngridients as ArrayList<String>)
            intent.putExtra("FoodItemquantities", foodQuantity as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrievecartItem() {
//        database reference to the database
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""
        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItem")
//        List to store cart item
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImageUrl = mutableListOf()
        foodDescriptions = mutableListOf()
        foodIngridients = mutableListOf()
        quantity = mutableListOf()

//        fetch data from the database
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
//                    get the cartItem object from the child node


                    val cartItems = foodsnapshot.getValue(CartItem::class.java)
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodImage?.let { foodImageUrl.add(it) }
                    cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngridient?.let { foodIngridients.add(it) }

                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "this data not fatch ", Toast.LENGTH_SHORT).show()
            }


        })


    }

    private fun setAdapter() {

        cartAdapter = cartadapter(
            foodNames,
            foodPrices,
            foodImageUrl,
            foodDescriptions,
            quantity,
            foodIngridients,
            requireContext()
        )
        binding.cartrecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.cartrecyclerview.adapter = cartAdapter
    }


    companion object {


    }
}