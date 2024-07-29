package com.example.foodorderapplication.Fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderapplication.R
import com.example.foodorderapplication.adapter.BuyAgainAdapter
import com.example.foodorderapplication.databinding.FragmentHistoryBinding
import com.example.foodorderapplication.model.OrderDetails
import com.example.foodorderapplication.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    private lateinit var  binding:FragmentHistoryBinding
    private lateinit var buyAgainAdapter: BuyAgainAdapter
    private lateinit var database:FirebaseDatabase
    private lateinit var auth:FirebaseAuth
    private lateinit var userId:String
    private var listOfOrderItem:MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        auth=FirebaseAuth.getInstance()
        database= FirebaseDatabase.getInstance()
        retrieveBuyHistory()
        //setupRecyclerView()
        binding.recentbuyitem.setOnClickListener {
            seeItemsRecentBuy()
            binding.receivedButton.setOnClickListener {
                updateOrderStatus()
            }
        }
        return binding.root
    }

    private fun updateOrderStatus() {
        val itemPushKey=listOfOrderItem[0].itemPushKey
        val completeOrderReference=database.reference.child("CompleteOrder").child(itemPushKey!!)
        completeOrderReference.child("paymentReceived").setValue(true)
    }

    private fun seeItemsRecentBuy(){
    listOfOrderItem.firstOrNull()?.let{recentBuy->

        val intent= Intent(requireContext(),recentOrderItems::class.java)
        intent.putExtra("RecentBuyOrderItem",recentBuy)
        startActivity(intent)
    }
}
    private fun retrieveBuyHistory() {
binding.recentbuyitem.visibility=View.INVISIBLE
        userId=auth.currentUser?.uid?:""

        val buyItemReference=database.reference.child("user").child(userId).child("BuyHistory")
        val shortingQuery=buyItemReference.orderByChild("currentTime")

        shortingQuery.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(buySnapshot in snapshot.children){
                    val buyHistoryItem=buySnapshot.getValue(OrderDetails::class.java)
                    buyHistoryItem?.let {
                        listOfOrderItem.add(it)
                    }
                }
                listOfOrderItem.reverse()
                if(listOfOrderItem.isNotEmpty()){
                    setDataInRecentBuyItem()

                    setPreviouslyBuyItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun setDataInRecentBuyItem() {
        binding.recentbuyitem.visibility=View.VISIBLE
        val recentOrderItem=listOfOrderItem.firstOrNull()
        recentOrderItem?.let{
            with(binding){
              buyAgainfoodName.text=it.foodNames?.firstOrNull()?:""
                buyAgainfoodPrice.text=it.foodPrices?.firstOrNull()?:""
                val image=it.foodImages?.firstOrNull()?:""
                val uri= Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(buyAgainfoodImage)

                 val isOrderIsAccepted =listOfOrderItem[0].orderAccepted
                Log.d("TAG","setDataInRecentBuyItem: $isOrderIsAccepted")
                if(isOrderIsAccepted == true){
                    orderStatus.background.setTint(Color.GREEN)
                    receivedButton.visibility=View.VISIBLE
                }
            }
        }
    }

    private fun setPreviouslyBuyItemsRecyclerView() {
        val buyAgainFoodName= mutableListOf<String>()
        val buyAgainFoodPrice= mutableListOf<String>()
        val buyAgainFoodImage= mutableListOf<String>()
        for(i in 1 until listOfOrderItem.size){
listOfOrderItem[i].foodNames?.firstOrNull()?.let{buyAgainFoodName.add(it)}
            listOfOrderItem[i].foodPrices?.firstOrNull()?.let{buyAgainFoodPrice.add(it)}
            listOfOrderItem[i].foodImages?.firstOrNull()?.let{buyAgainFoodImage.add(it)}

        }
        val rv=binding.BuyAgainRecyclerView
        rv.layoutManager=LinearLayoutManager(requireContext())
        buyAgainAdapter= BuyAgainAdapter(buyAgainFoodName,buyAgainFoodPrice,buyAgainFoodImage,requireContext())
        rv.adapter=buyAgainAdapter
    }



    }
