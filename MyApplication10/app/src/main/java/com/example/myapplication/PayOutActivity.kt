package com.example.myapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityPayOutBinding
import com.example.myapplication.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayOutActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var name:String

    private lateinit var address:String
    private lateinit var phone:String
    private lateinit var totalamount:String
    private lateinit var foodItemName:ArrayList<String>
    private lateinit var foodItemImage:ArrayList<String>
    private lateinit var foodItemIngridients:ArrayList<String>
    private lateinit var foodItemquantities:ArrayList<Int>
    private lateinit var foodItemPrice:ArrayList<String>
    private lateinit var foodItemDescription:ArrayList<String>
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId:String

    lateinit var binding:ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPayOutBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



//        Initialize firebase and user detail
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference()
        setUserData()

        binding.button3.setOnClickListener{
            finish()
        }

//        get user detail from firebaes
        val intent = intent
        foodItemName =intent.getStringArrayListExtra("FoodItemName") as ArrayList<String>
        foodItemImage =intent.getStringArrayListExtra("FoodItemImage") as ArrayList<String>
        foodItemPrice =intent.getStringArrayListExtra("FoodItemPrice") as ArrayList<String>
        foodItemDescription =intent.getStringArrayListExtra("FoodItemDescription") as ArrayList<String>
        foodItemIngridients =intent.getStringArrayListExtra("FoodItemIngridients") as ArrayList<String>
        foodItemquantities =intent.getIntegerArrayListExtra("FoodItemquantities") as ArrayList<Int>


        totalamount = calculateTotalAmount().toString() + "$"
     //   binding.toatalamount.isEnabled = false

        binding.toatalamount.setText(totalamount)

        binding.saveButton.setOnClickListener{

//            get data from edit text because we have to send this detail on admin pannel


            name = binding.Name.text.toString().trim()
            address = binding.address.text.toString().trim()
            phone = binding.phone.text.toString().trim()
            if(name.isBlank() || address.isBlank() || phone.isBlank()){
                Toast.makeText(this, "Please enter all the detail", Toast.LENGTH_SHORT).show()
            }else
            {
                placeorder()
            }



        }

    }

    private fun placeorder() {
        userId = auth.currentUser?.uid?:""
        val time = System.currentTimeMillis()
        val itempushkey  = databaseReference.child("OrderDetails").push().key
        val orderdetail = OrderDetails(
            userId,
            name,
            foodItemName,
            foodItemPrice,
            foodItemImage,
            foodItemquantities,
            address,
            phone,
            time,
            itempushkey,
            false,
            false,
            totalamount,
        )

        val orderReference = databaseReference.child("OrderDetails").child(itempushkey!!)
        orderReference.setValue(orderdetail).addOnSuccessListener {
            val bottumSheet = CongretsBottumSheet()
            bottumSheet.show(supportFragmentManager,"Test")
            removeItemFromCart()
            addOrderToHistory(orderdetail)
        }
            .addOnFailureListener {
                Toast.makeText(this, "order failed", Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderdetail: OrderDetails) {
databaseReference.child("user").child(userId).child("BuyHistory")
    .child(orderdetail.itemPushKey!!)
    .setValue(orderdetail).addOnSuccessListener {

    }
    }

    private fun removeItemFromCart() {
        val carItemReference = databaseReference.child("user").child(userId).child("CartItem")
        carItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
        var totalamount = 0
        for (i in 0 until foodItemPrice.size){
            var price = foodItemPrice[i]
            val lastchar = price.last()
            val priceIntVale =if(lastchar=='$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity = foodItemquantities[i]
            totalamount += priceIntVale * quantity
        }
        return totalamount


    }

    private fun setUserData() {
        val user = auth.currentUser
        if(user!=null){
            val userId:String =user.uid
            val userReference = databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        val names = snapshot.child("name").getValue(String::class.java)?:""
                        val addresss = snapshot.child("address").getValue(String::class.java)?:""
                        val phones = snapshot.child("phone").getValue(String::class.java)?:""

                        binding.apply {
                            Name.setText(names)
                            address.setText(addresss)
                            phone.setText(phones)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        }



    }
}