package com.sgr.glucomaster

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.google.firebase.auth.FirebaseAuth
import com.sgr.glucomaster.databinding.ActivityMainBinding
import com.sgr.glucomaster.db.Usuario

class MainActivity : AppCompatActivity() {

    //Variables
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val database = Database(AndroidSqliteDriver(Database.Schema, this, "Database.db"))
    private lateinit var sharedPreferences: SharedPreferences

    //Recieves the date of the created regimen an sets as actual
    private val addRegActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        resultado ->
        if ( resultado.resultCode == Activity.RESULT_OK) {
            val data: Intent? = resultado.data
            binding.tvPautaActual.text = data?.getStringExtra("nueva_pauta")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()
        //Get actual logged user
        val user = auth.currentUser
        val userMail = user?.email
        val toolbar = binding.toolbar

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //Check if the user is in the local db
        var usuario: Usuario? = database.userQueries.getUser(auth.currentUser?.email).executeAsOneOrNull()
        //Get userId
        var userId = usuario?.id


        //If the user is not is the DB, insert it
        if (usuario == null) {
            database.userQueries.insertUser(auth.currentUser?.email)
            usuario = database.userQueries.getUser(auth.currentUser?.email).executeAsOne()
            userId = usuario.id
        }

        val pautas = database.pautaQueries.getAllRegByUserId(userId!!).executeAsList()
        if (pautas.isEmpty()) {

            binding.tvPautaActual.setText("--/--/----")
        }

        //Get last actual regimen date from the sharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs_$userMail", Context.MODE_PRIVATE)
        val pauAc = sharedPreferences.getString("pauAc", "--/--/----")
        binding.tvPautaActual.setText(pauAc)

        //Get regimen id
        var pautaId: Long = 0
        if (pauAc != null && userId != null && pautas.isNotEmpty()) {
            pautaId = database.pautaQueries.getRegByDate(pauAc, userId).executeAsOne().id
        }



        //Button to the actual regimen screen
        binding.tvPautaActual.setOnClickListener() {

            var pautaActual = binding.tvPautaActual.text.toString()

            if (!pautaActual.equals("--/--/----")) {
                val intent = Intent(this, CurrentRegimenActivity::class.java)
                intent.putExtra("pauta_actual", binding.tvPautaActual.text.toString())
                intent.putExtra("userid", userId)
                startActivity(intent)
            } else {

                Toast.makeText(baseContext, getString(R.string.añadaPauta), Toast.LENGTH_SHORT).show()
            }
        }


        binding.btnAddPattern.setOnClickListener {

            val intent = Intent (this, AddRegimenActivity::class.java)
            //Starts addRegimenActivity and waits for data to return
            addRegActivityLauncher.launch(intent)
        }

        binding.btnAddMed.setOnClickListener {

            val intent = Intent (this, AddMedicationActivity::class.java)
            startActivity(intent)
        }

        binding.btnTips.setOnClickListener {

            val intent = Intent (this, TipsActivity::class.java)
            startActivity(intent)
        }

        binding.btnDesayuno.setOnClickListener {

            var pautaActual = binding.tvPautaActual.text.toString()

            if (!pautaActual.equals("--/--/----")) {
                val intent = Intent(this, GlucemiaActivity::class.java)
                intent.putExtra("turno", getString(R.string.desayuno))
                intent.putExtra("pautaId", pautaId)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {

                Toast.makeText(baseContext, getString(R.string.añadaPauta), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnComida.setOnClickListener {

            var pautaActual = binding.tvPautaActual.text.toString()

            if (!pautaActual.equals("--/--/----")) {
                val intent = Intent(this, GlucemiaActivity::class.java)
                intent.putExtra("turno", getString(R.string.comida))
                intent.putExtra("pautaId", pautaId)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {

                Toast.makeText(baseContext, getString(R.string.añadaPauta), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnCena.setOnClickListener {

            var pautaActual = binding.tvPautaActual.text.toString()

            if (!pautaActual.equals("--/--/----")) {
                val intent = Intent(this, GlucemiaActivity::class.java)
                intent.putExtra("turno", getString(R.string.cena))
                intent.putExtra("pautaId", pautaId)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {

                Toast.makeText(baseContext, getString(R.string.añadaPauta), Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnResopon.setOnClickListener {

            var pautaActual = binding.tvPautaActual.text.toString()

            if (!pautaActual.equals("--/--/----")) {
                val intent = Intent(this, GlucemiaActivity::class.java)
                intent.putExtra("turno", getString(R.string.resopon))
                intent.putExtra("pautaId", pautaId)
                intent.putExtra("userId", userId)
                startActivity(intent)
            } else {

                Toast.makeText(baseContext, getString(R.string.añadaPauta), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.my_menu, menu)
        return true
    }

    //Configure the menu option
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_settings -> {
                auth.signOut()
                Toast.makeText(baseContext, getString(R.string.cierresesion), Toast.LENGTH_SHORT)
                    .show()
                val  intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Save actual regimen date in shared preferences
    override fun onStop() {
        super.onStop()

        val inputPauAc = binding.tvPautaActual.text.toString()
        sharedPreferences.edit().putString("pauAc", inputPauAc).apply()
    }
}