package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.myapplication.databinding.ActivitySingupBinding
import com.example.myapplication.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.math.E

class singupActivity : AppCompatActivity() {

    private lateinit var Email: String
    private lateinit var Password: String
    private lateinit var UserName: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivitySingupBinding by lazy {
        ActivitySingupBinding.inflate(layoutInflater)
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
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()


        //        initialize datatbase auth
        auth = Firebase.auth
//        initialize firebase database
        database = Firebase.database.reference
        //        initialize firebase database
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)





        binding.signInButton.setOnClickListener {
            UserName = binding.userName.text.toString()
            Email = binding.email.text.toString().trim()
            Password = binding.password.text.toString().trim()


            if (Email.isBlank() || Password.isBlank() || UserName.isBlank()) {
                Toast.makeText(this, "fill all the detail", Toast.LENGTH_SHORT).show()
            } else {
                CreateAccount(Email, Password)
            }
        }

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


        binding.btGoogle.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }


    }

    //    launcher for google sign in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount? = task.result
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account?.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this, "signIn fieled", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "signIn fieled", Toast.LENGTH_SHORT).show()
            }
        }

    private fun CreateAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Account created successfully", Toast.LENGTH_SHORT).show()
                saveUserData()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Account creation fieled", Toast.LENGTH_SHORT).show()
                Log.d("Account", "createAccount: Failuare", task.exception)
            }

        }
    }

    private fun saveUserData() {
//        retrieve data from edit text
        UserName = binding.userName.text.toString()
        Password = binding.password.text.toString().trim()
        Email = binding.email.text.toString().trim()

        val user = UserModel(UserName, Email, Password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//        save data to firebase database
        database.child("user").child(userId).setValue(user)
    }
}