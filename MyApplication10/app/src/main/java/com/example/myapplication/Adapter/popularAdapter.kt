package com.example.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailsActivity
import com.example.myapplication.databinding.PopularItemBinding

class popularAdapter(private val items:List<String>,private val price: List<String>,private val image:List<Int>,private val requireContext: Context) : RecyclerView.Adapter<popularAdapter.PouplerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PouplerViewHolder {
       return PouplerViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }



    override fun onBindViewHolder(holder: PouplerViewHolder, position: Int) {
    val item = items [position]
        val images = image[position]
        val price =  price[position]
        holder.bind(item,price,images)

        holder.itemView.setOnClickListener{
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("menuItemName",item)
            intent.putExtra("MenuImages",images)
            requireContext.startActivity(intent)
        }
    }



    override fun getItemCount(): Int {

        return items.size
    }


    class PouplerViewHolder (private val binding : PopularItemBinding) : RecyclerView.ViewHolder(binding.root) {

       private val imagesView = binding.menuFoodImage

        fun bind(item: String,price:String, images: Int) {
           binding.menuFoodname.text = item
            binding.menuprice.text = price
            imagesView.setImageResource(images)

        }

    }
}