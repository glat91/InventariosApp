package com.example.inventariosapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inventariosapp.local.dao.ClientDao
import com.example.inventariosapp.local.dao.InventoryDao
import com.example.inventariosapp.local.dao.NewPayDao
import com.example.inventariosapp.local.dao.PayDao
import com.example.inventariosapp.local.dao.PostSalesDao
import com.example.inventariosapp.local.dao.ProductDao
import com.example.inventariosapp.local.dao.SalesDao
import com.example.inventariosapp.local.entity.ClientEntity
import com.example.inventariosapp.local.entity.InventoryEntity
import com.example.inventariosapp.local.entity.NewPayEntity
import com.example.inventariosapp.local.entity.PayEntity
import com.example.inventariosapp.local.entity.PostSaleEntity
import com.example.inventariosapp.local.entity.PostSaleProductEntity
import com.example.inventariosapp.local.entity.ProductEntity
import com.example.inventariosapp.local.entity.SalesEntity

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
    version = 4,
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