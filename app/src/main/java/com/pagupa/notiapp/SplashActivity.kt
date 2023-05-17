package com.pagupa.notiapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.pagupa.notiapp.clases.Server
import com.pagupa.notiapp.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        esperaActivity()
    }

    fun esperaActivity(){
        val user_id = Server.getUser_id(applicationContext)
        val extras = intent.extras
        //FirebaseMessaging.getInstance().subscribeToTopic("Canal 1");

        var main = Intent(applicationContext, MainActivity::class.java)

        Handler().postDelayed({
            if (user_id > 0) {
                FirebaseMessaging.getInstance().token
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.e("fcm", "Fetching FCM registration token failed", task.exception)
                                return@OnCompleteListener
                            }
                            if (extras != null) {
                                if (extras.containsKey("accion")) {
                                    main.putExtra("accion", extras.getString("accion"))
                                    main.putExtra("codigo", extras.getInt("codigo", 0))
                                }
                            }
                            val token = task.result
                            Server.guardarTokenFcm(applicationContext, token)
                        })
            }
            else
                main=Intent(applicationContext, LoginActivity::class.java)

            startActivity(main)
            finish()
        }, 1500)
    }
}