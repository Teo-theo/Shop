package com.example.doanltd.RoomDatabase.NgDungRoom

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ngDung_item")
data class NgDungEntity(
    @PrimaryKey()val MaNgD: String = "ND00",
    val TenNgD:String,
    val Email:String,
    val SDT:String,
    val TKNgD:String,
    val TrangThai:Int,
    val ChucVu:String
)
