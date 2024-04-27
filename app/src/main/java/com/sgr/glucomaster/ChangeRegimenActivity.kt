package com.sgr.glucomaster

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.auth.FirebaseAuth
import com.sgr.glucomaster.databinding.ActivityChangeRegimenBinding
import com.sgr.glucomaster.db.Pauta

class ChangeRegimenActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityChangeRegimenBinding
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    lateinit var mRecyclerView: RecyclerView
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityChangeRegimenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        val userEmail = auth.currentUser?.email
        val userId = database.userQueries.getUser(userEmail).executeAsOne().id

        val lista = getRegimens(userId)

        setupRecyclerView(R.id.rvPautas, lista)
    }

    //Function to get all regimen's date
    fun getRegimens(userId: Long): ArrayList<String> {

        val listaReg: List<Pauta> = database.pautaQueries.getAllRegByUserId(userId).executeAsList()
        val listaFinal = ArrayList<String>()

        for (item in listaReg) {

            listaFinal.add(item.fecha)
        }

        return listaFinal
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
}