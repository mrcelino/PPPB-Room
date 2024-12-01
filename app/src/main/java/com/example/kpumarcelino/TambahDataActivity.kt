package com.example.kpumarcelino

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kpumarcelino.database.Note
import com.example.kpumarcelino.database.NoteDao
import com.example.kpumarcelino.database.NoteRoomDatabase
import com.example.kpumarcelino.databinding.ActivityTambahDataBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TambahDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTambahDataBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        with(binding) {
            btnSimpanTambah.setOnClickListener {
                handleSaveButtonClick()
            }
        }
    }

    private fun handleSaveButtonClick() {
        val namaPemilih = binding.etNamaTmbh.text.toString()
        val nik = binding.etNikTmbh.text.toString()
        val alamat = binding.etAlamat.text.toString()
        val selectedGender = getSelectedGender()

        if (validateInput(namaPemilih, nik, alamat)) {
            val note = Note(
                nama_pemilih = namaPemilih,
                nik = nik,
                gender = selectedGender,
                alamat = alamat
            )
            insert(note)
            navigateToMainActivity()
        } else {
            showToast("Isi semua field terlebih dahulu")
        }

        setEmptyField()
    }

    private fun getSelectedGender(): String {
        return when (binding.radioGroupGender.checkedRadioButtonId) {
            binding.radioLaki.id -> "Laki-laki"
            binding.radioWanita.id -> "Perempuan"
            else -> ""
        }
    }

    private fun validateInput(namaPemilih: String, nik: String, alamat: String): Boolean {
        return namaPemilih.isNotBlank() && nik.isNotBlank() && alamat.isNotBlank()
    }

    private fun insert(note: Note) {
        executorService.execute {
            mNotesDao.insert(note)
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this@TambahDataActivity, MainActivity::class.java))
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this@TambahDataActivity, message, Toast.LENGTH_SHORT).show()
    }

    private fun setEmptyField() {
        with(binding) {
            etNamaTmbh.setText("")
            etNikTmbh.setText("")
            etAlamat.setText("")
        }
    }
}
