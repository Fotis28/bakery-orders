package com.fotis.bakeryorders.ui.home


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagData
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxData
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupDao
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupData
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.ProductDao
import com.fotis.bakeryorders.ui.gallery.CakeDataDao
import com.fotis.bakeryorders.ui.gallery.CakeData

@Database(entities = [Product::class, BagData::class, BoxData::class, CupData::class,CakeData::class],  version = 17,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun bagDao(): BagDao
    abstract fun boxDao(): BoxDao
    abstract fun cupDao(): CupDao
    abstract fun cakeDataDao(): CakeDataDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "product_database"
                )
                    //.fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}