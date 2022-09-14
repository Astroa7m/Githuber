package com.astroscoding.githuber.common.data.local

import androidx.room.*
import com.astroscoding.githuber.common.data.local.model.RepositoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryDao {

    @Transaction
    @Query(
        "SELECT * FROM repositories ORDER BY " +
                "CASE WHEN :sort == 'stars' THEN starsCount END DESC," +
                "CASE WHEN :sort == 'forks' THEN forksCount END DESC," +
                "CASE WHEN :sort == 'issues' THEN issuesCount END DESC"
    )
    fun getAllRepos(sort: String): Flow<List<RepositoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRepo(repositoryEntity: RepositoryEntity)

    @Query("DELETE FROM repositories")
    suspend fun deleteAllRepos()

}