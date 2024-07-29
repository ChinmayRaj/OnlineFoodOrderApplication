package com.example.foodorderapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapplication.R
import com.example.foodorderapplication.adapter.MenuAdapter
import com.example.foodorderapplication.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding:FragmentSearchBinding
    private lateinit var adapter:MenuAdapter
private val originalFoodName= listOf("Burger","Sandwich","Momo","Item","Sandwich","Momo")
  private  val originalmenuItemPrice= listOf("$5","$4","$8","$7","$9","$5")
     private val originalmenuImage= listOf(
        R.drawable.menu1,
        R.drawable.menu2,
        R.drawable.menu3,
        R.drawable.menu4,
        R.drawable.menu5,
        R.drawable.menu6,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
   private val filteredMenuFoodName=mutableListOf<String>()
    private val filteredMenuItem=mutableListOf<String>()
    private val filteredMenuImage=mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentSearchBinding.inflate(inflater,container,false)

//        adapter=MenuAdapter(filteredMenuFoodName,
//            filteredMenuItem,
//            filteredMenuImage,
//            requireContext())


        binding.menuRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.menuRecyclerView.adapter=adapter
        //setup for search view
        setupSearchView()
        //show all menu items
        showAllMenuItems()
        return binding.root
    }

    private fun showAllMenuItems() {
        filteredMenuFoodName.clear()
        filteredMenuItem.clear()
        filteredMenuImage.clear()

        filteredMenuFoodName.addAll(originalFoodName)
        filteredMenuItem.addAll(originalmenuItemPrice)
        filteredMenuImage.addAll(originalmenuImage)

        adapter.notifyDataSetChanged()
    }

    private fun setupSearchView() {

        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                filterMenuItem(query)
                return true
            }
            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItem(newText)
                return true
            }
        })
    }

    private fun filterMenuItem(query: String) {
        filteredMenuFoodName.clear()
        filteredMenuItem.clear()
        filteredMenuImage.clear()

        originalFoodName.forEachIndexed { index, foodName ->
            if(foodName.contains(query, ignoreCase = true)){
                filteredMenuFoodName.add(foodName)
                filteredMenuItem.add(originalmenuItemPrice[index])
                filteredMenuImage.add(originalmenuImage[index])
            }
        }
        adapter.notifyDataSetChanged()
    }
    companion object {

        }
    }
