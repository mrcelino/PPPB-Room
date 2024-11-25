package com.example.kpumarcelino

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.kpumarcelino.databinding.ActivityEditBinding
import com.example.mynote.database.Note
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditBinding
    private lateinit var noteDao: NoteDao
    private var noteId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeDatabase()
        noteId = intent.getIntExtra("note_id", 0)

        loadPemilihData()

        binding.btnSimpanTambah.setOnClickListener {
            updateData()
        }
    }

    private fun initializeDatabase() {
        val db = NoteRoomDatabase.getDatabase(this)
        noteDao = db?.noteDao() ?: throw IllegalStateException("Database not initialized")
    }

    private fun updateData() {
        val selectedGender = getSelectedGender()
        val namaPemilih = binding.etNamaEdit.text.toString()
        val nik = binding.etNikEdit.text.toString()
        val alamat = binding.etAlamat.text.toString()

        if (validateInput(namaPemilih, nik, alamat)) {
            lifecycleScope.launch(Dispatchers.IO) {
                noteDao.update(
                    Note(
                        id = noteId,
                        nama_pemilih = namaPemilih,
                        nik = nik,
                        gender = selectedGender,
                        alamat = alamat
                    )
                )
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@EditActivity, "Data updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        } else {
            Toast.makeText(this, "Isi semua field terlebih dahulu", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }

    private fun getSelectedGender(): String {
        return when (binding.radioGroupGender.checkedRadioButtonId) {
            binding.radioLaki.id -> "Laki-laki"
            binding.radioWanita.id -> "Perempuan"
            else -> ""
        }
    }

    private fun loadPemilihData() {
        lifecycleScope.launch(Dispatchers.IO) {
            noteDao.getVoterById(noteId).collect { pemilih ->
                pemilih?.let {
                    withContext(Dispatchers.Main) {
                        binding.apply {
                            etNamaEdit.setText(it.nama_pemilih)
                            etNikEdit.setText(it.nik)
                            if (it.gender == "Laki-laki") {
                                radioLaki.isChecked = true
                            } else {
                                radioWanita.isChecked = true
                            }
                            etAlamat.setText(it.alamat)
                        }
                    }
                }
            }
        }
    }
}
