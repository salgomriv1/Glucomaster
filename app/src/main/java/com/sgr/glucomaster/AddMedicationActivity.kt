package com.sgr.glucomaster

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sgr.glucomaster.databinding.ActivityAddMedicationBinding
import com.sgr.glucomaster.db.Medicacion

class AddMedicationActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityAddMedicationBinding
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    lateinit var mRecyclerView: RecyclerView
    private val mAdapter : MAdapter = MAdapter()
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

        //Update the list
        updateMedList()

        binding.btnAddMedication.setOnClickListener {

            //Check the medication field is not empty
            if ( binding.etAddMed.text.isNotEmpty()) {

                //Insert medication in the db
                database.medicacionQueries.insertMedication(binding.etAddMed.text.toString())
                //Update list
                updateMedList()
                //Message to confirm medication added
                Toast.makeText(baseContext, getString(R.string.medAnyadida), Toast.LENGTH_SHORT).show()
            } else {

                Toast.makeText(baseContext, getString(R.string.medNoVacio), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Method for setuping the recyclerView
    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.MAdapter(list, this)
        mRecyclerView.adapter = mAdapter

    }

    //Method for update the list
    fun updateMedList() {

        //Get all meds in bd
        val medicamentos: List<Medicacion> = database.medicacionQueries.getAllMed().executeAsList()
        val listaMedicamentos: MutableList<String> = mutableListOf()

        //Check if the list is empty
        if (medicamentos.isEmpty()) {

            val listaVacia: MutableList<String> = mutableListOf("Sin medicaciones")
            setupRecyclerView(R.id.rvMedication, listaVacia)
        }else {

            //Fill a list with the name of the medication
            for (medicamento in medicamentos) {
                listaMedicamentos.add(medicamento.nombre)
            }

            //Setup the recyclerview with the data
            setupRecyclerView(R.id.rvMedication, listaMedicamentos)
        }
    }
}