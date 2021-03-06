package edu.ucsb.cs.cs184.bustester

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

// Reference: https://www.youtube.com/watch?v=P9AgWjBIixc&list=LLmp1_AyMnzjsqaEiDaJqnKw&index=5&t=319s
class AuthActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>
    val MY_REQUEST_CODE: Int = 7117

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        //Init
        providers = Arrays.asList<AuthUI.IdpConfig>(
                AuthUI.IdpConfig.EmailBuilder().build(), //Email login
                //AuthUI.IdpConfig.FacebookBuilder().build(), //Facebook login
                AuthUI.IdpConfig.GoogleBuilder().build(), //Google login
                AuthUI.IdpConfig.PhoneBuilder().build()
        )

        showSignInOptions()

        btn_sign_out.setOnClickListener{
            AuthUI.getInstance().signOut(this@AuthActivity)
                    .addOnCompleteListener {
                        btn_sign_out.isEnabled = false
                        showSignInOptions()
                    }
                    .addOnFailureListener {
                        e-> Toast.makeText(this@AuthActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == MY_REQUEST_CODE){
            val response = IdpResponse.fromResultIntent(data)
            if(resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,""+user!!.email, Toast.LENGTH_SHORT).show()

                btn_sign_out.isEnabled = true
            }
            else{
                Toast.makeText(this,""+response!!.error!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showSignInOptions(){
        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.MyTheme)
                .build(), MY_REQUEST_CODE)
    }
}
