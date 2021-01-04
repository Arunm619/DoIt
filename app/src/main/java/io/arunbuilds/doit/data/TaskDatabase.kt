package io.arunbuilds.doit.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import io.arunbuilds.doit.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

@Database(entities = [Task::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    class RoomDatabaseCallback @Inject constructor(
        private val taskDatabase: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // db operations
            val taskDao = taskDatabase.get().taskDao()
            applicationScope.launch {
                repeat(10) {
                    Task("Item $it ",completed = Random.nextBoolean(), important = Random.nextBoolean()).also { task ->
                        taskDao.insert(task)
                    }
                }
            }

        }
    }
}