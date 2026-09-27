package com.fotis.bakeryorders.ui.gallery

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "cakeOrder")
data class CakeData (

        @PrimaryKey(autoGenerate = true)
        val cakeorderid: Int  , // "Box", "Bug", "Cup"

        @ColumnInfo(name = "NameAndLastName")
        var NameAndLastName: String,

        @ColumnInfo(name = "Tilefono")
        var Tilefono: String,

        @ColumnInfo(name = "Sxima")
        var Sxima: String,

        @ColumnInfo(name = "Eidos")
        var Eidos: String,

        @ColumnInfo(name = "Geusi")
        var Geusi: String,

        @ColumnInfo(name = "Vafla")
        var Vafla: String,

        @ColumnInfo(name = "Day")
        var Day: String,

        @ColumnInfo(name = "Time")
        var Time: String,

        @ColumnInfo(name="Kila")
        var Kila: Double,

        @ColumnInfo(name="Sxolia")
        var Sxolia: String,


        var isExpanded: Boolean = false,

        var IsChecked: Boolean=false



    ){ fun toMap(): Map<String, Any> {
        return mapOf(
                "cakeorderid" to cakeorderid,
                "NameAndLastName" to NameAndLastName,
                "Tilefono" to Tilefono,
                "Sxima" to Sxima,
                "Eidos" to Eidos,
                "Geusi" to Geusi,
                "Vafla" to Vafla,
                "Day" to Day,
                "Time" to Time,
                "Kila" to Kila,
                "Sxolia" to Sxolia,
                "isExpanded" to isExpanded,
                "IsChecked" to IsChecked
        )
    }
}