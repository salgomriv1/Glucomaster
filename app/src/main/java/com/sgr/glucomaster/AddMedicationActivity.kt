package com.sgr.glucomaster

import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.sgr.glucomaster.databinding.ActivityAddMedicationBinding

class AddMedicationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMedicationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddMedicationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val spinnerTurn = binding.spAddMedTurn

        //Data to show on spinner
        val medicaciones = arrayOf("Desayuno", "Comida", "Cena", "Resopón")

        //Create an adapter and a spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicaciones)

        //Specify the layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Asign the adapter to the spinner
        spinnerTurn.adapter = adapter
    }
}