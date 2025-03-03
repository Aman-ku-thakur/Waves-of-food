package com.example.myapplication.Adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartadapter(
    private val cartitems: MutableList<String>,
    private val cartItemprice: MutableList<String>,
    private var cartImage: MutableList<String>,
    private var carDescription: MutableList<String>,
    private val carQuantity: MutableList<Int>,
    private var carIngridient: MutableList<String>,
    private val context: Context
) : RecyclerView.Adapter<cartadapter.CartViewHOlder>() {
    //initialize firebase auth
    private val auth = FirebaseAuth.getInstance()

    init {
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartitems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemReference = database.reference.child("user").child(userId).child("CartItem")
    }


    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemReference: DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHOlder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHOlder(binding)
    }

    override fun getItemCount(): Int {
        return cartitems.size
    }

    override fun onBindViewHolder(holder: CartViewHOlder, position: Int) {

        holder.bind(position)

    }
    //get updated quantity
    fun getItemQuantities(): MutableList<Int> {
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(carQuantity)
        return itemQuantity
    }

    inner class CartViewHOlder(private val binding: CartItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                menuFoodName.text = cartitems[position]
                cartprice.text = cartItemprice[position]

//                load image
                val uriString = cartImage[position]
                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartImages)

                cartItemQuantity.text = quantity.toString()

                minus.setOnClickListener {
                    decreaseQuantity(position)
                }
                plus.setOnClickListener {
                    increseQuantity(position)
                }
                delete.setOnClickListener {

                    val itemPosition = adapterPosition
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        deleteItem(itemPosition)
                    }
                }

            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                carQuantity[position] = itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }
        }

        private fun increseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                carQuantity[position] = itemQuantities[position]
                binding.cartItemQuantity.text = itemQuantities[position].toString()
            }

        }

        private fun deleteItem(position: Int) {
            getUniqueKeyAtPosition(position) { uniqueKey ->
                if (!uniqueKey.isNullOrBlank()) {  // Check if the key is valid
                    removeItem(position, uniqueKey)
                } else {
                    Toast.makeText(context, "Failed to delete item. Key not found.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        private fun removeItem(position: Int, uniqueKey: String) {
            cartItemReference.child(uniqueKey).removeValue().addOnSuccessListener {
                if (position < cartitems.size) {  // Prevent IndexOutOfBoundsException
                    cartitems.removeAt(position)  // Remove from main list only
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartitems.size)
                    Toast.makeText(context, "Item deleted successfully", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to delete item.", Toast.LENGTH_SHORT).show()
            }
        }


        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uniqueKey = snapshot.children.elementAtOrNull(positionRetrieve)?.key  // Safe access
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    onComplete(null)
                }
            })
        }

    }


}