package com.example.foodorderapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.foodorderapplication.adapter.CartAdapter.CartViewHolder
import com.example.foodorderapplication.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartAdapter (private val context: Context,
                   private val cartItems:MutableList<String>,
                   private val cartItemPrices :MutableList<String>,
                   private var cartImages:MutableList<String>,
                   private var cartDescriptions:MutableList<String>,
                   private val cartQuantity:MutableList<Int>,
                   private val cartIngredient:MutableList<String>):RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

                       private val auth=FirebaseAuth.getInstance()

    init{
        val database=FirebaseDatabase.getInstance()
        val userId=auth.currentUser?.uid?:""
        val cartItemNumber=cartItems.size

        itemQuantities= IntArray(cartItemNumber){1}
        cartItemsReference=database.reference.child("user").child(userId).child("CartItems")
    }
    companion object{
        private var itemQuantities= intArrayOf()
        private lateinit var cartItemsReference:DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding=CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = cartItems.size

    @SuppressLint("SuspiciousIndentation")
    fun getUpdatedItemsQuantities(): MutableList<Int> {
      val itemQuantity= mutableListOf<Int>()
        itemQuantity.addAll(cartQuantity)
        return itemQuantity
    }
    inner class CartViewHolder(private val binding:CartItemBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
binding.apply {
    val quantity=itemQuantities[position]
    cartFoodname.text=cartItems[position]
    cartFoodprice.text=cartItemPrices[position]

    val uriString=cartImages[position]
   // Log.d("image","food url :$uriString")
    val uri = Uri.parse(uriString)
    Glide.with(context).load(uri).into(cartimage)

//    cartimage.setImageResource(cartImages[position])
    cartItemQuant.text=quantity.toString()

    minusbtn.setOnClickListener{
decreaseQuantity(position)
    }
    plusbtn.setOnClickListener{
increaseQuantity(position)
    }
    deletebtn.setOnClickListener {
val itemposition=adapterPosition
        if(itemposition!= RecyclerView.NO_POSITION){
            deleteQuantity(itemposition)
        }
    } }
        }
        private  fun increaseQuantity(position: Int){
        if(itemQuantities[position]<10){
            itemQuantities[position]++
            cartQuantity[position]= itemQuantities[position]
            binding.cartItemQuant.text=itemQuantities[position].toString()
        }
    }
        private   fun deleteQuantity(position: Int){
           val positionRetrieve=position
            getUniqueKeyAtPosition(positionRetrieve){uniqueKey->
                if(uniqueKey !=null){
                    removeItem(position,uniqueKey)
                }
            }
        }
    private  fun decreaseQuantity(position: Int) {
        if (itemQuantities[position] > 1) {
            itemQuantities[position]--
            cartQuantity[position]= itemQuantities[position]
            binding.cartItemQuant.text = itemQuantities[position].toString()
        }
    }
    }

    private fun removeItem(position: Int, uniqueKey: String) {
if(uniqueKey!=null){
    cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
        cartItems.removeAt(position)
        cartImages.removeAt(position)
        cartDescriptions.removeAt(position)
        cartQuantity.removeAt(position)
        cartItemPrices.removeAt(position)
        cartIngredient.removeAt(position)
        Toast.makeText(context,"Item Delete",Toast.LENGTH_SHORT).show()
        //update itemQuantities
        itemQuantities= itemQuantities.filterIndexed{index,i->index!=position}.toIntArray()
        notifyItemRemoved(position)
        notifyItemRangeChanged(position,cartItems.size)
    }.addOnFailureListener {
        Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show()
    }
}
    }

    private fun getUniqueKeyAtPosition(positionRetrieve: Int,onComplete:(String?)->Unit) {
cartItemsReference.addListenerForSingleValueEvent(object:ValueEventListener{
    @SuppressLint("SuspiciousIndentation")
    override fun onDataChange(snapshot: DataSnapshot) {
     var uniqueKey:String?=null
        snapshot.children.forEachIndexed { index, dataSnapshot ->
            if(index==positionRetrieve){
                uniqueKey=dataSnapshot.key
                return@forEachIndexed
            }
        }
        onComplete(uniqueKey)
    }

    override fun onCancelled(error: DatabaseError) {

    }
})
    }



}


