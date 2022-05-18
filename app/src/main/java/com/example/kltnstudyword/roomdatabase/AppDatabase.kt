package com.example.kltnstudyword.roomdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.kltnstudyword.model.TopicModel
import com.example.kltnstudyword.model.WordModel
import com.example.kltnstudyword.model.WordPractice


@Database(entities = [TopicModel::class,WordModel::class,WordPractice::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomDao(): TopicDAO?


    companion object {
        private var INSTANCE: AppDatabase? = null

        val migration_1_2: Migration = object: Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE topic ADD COLUMN phone TEXT DEFAULT ''")
            }
        }

        fun getInMemoryDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder<AppDatabase>(
                    context.applicationContext,AppDatabase::class.java,"AppDBB"
                )
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}