package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ConfigDao {
    @Query("SELECT * FROM configuration WHERE id = 1 LIMIT 1")
    fun getConfig(): Flow<Configuration?>

    @Query("SELECT * FROM configuration WHERE id = 1 LIMIT 1")
    suspend fun getConfigDirect(): Configuration?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertConfig(config: Configuration)
}

@Dao
interface ForwardLogDao {
    @Query("SELECT * FROM forward_logs ORDER BY timestamp DESC")
    fun getAllLogs(): Flow<List<ForwardLog>>

    @Query("SELECT * FROM forward_logs ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestLog(): ForwardLog?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: ForwardLog)

    @Query("DELETE FROM forward_logs")
    suspend fun clearLogs()
}
