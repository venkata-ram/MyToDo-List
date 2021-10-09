package com.buildappswithvenkat.mytodoapp.fragments.list

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.buildappswithvenkat.mytodoapp.R
import com.buildappswithvenkat.mytodoapp.databinding.FragmentListBinding

//List fragment handle notes lists
class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get()  = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding =  FragmentListBinding.inflate(layoutInflater, container, false)
        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        _binding!!.listLayout.setOnClickListener {
            findNavController().navigate(R.id.action_listFragment_to_updateFragment)
        }
        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }

}