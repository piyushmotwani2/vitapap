package com.example.navigationcomponentexample

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        mAuth = FirebaseAuth.getInstance()


        signup_btn.setOnClickListener {
            val email = signupEmail.text.toString().trim()
            val password = signupPassword.text.toString().trim()

            if (email.isEmpty()) {
                signupEmail.error = "Email Required"
                signupEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                signupEmail.error = "Valid Email Required"
                signupEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 8) {
                signupPassword.error = "minimum 8 char password required"
                signupPassword.requestFocus()
                return@setOnClickListener
            }

            registerUser(email, password)

        }

        to_login_btn.setOnClickListener {

            val p1 = android.util.Pair.create<View, String>(imageView3, "login_to_signup")
            val p2 = android.util.Pair.create<View, String>(signupEmail, "login_to_signup_email")
            val p3 = android.util.Pair.create<View, String>(signupPassword, "login_to_signup_pass")
            val p4 = android.util.Pair.create<View, String>(imageView5, "login_to_signup_box")
            val p5 = android.util.Pair.create<View, String>(signup_btn, "login_to_signup_btn")

            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this, p1, p2, p3, p4, p5)
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java),options.toBundle())
        }
    }

    private fun registerUser(email: String, password: String) {
        progressBar.visibility = View.VISIBLE
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar.visibility = View.GONE
                if (task.isSuccessful) {
                    login()
                } else {
                    task.exception?.message?.let {
                        toast(it)
                    }
                }
            }

    }

    override fun onStart() {
        super.onStart()
        mAuth.currentUser?.let {
            login()
        }
    }
}