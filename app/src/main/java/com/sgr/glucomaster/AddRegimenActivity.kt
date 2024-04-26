package com.sgr.glucomaster

import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sgr.glucomaster.databinding.ActivityAddRegimenBinding

class AddRegimenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddRegimenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddRegimenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerDes = binding.spDesayuno

        //Data to show on spinner
        val medicaciones = arrayOf("Medicación1", "Medicación2", "Medicación3")

        //Create an adapter and a spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicaciones)

        //Specify the layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Asign the adapter to the spinner
        spinnerDes.adapter = adapter


    }
}