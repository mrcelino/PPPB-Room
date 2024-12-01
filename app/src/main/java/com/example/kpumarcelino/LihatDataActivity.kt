package com.example.kpumarcelino

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kpumarcelino.databinding.ActivityLihatDataBinding
import com.example.kpumarcelino.database.NoteDao
import com.example.kpumarcelino.database.NoteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LihatDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLihatDataBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLihatDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        noteId = intent.getIntExtra("note_id", 0)

        loadPemilihData()

        binding.btnKembaliLihat.setOnClickListener {
            navigateToMainActivity()
        }
    }

    private fun initializeDatabase() {
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db?.noteDao() ?: throw IllegalStateException("Database not initialized")
    }

    private fun loadPemilihData() {
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.getVoterById(noteId).collect { pemilih ->
                pemilih?.let {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            etNamaLihat.setText(it.nama_pemilih)
                            etNikLihat.setText(it.nik)
                            etGenderLihat.setText(it.gender)
                            etAlamatLihat.setText(it.alamat)
                        }
                    }
                }
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Opsional: tutup aktivitas saat ini
    }
}
