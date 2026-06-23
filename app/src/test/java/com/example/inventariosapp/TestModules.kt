package com.example.inventariosapp

import android.content.Context
import androidx.room.Room
import com.example.inventariosapp.api.ApiService
import com.example.inventariosapp.di.NetworkModule
import com.example.inventariosapp.di.RoomModel
import com.example.inventariosapp.local.CompanyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import javax.inject.Singleton

/**
 * Módulo de pruebas para inyectar mocks en lugar de implementaciones reales.
 */
@Module
@InstallIn(SingletonComponent::class)
object TestModule {
    
    @Provides
    @Singleton
    fun provideMockApiService(): ApiService = mockk()
    
    @Provides
    @Singleton
    fun provideCompanyDatabase(@ApplicationContext context: Context): CompanyDatabase =
        Room.inMemoryDatabaseBuilder(context, CompanyDatabase::class.java).build()
    
    @Provides
    fun provideClientDao(db: CompanyDatabase) = db.getClientDao()
    
    @Provides
    fun provideProductDao(db: CompanyDatabase) = db.getProductDao()
    
    @Provides
    fun provideSalesDao(db: CompanyDatabase) = db.getSalesDao()
    
    @Provides
    fun providePayDao(db: CompanyDatabase) = db.getPayDao()
    
    @Provides
    fun providePostSalesDao(db: CompanyDatabase) = db.postSales()
    
    @Provides
    fun provideInventoryDao(db: CompanyDatabase) = db.inventoryDao()
    
    @Provides
    fun provideNewPayDao(db: CompanyDatabase) = db.newPayDao()
}

/**
 * Módulo para pruebas de NetworkModule con mocks
 */
@Module
@InstallIn(SingletonComponent::class)
object TestNetworkModule {
    
    @Provides
    @Singleton
    fun provideMockApiService(): ApiService = mockk()
}

/**
 * Módulo para pruebas de RoomModel con base de datos en memoria
 */
@Module
@InstallIn(SingletonComponent::class)
object TestRoomModule {
    
    @Provides
    @Singleton
    fun provideCompanyDatabase(@ApplicationContext context: Context): CompanyDatabase =
        Room.inMemoryDatabaseBuilder(context, CompanyDatabase::class.java).build()
}
