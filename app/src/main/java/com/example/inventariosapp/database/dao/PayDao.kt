package com.example.inventariosapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.database.entity.PayEntity

@Dao
interface PayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(payList: List<PayEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pay: PayEntity)

    @Query("SELECT * FROM pay_table")
    suspend fun getAll(): List<PayEntity>

    @Query("DELETE FROM pay_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM pay_table WHERE ventaId = :ventaId")
    suspend fun getPaymentsByVentaId(ventaId: Int): List<PayEntity>
}