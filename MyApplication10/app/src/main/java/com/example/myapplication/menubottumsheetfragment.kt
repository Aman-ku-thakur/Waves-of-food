package com.example.myapplication

import android.os.Bundle
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.myapplication.Adapter.menuAdapter
import com.example.myapplication.databinding.FragmentMenubottumsheetfragmentBinding
import com.example.myapplication.model.MenuItems
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.math.log


class menubottumsheetfragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentMenubottumsheetfragmentBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems: MutableList<MenuItems>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMenubottumsheetfragmentBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        binding.backbutton.setOnClickListener {
            dismiss()
        }

        retrieveMenuItem()


        return binding.root
    }

    private fun retrieveMenuItem() {
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems = mutableListOf()
        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshote in snapshot.children) {
                    val menuItem = foodSnapshote.getValue(MenuItems::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
//            one the data recieve , set to adapter
                    setadapter()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun setadapter() {

        if (menuItems.isNotEmpty()){
            val adapter = menuAdapter(menuItems, requireContext())

            binding.menurecyclerview.layoutManager = LinearLayoutManager(requireContext())
            binding.menurecyclerview.adapter = adapter
            Log.d("item","setAdapter:data set ")
        }else{
            Log.d("item","setAdapter:data set ")
        }


    }
}


