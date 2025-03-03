package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.RecentBuyAdapter
import com.example.myapplication.databinding.ActivityRecentOrderItemBinding
import com.example.myapplication.model.OrderDetails

class recentOrderItem : AppCompatActivity() {

    private lateinit var allfoodsname:ArrayList<String>
    private lateinit var allImagesname:ArrayList<String>
    private lateinit var allPricesname:ArrayList<String>
    private lateinit var allQuantitysname:ArrayList<Int>
    private lateinit var binding : ActivityRecentOrderItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        binding= ActivityRecentOrderItemBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.backbutton.setOnClickListener{
            finish()
        }

        val recentOrderItem = intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItem?.let { orderdetail->
            if (orderdetail.isNotEmpty()){
                val recentOrder =orderdetail[0]
                allfoodsname = recentOrder.foodNames as ArrayList<String>
                allImagesname= recentOrder.foodImages as ArrayList<String>
                allQuantitysname = recentOrder.foodQuantity?.map { it.toIntOrNull() ?: 0 } as ArrayList<Int>
                allPricesname = recentOrder.foodPrices as ArrayList<String>
            }


        }
        setAdapter()
    }

    private fun setAdapter() {
        val rv = binding.recentRecyclerView
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentBuyAdapter(this,allPricesname,allImagesname,allfoodsname,allQuantitysname)
        rv.adapter = adapter
    }
}