package com.example.food

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.databinding.ActivityAdminProfileBinding
import com.example.food.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfileActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var adminReference: DatabaseReference
    private val binding: ActivityAdminProfileBinding by lazy {
        ActivityAdminProfileBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        adminReference = database.reference.child("user")

        binding.imageButton.setOnClickListener {
            finish()
        }

        binding.saveInformation.setOnClickListener {
            updateUserData()
        }

        binding.etname.isEnabled = false
        binding.address.isEnabled = false
        binding.etemail.isEnabled = false
        binding.etpassword.isEnabled = false
        binding.etPhone.isEnabled = false
        binding.saveInformation.isEnabled = false

        var isEnable = false
        binding.editbutton.setOnClickListener {
            isEnable = !isEnable

            binding.etname.isEnabled = isEnable
            binding.address.isEnabled = isEnable
            binding.etemail.isEnabled = isEnable
            binding.etpassword.isEnabled = isEnable
            binding.etPhone.isEnabled = isEnable

            if (isEnable) {
                binding.etname.requestFocus()
            }
            binding.saveInformation.isEnabled = isEnable

        }
        retrieveUserData()
    }


    private fun retrieveUserData() {

        val currentUser = auth.currentUser?.uid
        if (currentUser != null) {
            val userReference = adminReference.child(currentUser)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        var ownerNAme = snapshot.child("name").getValue()
                        var email = snapshot.child("email").getValue()
                        var password = snapshot.child("password").getValue()
                        var address = snapshot.child("address").getValue()
                        var phone = snapshot.child("phone").getValue()
                        setDataToTextView(ownerNAme, email, password, address, phone)
                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }
    private fun updateUserData() {
        var updateNAme = binding.etname.text.toString()
        var UpdateEmail = binding.etemail.text.toString()
        var UpdatePhone = binding.etPhone.text.toString()
        var updatePassword = binding.etpassword.text.toString()
        var Updateaddress = binding.address.text.toString()

        val currentUSerUid = auth.currentUser?.uid
        if (currentUSerUid != null) {
            val userReference = adminReference.child(currentUSerUid)
            userReference.child("name").setValue(updateNAme)
            userReference.child("email").setValue(UpdateEmail)
            userReference.child("address").setValue(Updateaddress)
            userReference.child("password").setValue(updatePassword)
            userReference.child("phone").setValue(UpdatePhone)

            Toast.makeText(this, "Profile update successfull😍😍😍", Toast.LENGTH_SHORT).show()
//            update the email and password for firebase authantication
            auth.currentUser?.updateEmail(UpdateEmail)
            auth.currentUser?.updatePassword(updatePassword)
        } else {
            Toast.makeText(this, "Profile update fils😍😍😍", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setDataToTextView(
        ownerNAme: Any?,
        email: Any?,
        password: Any?,
        address: Any?,
        phone: Any?
    ) {
        binding.etname.setText(ownerNAme.toString())
        binding.etemail.setText(email.toString())
        binding.etPhone.setText(phone.toString())
        binding.etpassword.setText(password.toString())
        binding.address.setText(address.toString())
    }
}