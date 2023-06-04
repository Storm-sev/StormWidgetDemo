package com.example.customnewdemo.act.room

import com.example.customnewdemo.adapter.CustomDecorAdapter

class RoomHelper {

    companion object {

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RoomHelper()
        }
    }

    // 获取所有的user信息
    fun allUsers(): List<User>? {

        val list = StormDatabase.INSTANCE.userDao.getAll()
        list.isEmpty() ?: return null
        return list
    }

    fun addUser(vararg user: User) {
        user?.let {
            StormDatabase.INSTANCE.userDao.insertAll(*it)

        }

    }


    fun deleteUser(user: User?) {
        user?.let {
            StormDatabase.INSTANCE.userDao.remove(it)

        }
    }
}