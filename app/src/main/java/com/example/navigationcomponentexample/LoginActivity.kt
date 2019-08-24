package com.example.navigationcomponentexample

import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()


        login_btn.setOnClickListener {
            val email = loginEmail.text.toString().trim()
            val password = loginPassword.text.toString().trim()

            if (email.isEmpty()) {
                loginEmail.error = "Email Required"
                loginEmail.requestFocus()
                return@setOnClickListener
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                loginEmail.error = "Valid Email Required"
                loginEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty() || password.length < 6) {
                loginPassword.error = "6 char password required"
                loginPassword.requestFocus()
                return@setOnClickListener
            }


            loginUser(email, password)
        }
        to_signup_btn.setOnClickListener {

            val p1 = android.util.Pair.create<View, String>(imageView, "login_to_signup")
            val p2 = android.util.Pair.create<View, String>(loginEmail, "login_to_signup_email")
            val p3 = android.util.Pair.create<View, String>(loginPassword, "login_to_signup_pass")
            val p4 = android.util.Pair.create<View, String>(imageView2, "login_to_signup_box")

            val options = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityOptions.makeSceneTransitionAnimation(this, p1, p2, p3, p4)
            } else {
                TODO("VERSION.SDK_INT < LOLLIPOP")
            }
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java),options.toBundle())
        }

    }


    private fun loginUser(email: String, password: String) {
        progressBar2.visibility = View.VISIBLE
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                progressBar2.visibility = View.GONE
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