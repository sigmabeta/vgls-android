package com.vgleadsheets.database.di

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.vgleadsheets.database.TableName
import com.vgleadsheets.database.VglsDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideVglsDatabase(context: Context) = Room
        .databaseBuilder(
            context,
            VglsDatabase::class.java,
            "vgls-database"
        )
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                db.beginTransaction()

                for (table in TableName.values()) {
                    val values = ContentValues()

                    values.put("table_id", table.ordinal.toString())
                    values.put("last_edit_time_ms", 0L)

                    db.insert("db_statistics", SQLiteDatabase.CONFLICT_FAIL, values)
                }

                db.setTransactionSuccessful()
                db.endTransaction()
            }
        }
)
        .build()
}
