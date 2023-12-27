package com.example.pract2_2

import androidx.room.*

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    suspend fun getRecentPlaylist(): List<Playlist>   //выборка всех записей

    @Insert
    suspend fun insertPlaylist(playlist: Playlist)  //вставка в базу данных

    @Delete
    suspend fun deletePlaylist(playlist: Playlist) //удаление из базы данных

    @Query("DELETE FROM playlists")
    suspend fun deleteAllPlayslists()  //удаление всех записей

    @Update
    suspend fun updatePlayslists(playlist: Playlist) //обновление базы данных


}
