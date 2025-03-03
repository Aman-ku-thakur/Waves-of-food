package com.example.food

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.food.Adapter.AllItemAdapter
import com.example.food.databinding.ActivityAllItemAactivityBinding
import com.example.food.model.AllMenu
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AllItemAactivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var database: FirebaseDatabase
    private  var  menuItem:ArrayList<AllMenu> = ArrayList()


    private val binding : ActivityAllItemAactivityBinding by lazy {
        ActivityAllItemAactivityBinding.inflate(layoutInflater)
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

        databaseReference = FirebaseDatabase.getInstance().reference
//        all item retrieve from database
        retrieveMenu()



//        val menuFoodName = listOf("aman","abhay","atul","anirudhha","tapasya")
//        val menuFoodPrice = listOf("10$","20$","30$","40$","50$")
//        val menuFoodImage = listOf(R.drawable.banner1,R.drawable.banner2,R.drawable.banner3,R.drawable.banner4,R.drawable.banner1)
//

//        val adapter = AllItemAdapter(
//            ArrayList(menuFoodName),  ArrayList(menuFoodPrice),  ArrayList(menuFoodImage)
//        )



        binding.imageButton.setOnClickListener{
            finish()
        }


    }

    private fun retrieveMenu() {
        database= FirebaseDatabase.getInstance()
        val foodRef :DatabaseReference = database.reference.child("menu")

//        fetch data from database
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
//clear all databefore populating
                menuItem.clear()
//                loop for through each food item
                for(foodSnapShot in snapshot.children){
                    val menuItems :AllMenu? = foodSnapShot.getValue(AllMenu::class.java)
                   menuItems?.let {
                       menuItem.add(it)

                   }
                }
                setAdapter()

            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("DatabaseError","Error: ${error.message}")
            }
        })
    }

    private fun setAdapter() {

        val adapter = AllItemAdapter(
      this@AllItemAactivity,menuItem,databaseReference){position ->
            deleteMEnuItem(position)
        }


            binding.menuREcyclerView.layoutManager = LinearLayoutManager(this)
        binding.menuREcyclerView.adapter=adapter
    }

    private fun deleteMEnuItem(position: Int) {

        val  menuItemToDelete = menuItem[position]
        val menuItemKey = menuItemToDelete.key
        val foodMenuReferenc = database.reference.child("menu").child(menuItemKey!!)
        foodMenuReferenc.removeValue().addOnCompleteListener {
            task->
            if(task.isSuccessful){
                menuItem.removeAt(position)
                binding.menuREcyclerView.adapter?.notifyItemRemoved(position)
            }else{
                Toast.makeText(this, "item not deleted ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}