package com.buildappswithvenkat.mytodoapp.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.buildappswithvenkat.mytodoapp.R
import com.buildappswithvenkat.mytodoapp.data.models.TodoData
import com.buildappswithvenkat.mytodoapp.data.viewmodel.SharedViewModel
import com.buildappswithvenkat.mytodoapp.data.viewmodel.ToDoViewModel
import com.buildappswithvenkat.mytodoapp.databinding.FragmentListBinding
import com.buildappswithvenkat.mytodoapp.fragments.list.adapter.ListAdapter
import com.google.android.material.snackbar.Snackbar
import jp.wasabeef.recyclerview.animators.LandingAnimator
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator

//List fragment handle notes lists
class ListFragment : Fragment(), SearchView.OnQueryTextListener {
    private var _binding : FragmentListBinding? = null
    private val binding get()  = _binding!!

    private val mToDoViewModel : ToDoViewModel by viewModels()
    private val mSharedViewModel : SharedViewModel by viewModels()
    private val adapter : ListAdapter by lazy { ListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        // Inflate the layout for this fragment
        _binding =  FragmentListBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = this
        binding.mSharedViewModel = mSharedViewModel

        setupRecyclerview()

        //observe LiveData
        mToDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
            mSharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)

        })

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    private fun setupRecyclerview() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.itemAnimator = SlideInUpAnimator().apply {
            addDuration = 300
        }
        //Swipe to delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.dataList[viewHolder.adapterPosition]
                //Delete item
                mToDoViewModel.deleteItem(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore deleted item
                restoreDeletedData(viewHolder.itemView,deletedItem,viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view : View, deletedItem : TodoData, position : Int){
        val snackbar = Snackbar.make(
            view,"Deleted : ${deletedItem.title}",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo"){
            mToDoViewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackbar.show()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.list_fragment_menu,menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search.actionView as? SearchView
        searchView?.isSubmitButtonEnabled = true
        searchView?.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_delete_all -> confirmRemoval()
            R.id.menu_priority_high -> mToDoViewModel.sortByHighPriority.observe(this, Observer { adapter.setData(it) })
            R.id.menu_priority_low -> mToDoViewModel.sortByLowPriority.observe(this, Observer { adapter.setData(it) })
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query != null){
            searchThroughDatabase(query)
        }
        return true
    }

    private fun searchThroughDatabase(query: String) {
        val searchQuery = "%$query%"

        mToDoViewModel.searchDatabase(searchQuery).observe(this, Observer { list ->
            list?.let {
                adapter.setData(it)
            }
        })
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}