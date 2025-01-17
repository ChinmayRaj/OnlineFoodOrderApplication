package com.example.foodorderapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderapplication.DetailActivity
import com.example.foodorderapplication.databinding.PopularItemsBinding

class PopularAdapter(private val items:List<String>,
                     private val price:List<String>,
                     private val image:List<Int>,
                     private val requireContext:Context)
    : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>(){


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PopularAdapter.PopularViewHolder {
        return PopularViewHolder(PopularItemsBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: PopularAdapter.PopularViewHolder, position: Int) {
        val item =items[position]
        val images= image[position]
        val price=price[position]
        holder.bind(item,price,images)

        holder.itemView.setOnClickListener{
            val intent= Intent(requireContext, DetailActivity::class.java)
            intent.putExtra("MenuItemName",item)
            intent.putExtra("MenuItemImage",images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class PopularViewHolder(private val binding:PopularItemsBinding) :RecyclerView.ViewHolder(binding.root){
        private val imagesView=binding.imageView6
        @SuppressLint("SuspiciousIndentation")
        fun bind(item: String, price:String, images: Int) {
                   binding.foodname.text=item
            binding.Price.text=price
               imagesView.setImageResource(images)
        }


    }
}