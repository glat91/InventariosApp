package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.InventoryEntity

@Dao
interface InventoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<InventoryEntity>)

    @Query("SELECT * FROM inventory")
    suspend fun getAll(): List<InventoryEntity>

    @Query("SELECT * FROM inventory WHERE productoId = :productoId")
    suspend fun getByProductoId(productoId: Int): InventoryEntity?

    @Query("DELETE FROM inventory")
    suspend fun clear()
}