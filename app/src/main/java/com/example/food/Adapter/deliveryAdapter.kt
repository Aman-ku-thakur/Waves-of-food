package com.example.food.Adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.food.databinding.DeleviryitemBinding

class deliveryAdapter(
    private val CoustomerNAme: MutableList<String>,
    private val moneyStatus: MutableList<Boolean>
) : RecyclerView.Adapter<deliveryAdapter.deliveryViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): deliveryAdapter.deliveryViewHolder {
        val binding =
            DeleviryitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return deliveryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: deliveryAdapter.deliveryViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return CoustomerNAme.size
    }

    inner class deliveryViewHolder(private val binding: DeleviryitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                customername.text = CoustomerNAme[position]
                if (moneyStatus[position]== true){
                    paymentstatus.text = "Not Recieve"
                }else{

                }
               paymentstatus.text = "recieve"

                val colorMap = mapOf(
                  true to Color.GREEN, false to Color.RED
                )

                paymentstatus.setTextColor(colorMap[moneyStatus[position]] ?: Color.BLACK)
                statuscolor.backgroundTintList =
                    ColorStateList.valueOf(colorMap[moneyStatus[position]] ?: Color.BLACK)
            }
        }

    }

}