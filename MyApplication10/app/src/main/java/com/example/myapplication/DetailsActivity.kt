package com.example.myapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.util.GlideSuppliers.GlideSupplier
import com.example.myapplication.databinding.ActivityDetailsBinding
import com.example.myapplication.model.CartItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    private var foodName: String? = null
    private var foodImage: String? = null
    private var fooddescription: String? = null
    private var foodPrice: String? = null
    private var foodIngridient: String? = null
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailsBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


//        initialize firebase auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("MenuItemName")
        fooddescription = intent.getStringExtra("MenuItemDescription")
        foodPrice = intent.getStringExtra("MenuItemPrice")
        foodIngridient = intent.getStringExtra("MenuItemIngridient")
        foodImage = intent.getStringExtra("MenuItemImage")

        with(binding) {
            detailfoodname.text = foodName
            descriptiontextbox.text = fooddescription
            ingridient.text = foodIngridient
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage))
                .into(detailfoodimage) // for image show on the places of imagefield

        }


        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.addToCard.setOnClickListener {
            addtocard()
        }
    }

    private fun addtocard() {
        val database  = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""  //current user aa jayga
//        create a cart item object
        val cardItem =CartItem(foodName.toString(),foodPrice.toString(),fooddescription.toString(),foodImage.toString(),1)
//        save data to cart item to firebase
        database.child("user").child(userId).child("CartItem").push().setValue(cardItem).addOnSuccessListener {
            Toast.makeText(this, "Item added into cart", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "aitem not added successfully", Toast.LENGTH_SHORT).show()
        }
    }
}