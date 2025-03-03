package com.example.food

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.Adapter.OrderDetalsAdapter
import com.example.food.Adapter.PandingOrderAdapter
import com.example.food.databinding.ActivityOrderDetailsBinding
import com.example.food.model.OrderDetail

class OrderDetailsActivity : AppCompatActivity() {

    private var userName:String?= null
    private var address:String?= null
    private var phoneNumber:String?= null
    private var totalPrice:String?= null
    private var foodName:ArrayList<String> = arrayListOf()
    private var foodImages:ArrayList<String> = arrayListOf()
    private var foodQuantity:ArrayList<String> = arrayListOf()
    private var foodPrices:ArrayList<String> = arrayListOf()


    private  lateinit var binding:ActivityOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.button3.setOnClickListener{
            finish()
        }

        getDataFromIntent()
    }

    private fun getDataFromIntent() {
        val recievedOrderedDetails = intent.getSerializableExtra("UserOrderDetails") as OrderDetail
       recievedOrderedDetails?.let { orderDetail: OrderDetail ->


               userName = recievedOrderedDetails.userName
               foodName= recievedOrderedDetails.foodNames as ArrayList<String>
               foodImages = recievedOrderedDetails.foodImages as ArrayList<String>
               foodQuantity = recievedOrderedDetails.foodQuantity as ArrayList<String>
               address = recievedOrderedDetails.address
               phoneNumber = recievedOrderedDetails.phoneNumber
               foodPrices = recievedOrderedDetails.foodPrices as ArrayList<String>
               totalPrice = recievedOrderedDetails.totalPrice
               setUserDetail()
               setAdapter()

       }



    }
    private fun setAdapter() {
        binding.recyclervirew.layoutManager = LinearLayoutManager(this)
        val adapter = OrderDetalsAdapter(this,foodName,foodImages,foodQuantity,foodPrices)
        binding.recyclervirew.adapter = adapter
    }

    private fun setUserDetail() {
        binding.Name.text = userName
        binding.address.text = address
        binding.phone.text = phoneNumber
        binding.TotalAmount.text = totalPrice

    }
}