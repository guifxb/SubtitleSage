package com.example.translatorsportfolio.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user" )
data class UserInfo(
    @PrimaryKey
    val id: Int = 1,
    var name: String = "",
    var title: String = "",
    var aboutMe: String = "",
    var whatsApp: String = "",
    var linkedIn: String = "",
    var email: String = "",
)

@Entity(tableName = "user_experience")
data class Experience(
    @PrimaryKey
    var company: String,
    var pair: String,
    var time: String,
    var role: String
)

val defaultExp = Experience("", "", "", "")
val defaultUser = UserInfo(1, "Full name", "professional title", "short summary", "0", "linkedin.com", "example@example.com")



