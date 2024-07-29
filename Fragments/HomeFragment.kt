package com.example.foodorderapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import com.example.foodorderapplication.model.MenuItem

import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.foodorderapplication.R
import com.example.foodorderapplication.adapter.MenuAdapter
import com.example.foodorderapplication.databinding.FragmentHomeBinding

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


@Suppress("UNREACHABLE_CODE")
class HomeFragment : Fragment() {
    private lateinit var database:FirebaseDatabase


    private lateinit var menuItems:MutableList<MenuItem>

    private lateinit var binding:FragmentHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewAllMenu.setOnClickListener{
            val bottomSheetDialog= MenuBottomSheetFragment()
            bottomSheetDialog.show(parentFragmentManager,"Test")
        }

retrieveAndDisplayPopularItems()
        return binding.root


    }

    private fun retrieveAndDisplayPopularItems() {
        database= FirebaseDatabase.getInstance()
        val foodRef=database.reference.child("menu")
        menuItems= mutableListOf()

        foodRef.addListenerForSingleValueEvent(object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(foodSnapshot in snapshot.children){
                    val menuItem=foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let { menuItems.add(it) }
                }
               randomPopularItems()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun randomPopularItems() {
        val index=menuItems.indices.toList().shuffled()
        val numItemToShow=6
        val subsetMenuItems=index.take(numItemToShow).map{menuItems[it]}

        setPopularItemsAdapter(subsetMenuItems)
    }

    private fun setPopularItemsAdapter(subsetMenuItems: List<MenuItem>) {
        val adapter=MenuAdapter(subsetMenuItems,requireContext())
        binding.popularRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.popularRecyclerView.adapter=adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageList= ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2,ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3,ScaleTypes.FIT))

        val imageSlider= binding.imageSlider
        imageSlider.setImageList(imageList)
        imageSlider.setImageList(imageList,ScaleTypes.FIT)
        imageSlider.setItemClickListener(object :ItemClickListener {
            override fun doubleClick(position: Int) {

            }
            override fun onItemSelected(position: Int) {
                val itemPosition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })


    }
companion object{

}

    }
