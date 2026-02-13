package com.example.inventariosapp.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.inventariosapp.local.entity.NewPayEntity

@Dao
interface NewPayDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(newPay: NewPayEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(newPay: List<NewPayEntity>): List<Long>

    @Query("SELECT * FROM NewPayEntity")
    suspend fun getAll(): List<NewPayEntity>

    @Query("DELETE FROM NewPayEntity")
    suspend fun deleteAll(): Int
}