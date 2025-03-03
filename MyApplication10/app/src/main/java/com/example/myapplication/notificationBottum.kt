package com.example.myapplication

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.Adapter.NotificationAdapter
import com.example.myapplication.databinding.FragmentNotificationBottumBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.util.ArrayList


class notificationBottum : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNotificationBottumBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottumBinding.inflate(layoutInflater, container, false)

        val notification = listOf("Your order has been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationimg =  listOf(R.drawable.sademoji,R.drawable.gadi,R.drawable.congretrs)

        val adapter = NotificationAdapter(
          ArrayList(notification),ArrayList(notificationimg)
        )

        binding.notificationRecyclerView.adapter = adapter
        binding.notificationRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        return binding.root
    }

    companion object {

    }
}