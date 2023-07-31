package cn.edu.sjtu.patrickli.cryptex.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

open class DataStoreViewModel(
    private val context: Context,
    private val dataStoreName: String
): ViewModel() {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(dataStoreName)
    protected suspend fun <T> writeToDataStore(storeKey: Preferences.Key<T>, value: T?) {
        context.dataStore.edit { pref ->
            value?.let {
                pref[storeKey] = it
            } ?: let {
                pref.remove(storeKey)
            }
        }
    }
    protected suspend fun <T> loadFromDataStore(key: Preferences.Key<T>): T? {
        val flow: Flow<T?> = context.dataStore.data.map { it[key] }
        return flow.first()
    }
    protected suspend fun clearDataStore() {
        context.dataStore.edit { it.clear() }
    }
}