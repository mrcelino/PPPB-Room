package com.example.mynote.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.kpumarcelino.Pemilih
import kotlinx.coroutines.flow.Flow

//kueri untuk ngelola data dalam db
@Dao
interface NoteDao {
    //kalau ada id yang yang sama , maka ga bakal dimasukkan ke dalam db
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: Note)
    @Update
    fun update(note: Note)
    @Delete
    fun delete(note: Note)
    @get:Query("SELECT * from note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Note>>
    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getVoterById(id: Int): Flow<Pemilih>

}