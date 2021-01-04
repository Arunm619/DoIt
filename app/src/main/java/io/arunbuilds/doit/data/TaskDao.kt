package io.arunbuilds.doit.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM task_table ")
    fun getAllTasks(): Flow<List<Task>>

    // Query "Run" ->  "Arun" will be returned
    @Query("SELECT * FROM task_table WHERE name LIKE '%' || :searchQuery || '%'  ORDER BY important DESC")
    fun getTasks(searchQuery : String): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)
}