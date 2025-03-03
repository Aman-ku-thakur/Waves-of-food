package com.example.food

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.databinding.ActivityMainBinding
import com.example.food.model.OrderDetail
import com.google.android.play.core.integrity.i
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var completedOrderReferences: DatabaseReference
    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
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

        binding.addmenu.setOnClickListener {
            val intent = Intent(this, AddItemActivity::class.java)
            startActivity(intent)
        }
        binding.allitemmenu.setOnClickListener {
            val intent = Intent(this, AllItemAactivity::class.java)
            startActivity(intent)
        }

        binding.outfordeliverybutton.setOnClickListener {
            val intent = Intent(this, OutForDelivery::class.java)
            startActivity(intent)
        }
        binding.profilecard.setOnClickListener {
            val intent = Intent(this, AdminProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createUser.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.createUser.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        binding.pandingorder.setOnClickListener {
            val intent = Intent(this, PandingActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this,signUpActivity::class.java))
            finish()
        }

        pandingOrders()
completedOrders()
        wholeTimeEarning()
    }

    private fun wholeTimeEarning() {
        var listOfTotalPay = mutableListOf<Int>()

completedOrderReferences = FirebaseDatabase.getInstance().reference.child("CompleteOrder")
        completedOrderReferences.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
              for (orderSnapshot in snapshot.children){
                  var completeOrder = orderSnapshot.getValue(OrderDetail::class.java)
                  completeOrder?.totalPrice?.replace("$","")?.toIntOrNull()
                      ?.let { i ->
                          listOfTotalPay.add(i)
                      }

              }

                binding.textView13.text = listOfTotalPay.sum().toString()+ "$"
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun completedOrders() {
        val CompleteOrderReference = database.reference.child("CompleteOrder")
        var CompleteOrderItemCount = 0
        CompleteOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                CompleteOrderItemCount = snapshot.childrenCount.toInt()
                binding.CompleteOrderCount.text = CompleteOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    private fun pandingOrders() {
        database = FirebaseDatabase.getInstance()
        val pandingOrderReference = database.reference.child("OrderDetails")
        var pandingOrderItemCount = 0
        pandingOrderReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                pandingOrderItemCount = snapshot.childrenCount.toInt()
                binding.pandingOrders.text = pandingOrderItemCount.toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
}