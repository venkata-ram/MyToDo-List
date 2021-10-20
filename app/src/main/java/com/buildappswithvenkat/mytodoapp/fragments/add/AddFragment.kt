package com.buildappswithvenkat.mytodoapp.fragments.add

import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.buildappswithvenkat.mytodoapp.R
import com.buildappswithvenkat.mytodoapp.data.models.Priority
import com.buildappswithvenkat.mytodoapp.data.models.TodoData
import com.buildappswithvenkat.mytodoapp.data.viewmodel.SharedViewModel
import com.buildappswithvenkat.mytodoapp.data.viewmodel.ToDoViewModel
import com.buildappswithvenkat.mytodoapp.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        _binding = FragmentAddBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)
        binding.prioritiesSpinner.onItemSelectedListener = mSharedViewModel.listener
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_add) {
            insertDataToDb()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDb() {
        val mTitle = _binding?.titleEt?.text.toString()
        val mPriority = _binding?.prioritiesSpinner?.selectedItem.toString()
        val mDescription = _binding?.descriptionEt?.text.toString()

        val validation = mSharedViewModel.verifyDataFromUser(mTitle,mDescription)
        if(validation){
            //insert data to database
            val newData = TodoData(
                0,
                mTitle,
                mSharedViewModel.parsePriority(mPriority),
                mDescription
            )
            mToDoViewModel.insertData(newData)
            Toast.makeText(requireContext(),"Added Successfully...",Toast.LENGTH_SHORT).show()
            //navigate back
            findNavController().navigate(R.id.action_addFragment_to_listFragment)
        }else{
            Toast.makeText(requireContext(),"Please fill out all fields...",Toast.LENGTH_SHORT).show()
        }


    }



}