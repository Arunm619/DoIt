package io.arunbuilds.doit.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import io.arunbuilds.doit.data.Task
import io.arunbuilds.doit.data.TaskDao

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {
    val task = state.get<Task>(KEY_TASK)

    var taskName = state.get<String>(KEY_TASK_NAME) ?: task?.name ?: ""
        set(value) {
            field = value
            state.set(KEY_TASK_NAME, value)
        }


    var taskImportance = state.get<Boolean>(KEY_TASK_IMPORTANCE) ?: task?.important ?: false
        set(value) {
            field = value
            state.set(KEY_TASK_IMPORTANCE, value)
        }

    companion object {
        private const val KEY_TASK_NAME = "taskName"
        private const val KEY_TASK_IMPORTANCE = "taskImportance"
        private const val KEY_TASK = "task"
    }
}