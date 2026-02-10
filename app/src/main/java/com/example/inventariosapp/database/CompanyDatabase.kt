package com.example.inventariosapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inventariosapp.database.dao.ClientDao
import com.example.inventariosapp.database.dao.InventoryDao
import com.example.inventariosapp.database.dao.NewPayDao
import com.example.inventariosapp.database.dao.PayDao
import com.example.inventariosapp.database.dao.PostSalesDao
import com.example.inventariosapp.database.dao.ProductDao
import com.example.inventariosapp.database.dao.SalesDao
import com.example.inventariosapp.database.entity.ClientEntity
import com.example.inventariosapp.database.entity.InventoryEntity
import com.example.inventariosapp.database.entity.NewPayEntity
import com.example.inventariosapp.database.entity.PayEntity
import com.example.inventariosapp.database.entity.PostSaleEntity
import com.example.inventariosapp.database.entity.PostSaleProductEntity
import com.example.inventariosapp.database.entity.ProductEntity
import com.example.inventariosapp.database.entity.SalesEntity

@Database(entities = [
    ClientEntity::class,
    ProductEntity::class,
    SalesEntity::class,
    PayEntity::class,
    PostSaleEntity::class,
    PostSaleProductEntity::class,
    InventoryEntity::class,
    NewPayEntity::class,
],
    version = 1,
    exportSchema = true)

@TypeConverters(PriceListConverter::class)

abstract class CompanyDatabase: RoomDatabase() {
    abstract fun getClientDao(): ClientDao
    abstract fun getProductDao(): ProductDao
    abstract fun getSalesDao(): SalesDao
    abstract fun getPayDao(): PayDao
    abstract fun postSales(): PostSalesDao
    abstract fun inventoryDao(): InventoryDao
    abstract fun newPayDao(): NewPayDao
}