package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table")
    suspend fun getAllCProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun instertAll(products: List<ProductEntity>)

    @Query("DELETE FROM client_table")
    suspend fun deleteAllClient()

    @Query("SELECT * FROM product_table WHERE productoId = :id")
    suspend fun getProductById(id: Int): ProductEntity?
}