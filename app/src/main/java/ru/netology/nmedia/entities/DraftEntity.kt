package ru.netology.nmedia.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class DraftEntity(
    @PrimaryKey
    val idUser: Long,
    val content: String?
)