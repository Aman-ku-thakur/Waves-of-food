package com.example.myapplication.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.menuAdapter
import com.example.myapplication.databinding.FragmentSearchBinding
import com.example.myapplication.model.MenuItems
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class searchFragment : Fragment() {

    private lateinit var adapter: menuAdapter
    private lateinit var binding: FragmentSearchBinding
    private lateinit var database: FirebaseDatabase
    private val originalvaluItem = mutableListOf<MenuItems>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)


//        retrieve menu item from the database
        retrieveMenuitem()

        // set up for search view
        setUpSeaarchView()




        return binding.root
    }

    private fun retrieveMenuitem() {
//        get database reference
        database = FirebaseDatabase.getInstance()
//        reference to the menu node
        val foodReference = database.reference.child("menu")
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children) {
                    val menuItam = foodsnapshot.getValue(MenuItems::class.java)
                    menuItam?.let {
                        originalvaluItem.add(it)
                    }
                }
                showAllMEnu()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

    }

    private fun showAllMEnu() {
        val filteredMenuItem = ArrayList(originalvaluItem)
        setAdapter(filteredMenuItem)
    }

    private fun setAdapter(filteredMenuItem: List<MenuItems>) {

        adapter = menuAdapter(filteredMenuItem, requireContext())
        binding.menurecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.menurecyclerview.adapter = adapter
    }


    private fun setUpSeaarchView() {
        binding.searchView.setOnQueryTextListener(object :
            android.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItem(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItem(newText)
                return true
            }
        })

    }

    private fun filterMenuItem(query: String) {

        val filteredMenuItems = originalvaluItem.filter {
            it.foodName?.contains(query, ignoreCase = true) == true
        }
        setAdapter(filteredMenuItems)

    }

    
}