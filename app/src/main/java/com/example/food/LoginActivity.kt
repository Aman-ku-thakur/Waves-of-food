package com.example.food

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.databinding.ActivityLoginBinding
import com.example.food.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {
    private var username: String? = null
    private var nameOfResetorent: String? = null
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

//        initialize auth
        auth = Firebase.auth
        database = Firebase.database.reference


//        initializing google sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)


        binding.btGoogle.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)

        }


        binding.loginButton.setOnClickListener {

//            get text from edittext
            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()

            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all detail", Toast.LENGTH_SHORT).show()
            } else {
                creatuseracount(email, password)
            }
        }
        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, signUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun creatuseracount(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

            if (task.isSuccessful) {
                val user = auth.currentUser
                Toast.makeText(this, "login successfull", Toast.LENGTH_SHORT).show()
                updateUi(user)
            } else {
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        Toast.makeText(this, "login fail", Toast.LENGTH_SHORT).show()
                        saveUSerData()
                        updateUi(user)
                    } else {
                        Toast.makeText(this, "authentication failed", Toast.LENGTH_SHORT).show()
                        Log.d(
                            "Account",
                            "createUSerAccount:Authentification fieled",
                            task.exception
                        )
                    }
                }
            }

        }
    }

    private fun saveUSerData() {
//        get text from edittext

        email = binding.email.text.toString().trim()
        password = binding.password.text.toString().trim()

        val user = UserModel(username, nameOfResetorent, email, password)
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId.let {
            if (it != null) {
                database.child("user").child(it).setValue(user)
            }
        }
    }



    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)

                    auth.signInWithCredential(credential).addOnCompleteListener { authtask ->
                        if (authtask.isSuccessful) {
//
                            Toast.makeText(
                                this,
                                "successfully signed in with google",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUi(authtask.result?.user)
                            finish()
                        } else {
                            Toast.makeText(this, "google sign in failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "google sign in failed", Toast.LENGTH_SHORT).show()
                }
            }
        }
    // check if user is already logged in
    override fun onStart() {
        super.onStart()
        val currentuser = auth.currentUser
        if (currentuser!=null){
                startActivity(Intent(this, MainActivity::class.java))
                finish()
        }
    }
    private fun updateUi(user: FirebaseUser?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}