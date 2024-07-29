package com.example.foodorderapplication.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderapplication.R
import com.example.foodorderapplication.adapter.NotificationAdapter
import com.example.foodorderapplication.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class Notification_Bottom_Fragment : BottomSheetDialogFragment() {
    private lateinit var binding:FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)
        val notifications= listOf("Your Order has been cancelled successfully","Order has been dispatched","Order has been placed")
        val notificationImages= listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)
        val adapter=NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )
        binding.notificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter

        return binding.root
    }

    companion object {

    }


}