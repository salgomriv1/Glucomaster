package com.sgr.glucomaster

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.userProfileChangeRequest
import com.sgr.glucomaster.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    //Variables
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        auth = FirebaseAuth.getInstance()

        binding.btnCrearCuenta.setOnClickListener {

            var name = binding.etRegisterName.text.toString()
            var email = binding.etRegisterEmail.text.toString()
            var pass = binding.etRegisterPass.text.toString()
            var repeatPass = binding.etRegisterRepeatPass.text.toString()

            //Check no empty fields
            if (name.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty() && repeatPass.isNotEmpty()) {

                //Check password length
                if (pass.length > 5) {

                    //Check two pass are the same
                    if (pass.equals(repeatPass)) {

                        createAccount(name, email, pass)
                    }else {

                        Toast.makeText(baseContext, getString(R.string.passnocoinciden), Toast.LENGTH_SHORT)
                            .show()
                        binding.etRegisterPass.requestFocus()
                    }
                } else {

                    Toast.makeText(baseContext, getString(R.string.passmenor6), Toast.LENGTH_SHORT).show()
                }
            } else {

                Toast.makeText(baseContext, getString(R.string.camposvacios), Toast.LENGTH_SHORT).show()
            }
        }
    }

    //Function to create an account
    private fun createAccount(name: String, email: String, pass: String) {

        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {

                    //Get the user
                    val user = auth.currentUser
                    //Prepare updates to profile
                    val profileUpdates = userProfileChangeRequest {
                        displayName = name
                    }
                    //Update the profile
                    user!!.updateProfile(profileUpdates)

                    Toast.makeText(baseContext, getString(R.string.cuentacreada), Toast.LENGTH_SHORT)
                        .show()
                    finish()
                } else {

                    //Check if the email is in use
                    val exception = it.exception
                    if (exception is FirebaseAuthUserCollisionException){

                        Toast.makeText(this, getString(R.string.correoenuso), Toast.LENGTH_SHORT)
                            .show()
                    } else {

                        Toast.makeText(baseContext, "Error" + exception, Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }
}