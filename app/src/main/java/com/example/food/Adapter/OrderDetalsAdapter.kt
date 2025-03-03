package com.example.food.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.databinding.OrderDetailsItemBinding

class OrderDetalsAdapter(
    private var context: Context,
    private var foodNames: ArrayList<String>,
   private var  foodImage: ArrayList<String>,
    private var foodQuantity:ArrayList<String>,
    private var foodprice:ArrayList<String>

): RecyclerView.Adapter<OrderDetalsAdapter.OrderDetailsViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderDetalsAdapter.OrderDetailsViewHolder {
       val binding = OrderDetailsItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderDetailsViewHolder(binding)

    }

    override fun onBindViewHolder(
        holder: OrderDetalsAdapter.OrderDetailsViewHolder,
        position: Int
    ) {
holder.bind(position)    }

    override fun getItemCount(): Int {
     return foodNames.size
    }

   inner class OrderDetailsViewHolder(private val binding:OrderDetailsItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                orderDetailFoodName.text = foodNames[position]
                orderDetailquantity.text = foodQuantity[position]
                orderDetailFoodPrice.text = foodprice[position]
          val uriString =foodImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(orderDetailImage)
            }

        }

    }
}