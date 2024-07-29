package com.example.foodorderapplication.Fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.disklrucache.DiskLruCache.Value
import com.example.foodorderapplication.R
import com.example.foodorderapplication.databinding.FragmentProfileBinding
import com.example.foodorderapplication.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ProfileFragment : Fragment() {
    private lateinit var binding:FragmentProfileBinding
private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentProfileBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        setUserData()
        binding.saveinfo.setOnClickListener{
            val name=binding.username.text.toString()
            val email=binding.email.text.toString()
            val phone=binding.contact.text.toString()
            val address=binding.address.text.toString()

            updateUserData(name,email,address,phone)

        }
        return binding.root
    }

    @SuppressLint("SuspiciousIndentation")
    private fun updateUserData(name: String, email: String, address: String, phone: String) {
  val userId=auth.currentUser?.uid
        if(userId!=null){
            val userReference=database.getReference("user").child(userId)
            val userData:HashMap<String,String> = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile update successfully",Toast.LENGTH_SHORT).show()

            }
                .addOnFailureListener {
                    Toast.makeText(requireContext(),"Profile update failed ",Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setUserData() {
        val userId=auth.currentUser?.uid
        if(userId!=null){
            val userReference=database.getReference("user").child(userId)
            userReference.addListenerForSingleValueEvent(object:ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
if(snapshot.exists()){
    val userProfile=snapshot.getValue(UserModel::class.java)
    if(userProfile!=null){

        binding.username.setText(userProfile.name).toString()
        binding.address.setText(userProfile.address).toString()
        binding.email.setText(userProfile.email).toString()
        binding.contact.setText(userProfile.phone).toString()
    }
}
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


}
