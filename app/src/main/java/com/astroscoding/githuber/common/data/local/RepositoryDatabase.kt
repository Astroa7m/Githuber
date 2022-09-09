package com.astroscoding.githuber.common.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.astroscoding.githuber.common.data.local.model.RepositoryEntity

@Database(
    entities = [RepositoryEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RepositoryDatabase: RoomDatabase() {

    abstract val repositoriesDao: RepositoryDao

}