package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.DetailsActivity
import com.example.myapplication.databinding.MenuItemBinding
import com.example.myapplication.model.MenuItems
import java.security.PrivateKey

class menuAdapter(
    private val menuItems: List<MenuItems>,
    private val requireContext: Context
) : RecyclerView.Adapter<menuAdapter.menuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): menuViewHolder {
        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return menuViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return menuItems.size
    }

    override fun onBindViewHolder(holder: menuViewHolder, position: Int) {
        holder.bind(position)
    }


    inner class menuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    opendetailactivity(position)
                }

            }
        }
// set data into recyclerview iten name , price, image
        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuItems[position]
                menuFoodname.text = menuItem.foodName
                menuprice.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage )
                Glide.with(requireContext).load(uri).into(menuFoodImage)

            }
        }

    }

    private fun opendetailactivity(position: Int) {
        val menuItem = menuItems[position]
//        a intent to open detailActivity and pass data
        val intent = Intent(requireContext, DetailsActivity::class.java).apply {
            putExtra("MenuItemName", menuItem.foodName)
            putExtra("MenuItemImage", menuItem.foodImage)
            putExtra("MenuItemDescription", menuItem.foodDescription)
            putExtra("MenuItemPrice", menuItem.foodPrice)
            putExtra("MenuItemIngridient", menuItem.foodIngredient)
        }
//        start the detail activity
        requireContext.startActivity(intent)

    }


}


