package com.ubaya.todoapp.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ubaya.todoapp.util.DB_NAME
import com.ubaya.todoapp.util.MIGRATION_1_2
import com.ubaya.todoapp.util.MIGRATION_1_3
import com.ubaya.todoapp.util.MIGRATION_2_3
import com.ubaya.todoapp.util.MIGRATION_3_4

//bisa lebih dari 1, untuk arrayOf, berdasarkan banyak entity
//version itu versi database
@Database(entities = arrayOf(Todo::class), version = 4)
abstract class TodoDatabase:RoomDatabase() {

//    ini dari banyaknya DAO yang dipunya, jadi kalo ada AnimalDao, nanti tambahin lagi abstract funnya
    abstract fun todoDao(): TodoDao

    companion object {
        @Volatile private var instance: TodoDatabase ?= null
        private val LOCK = Any()

        fun buildDatabase(context: Context) = Room.databaseBuilder(context.applicationContext, TodoDatabase::class.java, DB_NAME)
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
            .build()

        operator fun invoke(context: Context) {
            if (instance != null) {
                synchronized(LOCK) {
                    instance ?: buildDatabase(context).also {
                        instance = it
                    }
                }
            }
        }
    }
}