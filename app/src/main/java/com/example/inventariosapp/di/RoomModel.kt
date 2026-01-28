package com.example.inventariosapp.di

import android.content.Context
import androidx.room.Room
import com.example.inventariosapp.database.CompanyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModel {
    private const val DATA_BASE_NAME =  "company_database"

    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CompanyDatabase::class.java, DATA_BASE_NAME)
            .fallbackToDestructiveMigration(false)
            .build()

    @Singleton
    @Provides
    fun provideClientDao(db: CompanyDatabase) = db.getClientDao()

    @Singleton
    @Provides
    fun provideProductDao(db: CompanyDatabase) = db.getProductDao()

    @Singleton
    @Provides
    fun provideSalesDao(db: CompanyDatabase) = db.getSalesDao()

    @Singleton
    @Provides
    fun providePayDao(db: CompanyDatabase) = db.getPayDao()

    @Singleton
    @Provides
    fun providePostSalesDao(db: CompanyDatabase) = db.postSales()

    @Singleton
    @Provides
    fun provideInventoryDao(db: CompanyDatabase) = db.inventoryDao()
}