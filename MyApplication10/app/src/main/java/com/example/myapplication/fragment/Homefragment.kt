package com.example.myapplication.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.myapplication.Adapter.menuAdapter
import com.example.myapplication.Adapter.popularAdapter


import com.example.myapplication.R
import com.example.myapplication.databinding.FragmentHomefragmentBinding
import com.example.myapplication.menubottumsheetfragment
import com.example.myapplication.model.MenuItems
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Homefragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems:MutableList<MenuItems>
private lateinit var binding  : FragmentHomefragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomefragmentBinding.inflate(inflater, container, false)

        binding.viewMenu.setOnClickListener{
            val bottomSheetDialog = menubottumsheetfragment()
            bottomSheetDialog.show(parentFragmentManager," Test")
        }


//      retrievePopulerMenuItem
        retreiveMenuAndDisplayItem()




        return binding.root

    }

    private fun retreiveMenuAndDisplayItem() {
        database =FirebaseDatabase.getInstance()
        val foodRef :DatabaseReference = database.reference.child("menu")
        menuItems  = mutableListOf()
//        retrieve menu item from the databse
        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot in snapshot.children){
                    val menuItem = foodsnapshot.getValue(MenuItems::class.java)
                    menuItem?.let {
                        menuItems.add(it)
                    }
                }
//                display a random populer item
                randomPopulerItem()

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun randomPopulerItem() {
//        create as shuffled list of menu items
        val index = menuItems.indices.toList().shuffled()
        val numItemToShow = 6
        val  subsetManuItem = index.take(numItemToShow).map { menuItems[it] }

        setPopulerItemAdapter(subsetManuItem)
    }

    private fun setPopulerItemAdapter(subsetManuItem: List<MenuItems>) {
//         popularAdapter(foodname,price,populerFoodIMages,requireContext())
        val adapter =menuAdapter(subsetManuItem,requireContext())
        binding.populerRecyclerview.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.populerRecyclerview.adapter = adapter

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3,ScaleTypes.FIT))

        val imageSlider = binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)

//        for clicking on item

        imageSlider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {

            }

            override fun onItemSelected(position: Int) {
                val itemPosition = imageList[position]
                val itemMessage = "Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })



    }





}
