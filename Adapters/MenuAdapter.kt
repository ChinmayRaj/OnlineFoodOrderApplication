package com.example.foodorderapplication.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderapplication.DetailActivity
import com.example.foodorderapplication.databinding.MenuItemBinding
import com.example.foodorderapplication.model.MenuItem

class MenuAdapter(private val menuItems: List<MenuItem>,
                  private val requireContext:Context):RecyclerView.Adapter<MenuAdapter.MenuViewHolder>()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding=MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
      return MenuViewHolder(binding)
    }



    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int =menuItems.size

    inner class MenuViewHolder(private val binding:MenuItemBinding):RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position=adapterPosition
                if(position!=RecyclerView.NO_POSITION){
                     openDetailsActivity(position)
                }

            }
        }

        fun bind(position: Int){
            val menuItem=menuItems[position]
binding.apply {
    menuFoodnamePopular.text=menuItem.foodName
    menuPrice.text=menuItem.foodPrice
    val uri: Uri =Uri.parse(menuItem.foodImage)
    Glide.with(requireContext).load(uri).into(menuImg)
    }
        }
    }

    private fun openDetailsActivity(position: Int) {
      val menuItem=menuItems[position]
       val intent=Intent(requireContext,DetailActivity::class.java).apply{
           putExtra("MenuItemName",menuItem.foodName)
           putExtra("MenuItemImage",menuItem.foodImage)
           putExtra("MenuItemDescription",menuItem.foodDescription)
           putExtra("MenuItemIngredients",menuItem.foodIngredients)
           putExtra("MenuItemPrice",menuItem.foodPrice)
       }
        requireContext.startActivity(intent)
    }


}

