package com.example.foodorderapplication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderapplication.databinding.ActivitySignBinding
import com.example.foodorderapplication.model.UserModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class SignActivity : AppCompatActivity() {

private lateinit var email:String
    private lateinit var password:String
    private lateinit var username:String
    private lateinit var auth:FirebaseAuth
    private lateinit var database:DatabaseReference

    private lateinit var googleSigninClient: GoogleSignInClient


    private val binding: ActivitySignBinding by lazy{
        ActivitySignBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val googleSignInOptions=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

         auth=Firebase.auth
        database=Firebase.database.reference
        googleSigninClient=GoogleSignIn.getClient(this,googleSignInOptions)

        binding.createAccount.setOnClickListener{
           username=binding.userName.text.toString()
            email=binding.emailAccount.text.toString().trim()
            password=binding.accountPassword.text.toString().trim()

            if(email.isEmpty()||password.isBlank()||username.isBlank()){
                Toast.makeText(this,"Please fill all the details",Toast.LENGTH_SHORT).show()
            }else{
                createAccount(email,password)
            }
        }

        binding.logintxt.setOnClickListener{
            val intent=Intent(this,LoginActivity::class.java)
            startActivity(intent)
        }
        binding.googlecreatebtn.setOnClickListener {
            val signIntent=googleSigninClient.signInIntent
launcher.launch(signIntent)
        }
    }
    private val launcher=registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
        val task=GoogleSignIn.getSignedInAccountFromIntent(result.data)
        if(result.resultCode==Activity.RESULT_OK){
            val account:GoogleSignInAccount?=task.result
            val credential=GoogleAuthProvider.getCredential(account?.idToken,null)
            auth.signInWithCredential(credential).addOnCompleteListener { task->
                if(task.isSuccessful){
                    Toast.makeText(this,"Sign in Successfull",Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,"Sign in Failed",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else{
            Toast.makeText(this,"Sign-in failed",Toast.LENGTH_SHORT).show()
        }
    }

    private fun createAccount(email: String, password: String) {
       auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
           task->
           if(task.isSuccessful){
               Toast.makeText(this,"Account Created Successfully",Toast.LENGTH_SHORT).show()
               saveUserData()
               startActivity(Intent(this,LoginActivity::class.java))
               finish()
           }else{
               Toast.makeText(this,"Account Creation Failed",Toast.LENGTH_SHORT).show()
               Log.d("Account","create account: Failure ",task.exception)
           }
       }
    }

    private fun saveUserData() {
        username=binding.userName.text.toString()
        email=binding.emailAccount.text.toString().trim()
        password=binding.accountPassword.text.toString().trim()

          val user=UserModel(username,email,password)
        val userId:String=FirebaseAuth.getInstance().currentUser!!.uid
        database.child("user").child(userId).setValue(user)
    }
}