package com.example.food.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food.R
import com.example.food.databinding.PandingItemBinding

class PandingOrderAdapter(
    private val context: Context,
    private val customerName: MutableList<String>,
    private val PandingFoodQuantinty: MutableList<String>,
    private val PandingFoodImage: MutableList<String>,
  private val ItemClicked: OnItemClicked
) : RecyclerView.Adapter<PandingOrderAdapter.PandingViewHolder>() {

   interface OnItemClicked{
       fun onItemClickListner(position: Int)
       fun onItemAcceptClickListner(position: Int)
       fun onItemDispatchClickListner(position: Int)
   }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PandingOrderAdapter.PandingViewHolder {
        val binding = PandingItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PandingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PandingOrderAdapter.PandingViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return customerName.size
    }

    inner class PandingViewHolder(private val binding: PandingItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private var isaccept = false


        fun bind(position: Int) {
            if (position < customerName.size && position < PandingFoodQuantinty.size && position < PandingFoodImage.size) {
                binding.apply {
                    pandingcustomerName.text = customerName[position]
                    pandingquantity.text = PandingFoodQuantinty[position]

                    val uriString = PandingFoodImage[position]
                    val uri = Uri.parse(uriString)
                    Glide.with(context)
                        .load(uriString)
                        .into(pandingImage)

                    orderacceptbutton.apply {
                        if (!isaccept) {
                            text = "Accept"
                        } else {
                            text = "Dispatch"
                        }
                        setOnClickListener {
                            if (!isaccept) {
                                text = "Dispatch"
                                isaccept = true
                                showtoast("Order is accepted")

                                ItemClicked.onItemAcceptClickListner(position)
                            } else {
                                if (adapterPosition != RecyclerView.NO_POSITION) {
                                    customerName.removeAt(adapterPosition)
                                    PandingFoodQuantinty.removeAt(adapterPosition)
                                    PandingFoodImage.removeAt(adapterPosition)
                                    notifyItemRemoved(adapterPosition)
                                    showtoast("Order is dispatched")
                                    ItemClicked.onItemDispatchClickListner(position)
                                }
                            }
                        }
                    }
                    itemView.setOnClickListener {
                        ItemClicked.onItemClickListner(position)
                    }

                }
            }
        }
        private fun showtoast(message: String) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    }





