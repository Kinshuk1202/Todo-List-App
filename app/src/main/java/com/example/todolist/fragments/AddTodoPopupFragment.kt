package com.example.todolist.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.example.todolist.R
import com.example.todolist.databinding.FragmentAddTodoPopupBinding
import com.example.todolist.databinding.FragmentHomeBinding
import com.example.todolist.utils.TodoData
import com.google.android.material.textfield.TextInputEditText


class AddTodoPopupFragment : DialogFragment() {

    private lateinit var binding: FragmentAddTodoPopupBinding
    private lateinit var listner: DialogueNextBtnClickListener
    private var TodoData : TodoData ? = null
    fun setListener(listener: DialogueNextBtnClickListener) {
        this.listner = listener
    }

    companion object {
        const val TAG = "AddTodoPopupFragment"

        @JvmStatic
        fun newInstance(taskId: String, task: String) = AddTodoPopupFragment().apply {
            arguments = Bundle().apply {
                putString("taskId", taskId)
                putString("task", task)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAddTodoPopupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            TodoData = TodoData(
                arguments?.getString("taskId").toString(),
                arguments?.getString(("task")).toString()
            )
            binding.todoEt.setText(TodoData?.task)
        }

        registerEvents()
    }

    private fun registerEvents() {
        binding.todoNextBtn.setOnClickListener {
            val todoTask = binding.todoEt.text.toString()
            if (todoTask.isNotEmpty()) {
                if(TodoData == null)
                {
                    listner.onSaveTask(todoTask, binding.todoEt)
                }
                else
                {
                    TodoData?.task = todoTask
                    listner.onUpdateTask(TodoData!!,binding.todoEt)
                }

            } else {
                Toast.makeText(context, "Please type your task", Toast.LENGTH_SHORT).show()
            }
        }
        binding.todoClose.setOnClickListener {
            dismiss()
        }
    }

    interface DialogueNextBtnClickListener {
        fun onSaveTask(Todo: String, todoEt: TextInputEditText)
        fun onUpdateTask(todoData: TodoData, todoEt: TextInputEditText)
    }

}