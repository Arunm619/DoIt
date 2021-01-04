package io.arunbuilds.doit.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.arunbuilds.doit.data.TaskDao
import io.arunbuilds.doit.data.TaskDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        roomDatabaseCallback: TaskDatabase.RoomDatabaseCallback
    ): TaskDatabase {
        return Room.databaseBuilder(app, TaskDatabase::class.java, "task_database")
            .fallbackToDestructiveMigration()
            .addCallback(roomDatabaseCallback)
            .build()
    }

    @Provides
    fun provideTaskDao(taskDatabase: TaskDatabase): TaskDao {
        return taskDatabase.taskDao()
    }

    @ApplicationScope
    @Provides
    @Singleton
    fun providesApplicationScope(): CoroutineScope {
        return CoroutineScope(SupervisorJob())
    }
}

@Retention(AnnotationRetention.RUNTIME)
@Qualifier
annotation class ApplicationScope