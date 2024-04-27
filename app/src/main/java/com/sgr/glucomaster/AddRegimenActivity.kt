package com.sgr.glucomaster

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgr.glucomaster.databinding.ActivityAddRegimenBinding
import com.sgr.glucomaster.db.Restriccion

class AddRegimenActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityAddRegimenBinding
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    private lateinit var  auth: FirebaseAuth
    lateinit var mRecyclerView: RecyclerView
    private val medicaciones = ArrayList<String>()

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

        //Get current user
        auth = FirebaseAuth.getInstance()
        //Get user id
        val user= database.userQueries.getUser(auth.currentUser?.email).executeAsOneOrNull()?.id
        val user_id = user?.toLong()
        //Setup spinner and update restrictions spinners
        setupSpinners()
        updatelist(R.id.recyclerViewDesayuno, "desayuno", 0)
        updatelist(R.id.recyclerViewComida, "comida", 0)
        updatelist(R.id.recyclerViewCena, "cena", 0)
        updatelist(R.id.recyclerViewResopon, "resopon", 0)


        //Button to add a date
        binding.btnAddDate.setOnClickListener {

            //Check date field is not empty
            if (binding.etFecha.text.isNotEmpty()) {

                //Add a reg in the db
                if (user_id != null) {
                    database.pautaQueries.insertReg(binding.etFecha.text.toString(),user_id)
                    Toast.makeText(baseContext, getString(R.string.fechaAnyadida), Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(baseContext, getString(R.string.fechaVacia), Toast.LENGTH_SHORT).show()
            }
        }

        //Button to add rest to breakfast
        binding.btnARDesayuno.setOnClickListener {

            //Check if rest fields are not empty
            if (binding.etLimiteInfDesayuno.text.isNotEmpty()
                && binding.etLimiteSupDesayuno.text.isNotEmpty()
                && binding.etDosisDesayuno.text.isNotEmpty()) {

                //Get the id of the regimen
                val pauta_id = database.pautaQueries.getRegByDate(binding.etFecha.text.toString())
                    .executeAsOne().id.toLong()
                //Insert the rest in the db
                database.restriccionQueries.insertRest(pauta_id,
                    binding.etLimiteInfDesayuno.text.toString().toLong(),
                    binding.etLimiteSupDesayuno.text.toString().toLong(), "desayuno",
                    binding.etDosisDesayuno.text.toString().toLong())
                //Update the recyclerview
                updatelist(R.id.recyclerViewDesayuno, "desayuno", pauta_id)
            } else {

                Toast.makeText(baseContext, getString(R.string.resVacio), Toast.LENGTH_SHORT).show()
            }
        }

        //Button to add rest to lunch
        binding.btnARComida.setOnClickListener {

            //Check if rest fields are not empty
            if (binding.etLimiteInfComida.text.isNotEmpty()
                && binding.etLimiteSupComida.text.isNotEmpty()
                && binding.etDosisComida.text.isNotEmpty()) {

                //Get the id of the regimen
                val pauta_id = database.pautaQueries.getRegByDate(binding.etFecha.text.toString())
                    .executeAsOne().id.toLong()
                //Insert the rest in the db
                database.restriccionQueries.insertRest(pauta_id,
                    binding.etLimiteInfComida.text.toString().toLong(),
                    binding.etLimiteSupComida.text.toString().toLong(), "comida",
                    binding.etDosisComida.text.toString().toLong())
                //Update the recyclerview
                updatelist(R.id.recyclerViewComida, "comida", pauta_id)
            } else {

                Toast.makeText(baseContext, getString(R.string.resVacio), Toast.LENGTH_SHORT).show()
            }
        }

        //Button to add rest to dinner
        binding.btnARCena.setOnClickListener {

            //Check if rest fields are not empty
            if (binding.etLimiteInfCena.text.isNotEmpty()
                && binding.etLimiteSupCena.text.isNotEmpty()
                && binding.etDosisCena.text.isNotEmpty()) {

                //Get the id of the regimen
                val pauta_id = database.pautaQueries.getRegByDate(binding.etFecha.text.toString())
                    .executeAsOne().id.toLong()
                //Insert the rest in the db
                database.restriccionQueries.insertRest(pauta_id,
                    binding.etLimiteInfCena.text.toString().toLong(),
                    binding.etLimiteSupCena.text.toString().toLong(), "cena",
                    binding.etDosisCena.text.toString().toLong())
                //Update the recyclerview
                updatelist(R.id.recyclerViewCena, "cena", pauta_id)
            } else {

                Toast.makeText(baseContext, getString(R.string.resVacio), Toast.LENGTH_SHORT).show()
            }
        }

        //Button to add rest to resopon
        binding.btnARResopon.setOnClickListener {

            //Check if rest fields are not empty
            if (binding.etLimiteInfResopon.text.isNotEmpty()
                && binding.etLimiteSupResopon.text.isNotEmpty()
                && binding.etDosisResopon.text.isNotEmpty()) {

                //Get the id of the regimen
                val pauta_id = database.pautaQueries.getRegByDate(binding.etFecha.text.toString())
                    .executeAsOne().id.toLong()
                //Insert the rest in the db
                database.restriccionQueries.insertRest(pauta_id,
                    binding.etLimiteInfResopon.text.toString().toLong(),
                    binding.etLimiteSupResopon.text.toString().toLong(), "resopon",
                    binding.etDosisResopon.text.toString().toLong())
                //Update the recyclerview
                updatelist(R.id.recyclerViewResopon, "resopon", pauta_id)
            } else {

                Toast.makeText(baseContext, getString(R.string.resVacio), Toast.LENGTH_SHORT).show()
            }
        }

        //Button to finalize adding the regimen
        binding.btnAddReg.setOnClickListener {

            //Get selected med from the spinners
            val medDesayuno = binding.spDesayuno.selectedItem.toString()
            val medComida = binding.spComida.selectedItem.toString()
            val medCena = binding.spCena.selectedItem.toString()
            val medResopon = binding.spResopon.selectedItem.toString()

            //Check if there is any med
            if (!medDesayuno.equals("Sin medicaciones")
                && !medComida.equals("Sin medicaciones")
                && !medCena.equals("Sin medicaciones")
                && !medResopon.equals("Sin medicaciones")) {

                //Get the id of the med from the db
                val medDesayunoID =
                    database.medicacionQueries.getMedicationByName(medDesayuno).executeAsOne().id
                val medComidaID =
                    database.medicacionQueries.getMedicationByName(medComida).executeAsOne().id
                val medCenaID =
                    database.medicacionQueries.getMedicationByName(medCena).executeAsOne().id
                val medResoponID =
                    database.medicacionQueries.getMedicationByName(medResopon).executeAsOne().id

                //Get the id of the regimen from the db
                val pauta_id = database.pautaQueries.getRegByDate(binding.etFecha.text.toString())
                    .executeAsOne().id.toLong()

                //Add the relation between regimen and medication
                database.pauta_medicacionQueries.insertPauMed(pauta_id, medDesayunoID, "desayuno")
                database.pauta_medicacionQueries.insertPauMed(pauta_id, medComidaID, "comida")
                database.pauta_medicacionQueries.insertPauMed(pauta_id, medCenaID, "cena")
                database.pauta_medicacionQueries.insertPauMed(pauta_id, medResoponID, "resopon")

                finish()
                Toast.makeText(baseContext, getString(R.string.pautaAnyadida), Toast.LENGTH_SHORT)
                    .show()
            } else {

                Toast.makeText(baseContext, getString(R.string.sinMed), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Function for setting up recyclerviews
    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        var mAdapter : MAdapter = MAdapter()
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
    fun updatelist(rv: Int, turno: String, pauta_id: Long) {

        //Get all rest in the db
        val margenes: List<Restriccion> = database.restriccionQueries.getAllRestByTurn(turno, pauta_id).executeAsList()

        //Check if the list is empty
        if (margenes.isEmpty()) {

            val listaVacia: MutableList<String> = mutableListOf("Sin margenes")
            setupRecyclerView(rv, listaVacia)
        } else {

            val listaFinal = ArrayList<String>()
            //Iterate the rest list
            for (item in margenes) {

                //Create the string for the recyclerview
                val margen: String = item.LimiteInferior.toString() + " - " +
                        item.LimiteSuperior.toString() + "mg/l   Dosis: " + item.dosis
                //Add it to the list we will pass to the recyclerview
                listaFinal.add(margen)
            }
            //Setup the recyclerview
            setupRecyclerView(rv, listaFinal)
        }
    }
}