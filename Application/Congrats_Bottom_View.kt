package com.example.foodorderapplication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodorderapplication.databinding.FragmentCongratsBottomViewBinding
import com.example.foodorderapplication.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Congrats_Bottom_View : BottomSheetDialogFragment() {
private lateinit var binding:FragmentCongratsBottomViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCongratsBottomViewBinding.inflate(layoutInflater,container,false)
        binding.goHomebtn.setOnClickListener{
            val intent= Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

    }
}