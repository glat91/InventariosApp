package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.inventariosapp.database.entity.PostSaleEntity
import com.example.inventariosapp.database.entity.PostSaleProductEntity
import com.example.inventariosapp.database.entity.PostSaleWithProducts

@Dao
interface PostSalesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: PostSaleEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSaleProducts(products: List<PostSaleProductEntity>): List<Long>

    @Transaction
    suspend fun insertSaleWithProducts(sale: PostSaleEntity, products: List<PostSaleProductEntity>) {
        insertSale(sale)
        insertSaleProducts(products)
    }

    @Transaction
    @Query("SELECT * FROM post_sales")
    suspend fun getAllSales(): List<PostSaleWithProducts>

    @Transaction
    @Query("SELECT * FROM post_sales WHERE id = :id")
    suspend fun getSaleById(id: String): PostSaleWithProducts?

    @Query("DELETE FROM post_sales")
    suspend fun clearSales(): Int

    @Query("DELETE FROM post_sales WHERE id = :id")
    suspend fun deleteSaleById(id: String): Int

    @Query("DELETE FROM post_sale_products")
    suspend fun deleteAll(): Int

    @Query("UPDATE post_sales SET estatusVentaId = :newStatus WHERE id = :id")
    suspend fun updateSaleStatusById(id: String, newStatus: Int): Int
}