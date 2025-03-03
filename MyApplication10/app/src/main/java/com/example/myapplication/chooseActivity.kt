package com.example.myapplication

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.databinding.ActivityChooseBinding

class chooseActivity : AppCompatActivity() {
    private val binding: ActivityChooseBinding by lazy {
        ActivityChooseBinding.inflate(layoutInflater)
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

        val locationList: Array<String> = arrayOf("mandla"," jabalpur","indore","bhopal")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)

     val autoCompleteTextView : AutoCompleteTextView= binding.ListofLocation
     autoCompleteTextView.setAdapter(adapter)
    }
}