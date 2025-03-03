package com.example.food

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.databinding.ActivityAddItemBinding
import com.example.food.model.AllMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class AddItemActivity : AppCompatActivity() {

    // food item detail

    private lateinit var foodName:String
    private lateinit var foodPrice:String
    private lateinit var foodDescription:String
    private lateinit var foodIngridient:String
    private var foodImage: Uri? = null

//    firebase

    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseDatabase


    private lateinit var binding: ActivityAddItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddItemBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        initialize auth
        auth = FirebaseAuth.getInstance()
//        initialize firebase database instance
        database = FirebaseDatabase.getInstance()


        binding.addItemButton.setOnClickListener{
//            get data from edit text
            foodName = binding.FoodName.text.toString().trim()
            foodPrice = binding.FoodPrice.text.toString().trim()
            foodDescription = binding.shortDescription.text.toString().trim()
            foodIngridient = binding.ingredients.text.toString().trim()

            if (!(foodName.isBlank()||foodPrice.isBlank()||foodDescription.isBlank()||foodIngridient.isBlank())){
                uploadData()
                Toast.makeText(this, "Item added successfully ", Toast.LENGTH_SHORT).show()
                finish()
            }else{
                Toast.makeText(this, "Fill all the detail", Toast.LENGTH_SHORT).show()
            }

        }
        //for image
        binding.selectImage.setOnClickListener {
            pickImage.launch("image/*")
        }





        binding.imageButton.setOnClickListener {
            finish()
        }

    }

    private fun uploadData() {
        // get a reference to the "menu" node in the database
        val menuRef = database.getReference("menu")
        //Generate a uniqua key for the new menu item
        val menuItemKey = menuRef.push().key

        if (foodImage != null){
val storegeRef = FirebaseStorage.getInstance().reference
            val imageRef = storegeRef.child("menu_images/${menuItemKey}.jpg")
            val uploadTask = imageRef.putFile(foodImage!!)


            uploadTask.addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener {
                    downloadUrl->
//                    create a new mwnu item
                    val newItem = AllMenu(
                        menuItemKey,
                        foodName =foodName,
                        foodPrice = foodPrice,
                        foodDescription =foodDescription,
                        foodIngredient = foodIngridient,
                        foodImage = downloadUrl.toString()
                    )

                    menuItemKey?.let {
                        key->
                        menuRef.child(key).setValue(newItem).addOnSuccessListener {
                            Toast.makeText(this, "data uploaded successfully", Toast.LENGTH_SHORT).show()
                        }
                            .addOnFailureListener {
                                Toast.makeText(this, "data not uploaded successfully", Toast.LENGTH_SHORT).show()

                            }
                    }

                }

            }

                .addOnFailureListener {
                    Toast.makeText(this, "image upload fail", Toast.LENGTH_SHORT).show()

                }

        }else{

                Toast.makeText(this, "please sellect an image", Toast.LENGTH_SHORT).show()


        }

    }

   private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->

        if (uri != null) {
            binding.selectedimage.setImageURI(uri)
            foodImage = uri

        }
    }


}