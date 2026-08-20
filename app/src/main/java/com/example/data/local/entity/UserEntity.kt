package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.data.model.UserRole

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val phone: String = "+998 90 123 45 67",
    val role: UserRole = UserRole.STUDENT,
    val personalCode: String = "41909931330028",
    val lastName: String = "ABDUQODIROV",
    val firstName: String = "AZIZBEK",
    val fatherName: String = "ALISHER O'G'LI",
    val birthDay: Int = 15,
    val birthMonth: Int = 8,
    val birthYear: Int = 2004,
    val interests: String = "Ona tili va adabiyot, Ingliz tili, Matematika",
    val avatarUrl: String? = "avatar_boy_1",
    val passwordHash: String = "password123"
)
