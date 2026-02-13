package com.example.inventariosapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.local.entity.SalesEntity

@Dao
interface SalesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SalesEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSales(list: List<SalesEntity>): List<Long>

    @Query("SELECT * FROM sales ORDER BY fechaVenta DESC")
    suspend fun getAllSales(): List<SalesEntity>

    @Query("SELECT * FROM sales WHERE ventaId = :ventaId LIMIT 1")
    suspend fun getSaleByVentaId(ventaId: Int): SalesEntity?

    @Query("DELETE FROM sales")
    suspend fun deleteAllSales(): Int

    @Query("""
        SELECT * FROM sales 
        WHERE fechaVenta BETWEEN :startDate AND :endDate 
        ORDER BY fechaVenta DESC
    """)
    suspend fun getSalesBetween(startDate: String, endDate: String): List<SalesEntity>
}