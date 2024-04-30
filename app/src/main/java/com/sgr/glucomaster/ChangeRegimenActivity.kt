package com.sgr.glucomaster

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.auth.FirebaseAuth
import com.sgr.glucomaster.databinding.ActivityChangeRegimenBinding
import com.sgr.glucomaster.db.Pauta

class ChangeRegimenActivity : AppCompatActivity(), SAdapter.OnDeleteItemClickListener, SAdapter.OnItemClickedListener {

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

        refreshList()
    }

    //Function to refresh recyclerView
    private fun refreshList() {

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

        var sAdapter : SAdapter = SAdapter(this, this)
        mRecyclerView = findViewById(Recycler) as RecyclerView
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.layoutManager = LinearLayoutManager(this)
        sAdapter.SAdapter(list, this)
        mRecyclerView.adapter = sAdapter

    }

    //Function to manage recyclerView's item delete button and delete the regimen
    override fun onDeleteItemClick(item: String) {

        showConfirmationDialog(item)

    }

    //Function to show a confirmation dialog
    private fun showConfirmationDialog(item: String) {

        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.confirmarBorradoTitulo))
        builder.setMessage(getString(R.string.confirmarBorrado))

        builder.setPositiveButton(getString(R.string.si)) {
            dialog, which ->
            auth = FirebaseAuth.getInstance()
            val userEmail = auth.currentUser?.email
            val userId = database.userQueries.getUser(userEmail).executeAsOne().id
            val pauta_id = database.pautaQueries.getRegByDate(item, userId).executeAsOne().id

            //Delete restrictions for that regimen
            database.restriccionQueries.deleteRestByRegId(pauta_id)
            //Delete pauta_medicacion for that regimen
            database.pauta_medicacionQueries.deleteById(pauta_id)
            //Delete regimen
            database.pautaQueries.deleteRegById(pauta_id)
            //Refresh list
            refreshList()
        }

        builder.setNegativeButton(getString(R.string.no)) {
            dialog, which ->
            //Does nothing
        }

        val dialog = builder.create()
        dialog.show()
    }

    //Function to manage recyclerView's item click, and change to selected regimen
    override fun onItemClicked(item: String) {

        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("cambioPauta", item)
        startActivity(intent)
    }

}