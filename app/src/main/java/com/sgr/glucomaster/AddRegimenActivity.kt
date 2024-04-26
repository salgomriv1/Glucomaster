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
import com.sgr.glucomaster.databinding.ActivityAddRegimenBinding

class AddRegimenActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityAddRegimenBinding
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    lateinit var mRecyclerView: RecyclerView
    private val mAdapter : MAdapter = MAdapter()
    private val resDes = ArrayList<String>()
    private val resCom = ArrayList<String>()
    private val resCen = ArrayList<String>()
    private val resRes = ArrayList<String>()

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

        //Setup spinner and update restrictions spinners
        setupSpinners()
        updatelist(R.id.recyclerView, resDes)
        updatelist(R.id.recyclerViewComida, resCom)
        updatelist(R.id.recyclerViewCena, resCen)
        updatelist(R.id.recyclerViewResopon, resRes)


        binding.btnARDesayuno.setOnClickListener {

            if (binding.etLimiteInfDesayuno.text.isNotEmpty()
                && binding.etLimiteSupDesayuno.text.isNotEmpty()
                && binding.etDosisDesayuno.text.isNotEmpty()) {

                resDes.add(binding.etLimiteInfDesayuno.text.toString() + " " +
                binding.etLimiteSupDesayuno.text.toString() + " " +
                binding.etDosisDesayuno.text.toString())
                //updatelist()
            } else {

                Toast.makeText(baseContext, getString(R.string.resVacio), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Function for setting up recyclerviews
    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.MAdapter(list, this)
        mRecyclerView.adapter = mAdapter

    }

    //Function to setup the spinners
    fun setupSpinners() {
        //Get all the meds from db
        val listaMedicaciones = database.medicacionQueries.getAllMed().executeAsList()
        val medicaciones = ArrayList<String>()
        val spinnerDes = binding.spDesayuno
        val spinnerCom = binding.spComida
        val spinnerCen = binding.spCena
        val spinnerRes = binding.spResopon

        //Check if the list in not empty
        if (listaMedicaciones.isNotEmpty()) {

            //Data to show on spinner
            for (medicacion in listaMedicaciones) {
                medicaciones.add(medicacion.nombre)
            }
        } else {

            medicaciones.add("Sin medicaciones")
        }

        //Create an adapter and a spinner layout
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicaciones)

        //Specify the layout
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        //Asign the adapter to the spinners
        spinnerDes.adapter = adapter
        spinnerCom.adapter = adapter
        spinnerCen.adapter = adapter
        spinnerRes.adapter = adapter
    }

    //Function to update the recyclerviews lists
    fun updatelist(recyclerView: Int, listaRes: ArrayList<String>) {

        if (listaRes.isEmpty()) {

            val listaVacia: MutableList<String> = mutableListOf("Sin margenes")
            setupRecyclerView(recyclerView, listaVacia)
        } else {


        }
    }
}