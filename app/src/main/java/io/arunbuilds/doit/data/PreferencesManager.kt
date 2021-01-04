package io.arunbuilds.doit.data

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import androidx.datastore.preferences.createDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


enum class SortOrder { BY_NAME, BY_DATE }
data class FilterPreferences(val sortOrder: SortOrder, val hideCompleted: Boolean)

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore = context.createDataStore(NAME)
    val preferencesFlow = dataStore.data
        .catch {
            exception ->
            if( exception is IOException) {
                Log.e("Arun", "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val sortOrder = SortOrder.valueOf(preferences[PreferenceKeys.SORT_ORDER] ?: SortOrder.BY_DATE.name)
            val hideCompleted = preferences[PreferenceKeys.HIDE_COMPLETED] ?: false
            FilterPreferences(sortOrder,hideCompleted)
        }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.SORT_ORDER] = sortOrder.name
        }
    }

    suspend fun updateHideCompleted(hideCompleted: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.HIDE_COMPLETED] = hideCompleted
        }
    }

    companion object {
        const val NAME = "user_preferences"
        const val KEY_SORT_ORDER = "sort_order"
        const val KEY_HIDE_COMPLETED = "hide_completed"
    }

    private object PreferenceKeys {
        val SORT_ORDER = preferencesKey<String>(KEY_SORT_ORDER)
        val HIDE_COMPLETED = preferencesKey<Boolean>(KEY_HIDE_COMPLETED)

    }
}