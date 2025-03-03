package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myapplication.databinding.FragmentCongretsBottumSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CongretsBottumSheet : BottomSheetDialogFragment() {

private lateinit var binding : FragmentCongretsBottumSheetBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCongretsBottumSheetBinding.inflate(layoutInflater,container,false)


        binding.goHome.setOnClickListener{

            val intent = Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }

    companion object {

    }
}