package com.example.inventariosapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.local.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM client_table ORDER BY fechaIngreso DESC")
    suspend fun getAllClients(): List<ClientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOne(client: ClientEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(clients: List<ClientEntity>): List<Long>

    @Insert
    suspend fun insertById(client: ClientEntity): Long

    @Query("DELETE FROM client_table")
    suspend fun deleteAllClient(): Int

    @Query("SELECT * FROM client_table WHERE nombreCliente LIKE '%' || :query || '%'")
    fun searchClients(query: String): Flow<List<ClientEntity>>
}