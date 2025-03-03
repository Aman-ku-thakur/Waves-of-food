package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class LoginActivity : AppCompatActivity() {

    private lateinit var Email: String
    private  var UserName: String? = null
    private lateinit var Password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var GoogleSignInClient: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
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
//        initiallization

        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        auth = Firebase.auth
        database = Firebase.database.reference
        GoogleSignInClient = GoogleSignIn.getClient(this,googleSignInOption)




        binding.login.setOnClickListener {
//            get data from text field

            Email = binding.email.text.toString().trim()
            Password = binding.password.text.toString().trim()

            if (Email.isBlank()||Password.isBlank()){
                Toast.makeText(this, "Please enteer all the detail", Toast.LENGTH_SHORT).show()
            }else{
                createUser()

                Toast.makeText(this, "LoginSuccessfull", Toast.LENGTH_SHORT).show()
            }


        }

        binding.domothaveaccount.setOnClickListener {
            val intent = Intent(this, singupActivity::class.java)
            startActivity(intent)
        }


        binding.btGoogle.setOnClickListener{
            val signInIntent = GoogleSignInClient.signInIntent
            launcher.launch(signInIntent)
        }
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
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

    private fun createUser() {
        auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener { task->
            if (task.isSuccessful){
                val user:FirebaseUser? = auth.currentUser
                updateUi(user)
            }else{
                auth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener { task->
                    if (task.isSuccessful) {
                        saveUserData()
                        val user = auth.currentUser
                        updateUi(user)

                    }else{
                        Toast.makeText(this, "signinfield", Toast.LENGTH_SHORT).show()
                    }
                }

            }
        }
    }

    private fun saveUserData() {
        //            get data from text field
        Email = binding.email.text.toString().trim()
        Password = binding.password.text.toString().trim()

        val user = UserModel(UserName,Email,Password)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

//        save data into database

        database.child("user").child(userId).setValue(user)


    }

    override fun onStart() {
        super.onStart()
        val currentuser = auth.currentUser
        if (currentuser!=null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun updateUi(user:FirebaseUser?) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}