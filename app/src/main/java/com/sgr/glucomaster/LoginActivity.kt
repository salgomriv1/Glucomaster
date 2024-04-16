package com.sgr.glucomaster

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.sgr.glucomaster.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    //Variables
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        //Using a splashScreen
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        //Binding
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Thread.sleep(1000)
        screenSplash.setKeepOnScreenCondition {false}

        //Variable init
        auth = FirebaseAuth.getInstance()

        //Check if there is a logged user
      /*  if (auth.currentUser != null) {
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }*/

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        binding.btnGLogin.setOnClickListener {
            signInGoogle()
        }

        binding.btnIniciarSesion.setOnClickListener {
            if (binding.etLoginMail.text.isNotEmpty() && binding.etLoginPass.text.isNotEmpty()) {
                signIn(binding.etLoginMail.text.toString(), binding.etLoginPass.text.toString())
            } else {
                Toast.makeText(baseContext, getString(R.string.loginEnBlanco), Toast.LENGTH_LONG).show()
            }
        }

        binding.tvRegLink.setOnClickListener{

            val intent = Intent (this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    //AUTH GOOGLE
    //Function to sign in with Google
    private fun signInGoogle() {

        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
        if ( result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            handleResults(task)
        }
    }

    private fun handleResults(task: Task<GoogleSignInAccount>) {

        if (task.isSuccessful) {
            val account : GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            Toast.makeText(this, task.exception.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val intent: Intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                }
            }
    }
    //END GOOGLE AUTH

    //Function to log in with email and pass
    private fun signIn(email: String, pass: String) {

        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {

                if (it.isSuccessful) {

                    val user = auth.currentUser
                    Toast.makeText(baseContext, user?.email + getString(R.string.authOK), Toast.LENGTH_LONG)
                        .show()
                    val intent = Intent (this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {

                    Toast.makeText(baseContext, getString(R.string.errorLog), Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}