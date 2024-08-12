package com.example.foodorderapplication

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodorderapplication.databinding.ActivityDetailBinding
import com.example.foodorderapplication.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class DetailActivity : AppCompatActivity() {
    private  lateinit var binding:ActivityDetailBinding
    private var foodName:String?=null
    private var foodImage:String?=null
    private var foodDescription:String?=null
    private var foodIngredients:String?=null
    private var foodPrice:String?=null

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth= FirebaseAuth.getInstance()

        foodName=intent.getStringExtra("MenuItemName")
        foodDescription=intent.getStringExtra("MenuItemDescription")
        foodIngredients=intent.getStringExtra("MenuItemIngredients")
        foodPrice=intent.getStringExtra("MenuItemPrice")

        foodImage=intent.getStringExtra("MenuItemImage")

        with(binding){
            detailFoodName.text=foodName
            foodnamedescription.text=foodDescription
            ingredientText.text=foodIngredients
            Glide.with(this@DetailActivity).load(Uri.parse(foodImage)).into(detailimage)

        }

        binding.backfromdetail.setOnClickListener{
            finish()
        }
binding.addToCartbtn.setOnClickListener {
    addItemToCart()
    finish()
}
    }

    private fun addItemToCart() {
        val database=FirebaseDatabase.getInstance().reference
        val userId=auth.currentUser?.uid?:""

        val cartItem=CartItems(foodName.toString(),foodPrice.toString(),foodDescription.toString(),foodImage.toString(),1)

        database.child("user").child(userId).child("CartItem").push().setValue(cartItem).addOnCompleteListener {
            Toast.makeText(this,"Items added into cart successfully",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener {
            Toast.makeText(this,"Item not added",Toast.LENGTH_SHORT).show()

        }
    }
}