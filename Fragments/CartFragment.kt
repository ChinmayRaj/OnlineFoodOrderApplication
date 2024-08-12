package com.example.foodorderapplication.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapplication.Congrats_Bottom_View
import com.example.foodorderapplication.PayOutActivity
import com.example.foodorderapplication.R
import com.example.foodorderapplication.adapter.CartAdapter
import com.example.foodorderapplication.databinding.FragmentCartBinding
import com.example.foodorderapplication.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {
    private lateinit var binding:FragmentCartBinding
private lateinit var cartAdapter: CartAdapter
private lateinit var userId:String
private lateinit var auth:FirebaseAuth
private lateinit var database: FirebaseDatabase
private lateinit var foodNames:MutableList<String>
    private lateinit var foodPrices:MutableList<String>
    private lateinit var foodDescriptions:MutableList<String>
    private lateinit var foodImageUri:MutableList<String>
    private lateinit var foodIngredients:MutableList<String>
    private lateinit var foodquantity:MutableList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentCartBinding.inflate(inflater,container,false)

       auth=FirebaseAuth.getInstance()
        retrieveCartItems()

        binding.buttonproceed.setOnClickListener{
            getOrderItemsDetail()

        }
        return binding.root
    }

    private fun getOrderItemsDetail() {
        val IdReference:DatabaseReference=database.reference.child("user").child(userId).child("CartItem")
        val foodName= mutableListOf<String>()
        val foodPrice=mutableListOf<String>()
        val foodImage=mutableListOf<String>()
        val foodDescription=mutableListOf<String>()
        val foodIngredients=mutableListOf<String>()

        val foodQuantities=cartAdapter.getUpdatedItemsQuantities()

        IdReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             for(foodSnapshot in snapshot.children){
                 val orderItems=foodSnapshot.getValue(CartItems::class.java)
                 orderItems?.foodName?.let{ foodName.add(it) }
                 orderItems?.foodPrice?.let{ foodPrice.add(it) }
                 orderItems?.foodDescription?.let{ foodDescription.add(it) }
                 orderItems?.foodImage?.let{ foodImage.add(it) }
                 orderItems?.foodIngredient?.let{ foodIngredients.add(it) }
             }
                orderNow(foodName,foodPrice,foodDescription,foodImage,foodIngredients,foodQuantities)
                
            }

            override fun onCancelled(error: DatabaseError) {
Toast.makeText(requireContext(),"Order making failed.Please try Again",Toast.LENGTH_SHORT).show()

            }

        })
    }

    private fun orderNow(foodName: MutableList<String>, foodPrice: MutableList<String>, foodDescription: MutableList<String>, foodImage: MutableList<String>, foodIngredients: MutableList<String>, foodQuantities: MutableList<Int>) {
if(isAdded && context!=null){
    val intent=Intent(requireContext(),PayOutActivity::class.java)
    intent.putExtra("FoodItemName",foodName as ArrayList<String>)
    intent.putExtra("FoodItemPrice",foodPrice as ArrayList<String>)
    intent.putExtra("FoodItemImage",foodImage as ArrayList<String>)
    intent.putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
    intent.putExtra("FoodItemIngredient",foodIngredients as ArrayList<String>)
    intent.putExtra("FoodItemQuantities",foodQuantities as ArrayList<Int>)

    startActivity(intent)
}
    }

    private fun retrieveCartItems() {
        database= FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        val foodReference=database.reference.child("user").child(userId).child("CartItem")

        foodNames= mutableListOf()
        foodPrices= mutableListOf()
        foodDescriptions= mutableListOf()
        foodImageUri= mutableListOf()
        foodIngredients= mutableListOf()
        foodquantity= mutableListOf()

        foodReference.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
             for(foodSnapshot in snapshot.children){
                 val cartItems=foodSnapshot.getValue(CartItems::class.java)
                 cartItems?.foodName?.let { foodNames.add(it) }
                 cartItems?.foodPrice?.let { foodPrices.add(it) }
                 cartItems?.foodDescription?.let { foodDescriptions.add(it) }
                 cartItems?.foodImage?.let { foodImageUri.add(it) }
                 cartItems?.foodQuantity?.let { foodquantity.add(it) }
                 cartItems?.foodIngredient?.let { foodIngredients.add(it) }
             }
                setAdapter()
            }
            private fun setAdapter() {
                cartAdapter=CartAdapter(requireContext(),
                    foodNames,
                    foodPrices,
                    foodDescriptions,
                    foodImageUri,
                    foodquantity,
                    foodIngredients)
                binding.cartRecyclerView.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
                binding.cartRecyclerView.adapter=cartAdapter
            }
            override fun onCancelled(error: DatabaseError) {
        Toast.makeText(context,"data not fetched",Toast.LENGTH_SHORT).show()
            }
        })
    }
    }