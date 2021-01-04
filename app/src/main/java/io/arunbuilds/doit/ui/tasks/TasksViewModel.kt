package io.arunbuilds.doit.ui.tasks

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import io.arunbuilds.doit.data.TaskDao

class TasksViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao
) : ViewModel() {
    val tasks = taskDao.getTasks().asLiveData()

    override fun onCleared() {
        super.onCleared()
        Log.d("Arun", "Cleared called")
    }
}