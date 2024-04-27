package com.sgr.glucomaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.sgr.glucomaster.databinding.ActivityCurrentRegimenBinding
import com.sgr.glucomaster.db.Restriccion

class CurrentRegimenActivity : AppCompatActivity() {

    //Variables
    lateinit var mRecyclerView: RecyclerView
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    private lateinit var binding: ActivityCurrentRegimenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCurrentRegimenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //Recieve actual regiment date
        val pautaActualFecha = intent.getStringExtra("pauta_actual")
        val userId = intent.getLongExtra("userid", 0)

        if (!pautaActualFecha.equals("")
            && !pautaActualFecha.equals("--/--/----")
            && !pautaActualFecha.isNullOrEmpty()) {

            val pautaActualId = database.pautaQueries.getRegByDate(pautaActualFecha, userId).executeAsOne().id

            binding.tvTitleFechaData.setText(pautaActualFecha)

            //Meds are obtained from db and shown
            val medDesayuno = database.pauta_medicacionQueries.getMedbyReg(pautaActualId,"desayuno").executeAsOne()
            binding.tvMedicacion.setText(medDesayuno.nombre)
            val medComida = database.pauta_medicacionQueries.getMedbyReg(pautaActualId,"comida").executeAsOne()
            binding.tvMedicacionT.setText(medComida.nombre)
            val medCena = database.pauta_medicacionQueries.getMedbyReg(pautaActualId,"cena").executeAsOne()
            binding.tvMedicacionC.setText(medCena.nombre)
            val medResopon = database.pauta_medicacionQueries.getMedbyReg(pautaActualId,"resopon").executeAsOne()
            binding.tvMedicacionR.setText(medResopon.nombre)

            //Restrictions are obtained from db
            val marDesayuno = database.restriccionQueries.getAllRestByTurn("desayuno",pautaActualId).executeAsList()
            val marComida = database.restriccionQueries.getAllRestByTurn("comida",pautaActualId).executeAsList()
            val marCena = database.restriccionQueries.getAllRestByTurn("cena",pautaActualId).executeAsList()
            val marResopon = database.restriccionQueries.getAllRestByTurn("resopon",pautaActualId).executeAsList()

            //Setup all recyclerviews
            setupRecyclerView(R.id.tMRecycler, buildRestList(marDesayuno))
            setupRecyclerView(R.id.tMRecyclerT, buildRestList(marComida))
            setupRecyclerView(R.id.tMRecyclerC, buildRestList(marCena))
            setupRecyclerView(R.id.tMRecyclerR, buildRestList(marResopon))
        }


        binding.btnCambiar.setOnClickListener {

            val intent = Intent (this, ChangeRegimenActivity::class.java)
            startActivity(intent)
        }
    }

    fun setupRecyclerView(Recycler: Int, list: MutableList<String>) {

        val mAdapter : MAdapter = MAdapter()
        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        mAdapter.MAdapter(list, this)
        mRecyclerView.adapter = mAdapter

    }

    //Build the restriction lists for recyclerviews
    fun buildRestList(lista: List<Restriccion>): ArrayList<String> {

        val listaFinal = ArrayList<String>()

        for (item in lista) {

            val restric = "${item.LimiteInferior} - ${item.LimiteSuperior}mg/l   Dosis: ${item.dosis}UD"
            listaFinal.add(restric)
        }

        return listaFinal
    }
}