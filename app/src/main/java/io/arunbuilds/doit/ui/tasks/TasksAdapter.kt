package io.arunbuilds.doit.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.arunbuilds.doit.data.Task
import io.arunbuilds.doit.databinding.LayoutItemTaskBinding

class TasksAdapter constructor(private val listener: OnItemClickListener) : ListAdapter<Task, TasksAdapter.TaskViewHolder>(TaskDiffCallback()) {

  inner  class TaskViewHolder(private val binding: LayoutItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init{
            binding.apply {
                root.setOnClickListener{
                    val position = adapterPosition
                    if( position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                checkboxComplete.setOnClickListener{
                    val position = adapterPosition
                    if( position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkboxComplete.isChecked)
                    }
                }
            }
        }
        fun bind(task: Task) {
            with(binding) {
                textViewName.text = task.name
                textViewName.paint.isStrikeThruText = task.completed
                checkboxComplete.isChecked = task.completed
                imageViewLabelPriority.isVisible = task.important
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding =
            LayoutItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val currentTask = getItem(position)
        holder.bind(currentTask)
    }

    interface OnItemClickListener {
        fun onItemClick(task: Task)
        fun onCheckBoxClick(task: Task, isChecked: Boolean)
    }

    class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem
    }
}