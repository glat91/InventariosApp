package com.example.inventariosapp.database.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PostSaleWithProducts(
    @Embedded val sale: PostSaleEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "postSaleId"
    )
    val productos: List<PostSaleProductEntity>
)