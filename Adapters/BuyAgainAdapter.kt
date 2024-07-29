package com.example.foodorderapplication.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapplication.databinding.BuyAgainItemBinding

class BuyAgainAdapter( private val buyAgainFoodName:MutableList<String>,
                       private val buyAgainFoodPrice:MutableList<String>,
                       private val buyAgainFoodImage:MutableList<String>,
    private var requireContext: Context):
    RecyclerView.Adapter<BuyAgainAdapter.BuyagainViewholder>() {

    override fun onBindViewHolder(holder: BuyagainViewholder, position: Int) {
       holder.bind(buyAgainFoodName[position],buyAgainFoodPrice[position],buyAgainFoodImage[position])
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyagainViewholder {
        val binding=BuyAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BuyagainViewholder(binding)
    }

    override fun getItemCount(): Int =buyAgainFoodName.size


  inner   class BuyagainViewholder(private val binding:BuyAgainItemBinding):RecyclerView.ViewHolder(binding.root) {
                fun bind(foodName:String,foodPrice:String,foodImage:String){
                    binding.buyagainfoodname.text=foodName
                    binding.buyagainfoodprice.text=foodPrice
                    val uriString=foodImage
                    val uri= Uri.parse(uriString)

                   Glide.with(requireContext).load(uri).into(binding.buyagainFoodImage)
                }


    }

}