package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.PayEntity

@Dao
interface PayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payList: List<PayEntity>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pay: PayEntity): Long

    @Query("SELECT * FROM pay_table")
    suspend fun getAll(): List<PayEntity>

    @Query("DELETE FROM pay_table WHERE ventaPagoId = :ventaPagoId")
    suspend fun deleteById(ventaPagoId: Int): Int

    @Query("DELETE FROM pay_table")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM pay_table WHERE ventaId = :ventaId")
    suspend fun getPaymentsByVentaId(ventaId: Int): List<PayEntity>
}