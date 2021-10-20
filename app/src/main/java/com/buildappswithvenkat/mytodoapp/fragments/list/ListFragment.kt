package com.buildappswithvenkat.mytodoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.buildappswithvenkat.mytodoapp.R
import com.buildappswithvenkat.mytodoapp.data.viewmodel.ToDoViewModel
import com.buildappswithvenkat.mytodoapp.databinding.FragmentListBinding

//List fragment handle notes lists
class ListFragment : Fragment() {
    private var _binding : FragmentListBinding? = null
    private val binding get()  = _binding!!

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val adapter : ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding =  FragmentListBinding.inflate(layoutInflater, container, false)

        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())

        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)

        })

        binding.floatingActionButton.setOnClickListener{
            findNavController().navigate(R.id.action_listFragment_to_addFragment)
        }

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.menu_delete_all){
            confirmRemoval()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun confirmRemoval() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes"){ _, _ ->
            mToDoViewModel.deleteAll()
            Toast.makeText(
                requireContext(),
                "Successfully deleted...",
                Toast.LENGTH_SHORT
            ).show()
        }
        builder.setNegativeButton("No"){ _, _-> }
        builder.setTitle("Delete All?")
        builder.setMessage("Are you sure you want to remove all items?")
        builder.create().show()
    }

}