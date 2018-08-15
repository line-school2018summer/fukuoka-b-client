package com.line.fukuokabclient

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        //user = User(id = 1, clientNumber = "PT445")

        btn_login.setOnClickListener {
            mAuth!!.signInWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(applicationContext, "Signed in", Toast.LENGTH_LONG).show()
                            mUser = mAuth!!.currentUser
//                            updateUI(mUser!!)
                        } else {
                            Toast.makeText(applicationContext, "Logged in failed", Toast.LENGTH_LONG).show()
                        }

                    }

            //Toast.makeText(applicationContext, "Submitted", Toast.LENGTH_LONG).show()
        }
    }

//    fun updateUI(mUser:FirebaseUser) {
//        var intent = Intent(applicationContext, PinActivity::class.java)
//        startActivity(intent)
//    }
}
