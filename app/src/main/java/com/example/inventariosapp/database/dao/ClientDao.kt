package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.ClientEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Query("SELECT * FROM client_table ORDER BY fechaIngreso DESC")
    suspend fun getAllClients(): List<ClientEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertOne(clients: ClientEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertAll(clients: List<ClientEntity>)

    @Insert()
    suspend fun instertById(client: ClientEntity)

    @Query("DELETE FROM client_table")
    suspend fun deleteAllClient()

    @Query("SELECT * FROM client_table WHERE nombreCliente LIKE '%' || :query || '%'")
    fun searchClients(query: String): Flow<List<ClientEntity>>
}