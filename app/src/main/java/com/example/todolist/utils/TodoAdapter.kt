package com.example.todolist.utils

import android.view.LayoutInflater
import android.view.OnReceiveContentListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.EachTodoBinding

class TodoAdapter(private val list:MutableList<TodoData>) :RecyclerView.Adapter<TodoAdapter.ToDoViewHolder>(){
    private var listener:ToDoAdapterClicksInerfaceBtnClicked?= null
    fun setlistener(listener: ToDoAdapterClicksInerfaceBtnClicked){
        this.listener = listener
    }
    inner class ToDoViewHolder(val binding:EachTodoBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder){
            with(list[position]){
                binding.TodoTask.text = this.task
                binding.deleteTask.setOnClickListener{
                    listener?.onDeleteTaskBtnClicked(this)

                }
                binding.editTask.setOnClickListener{
                    listener?.onEditTaskBtnClicked(this)
                }
            }
        }
    }
    interface ToDoAdapterClicksInerfaceBtnClicked{
        fun onDeleteTaskBtnClicked(todoData: TodoData)
        fun onEditTaskBtnClicked(todoData: TodoData)

    }
}