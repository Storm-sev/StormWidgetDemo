package com.example.customnewdemo.act.room


import androidx.room.*
import org.jetbrains.annotations.Nullable


// indices 返回表上的索引列表.
@Entity(
    tableName = "user",
    indices = [Index(
        value = ["id"],
        unique = true
    )]
)
data class User(
    // 这个注解代表指定该属性作为表的主键, 自增长.

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    // 该注解设置当前属性在数据库中的列名和类型,
    // 可以不进行设置,如果不设置的话那么列名和属性名相同
    @Nullable
    @ColumnInfo(name = "user_name", typeAffinity = ColumnInfo.TEXT)
    public val userName: String?,

    @Nullable
    @ColumnInfo(name = "age", typeAffinity = ColumnInfo.INTEGER)
    public val age: Int?,
    // Entity 中所有的属性都会被持久化到数据库中,
    // 如果用 @Ignore 标识之后就不会对其属性进行持久化
//    @ColumnInfo(name = "user_gender", typeAffinity = ColumnInfo.TEXT)
//    @Nullable
//    @Ignore var userGender: String?
)


@Dao
interface UserDAO {

    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIds)")
    fun getUsers(userIds: IntArray): List<User>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(vararg user: User)

    //更新操作, 发生冲突的时候的策略
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Delete
    fun remove(user: User)
}