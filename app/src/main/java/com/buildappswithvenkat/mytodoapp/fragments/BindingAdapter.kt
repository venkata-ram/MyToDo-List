package com.buildappswithvenkat.mytodoapp.fragments

import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import com.buildappswithvenkat.mytodoapp.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BindingAdapter {

    companion object{

        @BindingAdapter("android:navigateToAddFragment")
        @JvmStatic
        fun navigateToAddFragment(view : FloatingActionButton, navigate : Boolean){
            view.setOnClickListener{
                if(navigate){
                    view.findNavController().navigate(R.id.action_listFragment_to_addFragment)
                }
            }
        }
    }
}