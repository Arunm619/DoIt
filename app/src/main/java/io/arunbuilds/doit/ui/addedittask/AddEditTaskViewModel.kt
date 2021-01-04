package io.arunbuilds.doit.ui.addedittask

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.arunbuilds.doit.data.Task
import io.arunbuilds.doit.data.TaskDao
import io.arunbuilds.doit.ui.ADD_TASK_RESULT_OK
import io.arunbuilds.doit.ui.EDIT_TASK_RESULT_OK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddEditTaskViewModel @ViewModelInject constructor(
    private val taskDao: TaskDao,
    @Assisted private val state: SavedStateHandle
) : ViewModel() {

    fun onSaveClick() {
        if (taskName.isBlank()) {
            showInvalidInputMessage("Name cannot be empty")
            return
        }

        if (taskName.length < 4) {
            showInvalidInputMessage("Name length should be atleast 5 characters")
            return
        }

        if (task != null) {
            val updatedTask = task.copy(name = taskName, important = taskImportance)
            updateTask(updatedTask)
        } else {
            val newTask = Task(name = taskName, important = taskImportance)
            createTask(newTask)
        }
    }

    private fun showInvalidInputMessage(message: String) = viewModelScope.launch {
        addEditTaskEventChannel.send(AddEditTaskEvent.ShowInvalidInputMessage(message))
    }

    private val addEditTaskEventChannel = Channel<AddEditTaskEvent>()
    val addEditTaskEvent = addEditTaskEventChannel.receiveAsFlow()

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

    private fun createTask(task: Task) = viewModelScope.launch {
        taskDao.insert(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(ADD_TASK_RESULT_OK))
    }


    private fun updateTask(task: Task) = viewModelScope.launch {
        taskDao.update(task)
        addEditTaskEventChannel.send(AddEditTaskEvent.NavigateBackWithResult(EDIT_TASK_RESULT_OK))
    }


    sealed class AddEditTaskEvent {
        data class ShowInvalidInputMessage(val message: String) : AddEditTaskEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditTaskEvent()
    }


    companion object {
        private const val KEY_TASK_NAME = "taskName"
        private const val KEY_TASK_IMPORTANCE = "taskImportance"
        private const val KEY_TASK = "task"
    }
}