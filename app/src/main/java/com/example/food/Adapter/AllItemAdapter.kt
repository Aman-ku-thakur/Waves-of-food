package com.example.food.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.databinding.ItemBinding
import com.example.food.model.AllMenu
import com.google.firebase.database.DatabaseReference
import java.security.PrivateKey


class AllItemAdapter(
   private val contex:Context,
    private val menuList:ArrayList<AllMenu>,
   databaseReference: DatabaseReference,
   private val onDeleteClickListener:(position :Int) -> Unit,




) : RecyclerView.Adapter<AllItemAdapter.ItemViewHolder>() {

    private val itemQuantities = IntArray( menuList.size) { 1 }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return  menuList.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class ItemViewHolder(private val binding: ItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {

                val quantity = itemQuantities[position]
                val menuItem = menuList[position]
                val uriString = menuItem.foodImage
                val uri = Uri.parse(uriString)


                binding.menuFoodName.text =  menuItem.foodName
                binding.menuFoodPrice.text =  menuItem.foodPrice

                Glide.with(contex).load(uri).into(menuFoodImage)

                binding.quantity.text = quantity.toString()
                minus.setOnClickListener {
                    decreasequantity(position)
                }
                plus.setOnClickListener {
                    increasequantity(position)
                }
                delete.setOnClickListener {
                    onDeleteClickListener(position)
                }
            }

        }

        private fun increasequantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreasequantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                binding.quantity.text = itemQuantities[position].toString()
            }
        }

        private fun deletequantity(position: Int) {
             menuList.removeAt(position)
             menuList.removeAt(position)
             menuList.removeAt(position)

            notifyItemRemoved(position)
            notifyItemRangeChanged(position,  menuList.size)

        }

    }


}