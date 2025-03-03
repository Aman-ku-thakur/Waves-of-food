package com.example.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.databinding.ActivitySignUpBinding
import com.example.food.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth


import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class signUpActivity : AppCompatActivity() {


    private lateinit var auth : FirebaseAuth
    private lateinit var email: String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var nameOfResetorent:String
    private lateinit var database: DatabaseReference

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        initialization firebase
        auth = Firebase.auth
//        initialize firease database
        database =Firebase.database.reference


        binding.creatAccount.setOnClickListener{
           // get text from edit text
            username = binding.name.text.toString().trim()
            nameOfResetorent = binding.restorentName.text.toString().trim()
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if(username.isBlank()|| nameOfResetorent.isBlank() || password.isBlank()){
                Toast.makeText(this, "Fill all detail", Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }


        }







        val loctionList = arrayOf("mandla","khaddeora","chauraha","padmi")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,loctionList)

        val autoCompleteTextView =binding.listOfLocation1
        autoCompleteTextView.setAdapter(adapter)





        binding.alreadyHaveAccount.setOnClickListener{
            val intent = Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }

    }

    private fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->

            if (task.isSuccessful){
                Toast.makeText(this, "account crated successfully", Toast.LENGTH_SHORT).show()

                saveUserData()
                val intent = Intent(this,LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Account creation fieled", Toast.LENGTH_SHORT).show()
                Log.d("Account","createAcount:Failure",task.exception)
            }
        }
    }
// save data into database
    private fun saveUserData() {
//        get text from edit text
        username = binding.name.text.toString().trim()
        nameOfResetorent = binding.restorentName.text.toString().trim()
        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user= UserModel(username,nameOfResetorent,email,password)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid

    // save user data firebase database
        database.child("user").child(userID).setValue(user)
    }
}