package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.R
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.utils.TodoAdapter
import com.example.todolist.utils.TodoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.ref.PhantomReference


class HomeFragment : Fragment(), AddTodoPopupFragment.DialogueNextBtnClickListener,
    TodoAdapter.ToDoAdapterClicksInerfaceBtnClicked {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var navController: NavController
    private lateinit var binding:FragmentHomeBinding
    private  var popupFragment:AddTodoPopupFragment ?= null
    private lateinit var adapter: TodoAdapter
    private lateinit var mList: MutableList<TodoData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init(view)
        getDataFromFirebase()
        registerEvents()
    }
    private fun registerEvents(){
       binding.addBtnHome.setOnClickListener{
           if(popupFragment!=null)
               childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
            popupFragment = AddTodoPopupFragment()
           popupFragment!!.setListener(this)
           popupFragment!!.show(childFragmentManager,AddTodoPopupFragment.TAG)


       }
    }
    private fun init(view: View)
    {
        navController = Navigation.findNavController(view)
        auth  = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference.child("Tasks").child(auth.currentUser?.uid.toString() )
        binding.RecyclerView.setHasFixedSize(true)
        binding.RecyclerView.layoutManager = LinearLayoutManager(context)
        mList = mutableListOf()
        adapter  = TodoAdapter(mList)
        adapter.setlistener(this)
        binding.RecyclerView.adapter = adapter
    }
    private fun getDataFromFirebase(){
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mList.clear()
                for(takeSnapshot in snapshot.children)
                {
                    val todoTask = takeSnapshot.key?.let{
                        TodoData(it , takeSnapshot.value.toString())
                    }
                    if(todoTask!=null) {
                        mList.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,error.message,Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onSaveTask(Todo: String, todoEt: TextInputEditText) {
        databaseReference.push().setValue(Todo).addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Task Saved Successfully!!",Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onUpdateTask(todoData: TodoData, todoEt: TextInputEditText) {
        val map = HashMap<String, Any>()
        map[todoData.taskId] = todoData.task
        databaseReference.updateChildren(map).addOnCompleteListener{
            if(it.isSuccessful)
            {
                Toast.makeText(context,"Updated Successfully!!",Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popupFragment!!.dismiss()
        }
    }

    override fun onDeleteTaskBtnClicked(todoData: TodoData) {
        databaseReference.child(todoData.taskId).removeValue().addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Deleted Successfully!!",Toast.LENGTH_SHORT).show()

            }
            else
            {
                Toast.makeText(context,it.exception?.message,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onEditTaskBtnClicked(todoData: TodoData) {
        if(popupFragment!=null)
            childFragmentManager.beginTransaction().remove(popupFragment!!).commit()
        popupFragment = AddTodoPopupFragment.newInstance(todoData.taskId,todoData.task)
        popupFragment!!.setListener(this)
        popupFragment!!.show(childFragmentManager,AddTodoPopupFragment.TAG)
    }

}