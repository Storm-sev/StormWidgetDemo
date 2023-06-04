package com.example.customnewdemo.act.room


import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.customnewdemo.app.MyApplication


@Database(
    entities = [User::class],//持久化的数据类
    version = 1,//版本
    exportSchema = false,// 当设置变量 room.schemaLocation="XXX“时，且 exportSchema 为 true 时将数据库模式导出到给定的文件夹

)
abstract class StormDatabase : RoomDatabase() {

    // 抽象 dao
//
    abstract val userDao: UserDAO

    companion object {

        private val DB_NAME = "user.db"

        val INSTANCE : StormDatabase  by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {

            val instance = Room.databaseBuilder(
                context = MyApplication.appContext,
                StormDatabase::class.java,
                DB_NAME,
            )
                // 开发阶段可以使用, 直接丢弃旧版本数据,创建新版本导致旧版本丢失.
                .fallbackToDestructiveMigration()
                .build()

            instance
        }

    }
}




