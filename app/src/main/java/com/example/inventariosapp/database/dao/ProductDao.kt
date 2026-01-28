package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.ProductEntity

@Dao
interface ProductDao {
    @Query("SELECT * FROM product_table")
    suspend fun getAllProducts(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(products: List<ProductEntity>): List<Long>

    @Query("DELETE FROM product_table")
    suspend fun deleteAllProducts(): Int

    @Query("SELECT * FROM product_table WHERE productoId = :id")
    suspend fun getProductById(id: Int): ProductEntity?
}