package com.line.fukuokabclient

import android.content.Intent
import android.graphics.drawable.Animatable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.gson.GsonBuilder
import com.line.fukuokabclient.Client.APIClient
import com.line.fukuokabclient.Client.APIFactory
import com.line.fukuokabclient.Utility.Prefs
import com.line.fukuokabclient.Client.UserClient
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import android.graphics.drawable.AnimationDrawable
import android.support.v4.view.ViewCompat.setBackground


/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mUser: FirebaseUser? = null
    var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser

        //user = User(id = 1, clientNumber = "PT445")

        checkLoggedIn()

        btn_login.setOnClickListener {
//            loginProgressBar.visibility = View.VISIBLE
            progress.visibility = View.VISIBLE
            val anim = progress.background as Animatable
            anim.start()
            mAuth!!.signInWithEmailAndPassword(txt_email.text.toString(), txt_password.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Prefs.edit(applicationContext)
                                    .putString("email", txt_email.text.toString())
                                    .putString("password", txt_password.text.toString())
                                    .apply()
//                            loginProgressBar.visibility = View.INVISIBLE
                            anim.stop()
                            progress.visibility = View.INVISIBLE
                            Toast.makeText(applicationContext, "Signed in", Toast.LENGTH_LONG).show()
                            getToken(mUser!!)
                        } else {
//                            loginProgressBar.visibility = View.INVISIBLE
                            anim.stop()
                            progress.visibility = View.INVISIBLE
                            Toast.makeText(applicationContext, "Logged in failed", Toast.LENGTH_LONG).show()
                        }

                    }
        }
    }

    fun getToken(mUser: FirebaseUser) {
        mUser!!.getIdToken(true)
                .addOnCompleteListener { tokenTask: Task<GetTokenResult> ->
                    if (!tokenTask.isSuccessful) {
                        Toast.makeText(applicationContext, "ユーザーサーバーと接続不能", Toast.LENGTH_LONG).show()
                        return@addOnCompleteListener
                    }

                    token = tokenTask.result.token

                    if (token == null) {
                        Log.d("TOKEN2", "NULL")
                    } else {
                        Log.d("TOKEN3", "$token")
                        Prefs.edit(applicationContext)
                                .putString("token", token)
                                .apply()
                        updateUI(mUser!!)
                    }
//                    updateUI(mUser!!)
                }
    }

    fun updateUI(mUser:FirebaseUser) {
        APIFactory.build(Prefs.get(applicationContext).getString("token", "none")!!)
                .create(UserClient::class.java)
                .getUserByMail(mUser.email!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    Prefs.edit(applicationContext)
                            .putLong("id", it.id)
                            .apply()
//                    Log.d("TOKEN3", "$it")
                    var intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }, {
                    Toast.makeText(applicationContext, "ユーザーサーバーと接続不能", Toast.LENGTH_LONG).show()
                })

    }

    fun checkLoggedIn() {
        txt_email.setText(Prefs.get(applicationContext).getString("email", ""))
        txt_password.setText(Prefs.get(applicationContext).getString("password", ""))
    }
}
