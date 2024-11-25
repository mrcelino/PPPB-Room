package com.example.kpumarcelino

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynote.database.NoteDao
import com.example.mynote.database.NoteRoomDatabase
import com.example.kpumarcelino.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var prefManager: PrefManager
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi PrefManager dan cek login
        prefManager = PrefManager.getInstance(this)
        checkLoginStatus()

        // Inisialisasi database dan executor service
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        // Setup listener untuk tombol
        setupListeners()
    }

    private fun setupListeners() {
        with(binding) {
            btnTambahdata.setOnClickListener {
                startActivity(Intent(this@MainActivity, TambahDataActivity::class.java))
            }

            btnLogout.setOnClickListener {
                logoutUser()
            }
        }
    }

    private fun checkLoginStatus() {
        if (!prefManager.isLoggedIn()) {
            navigateToLogin()
        }
    }

    private fun logoutUser() {
        prefManager.setLoggedIn(false)
        navigateToLogin()
    }

    private fun navigateToLogin() {
        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
        finish()
    }

    private fun getAllNotes() {
        with(binding.rvPemilih) {
            layoutManager = LinearLayoutManager(this@MainActivity)
            mNotesDao.allNotes.observe(this@MainActivity) { pemilih ->
                adapter = PemilihAdapter(
                    pemilih,
                    onDeleteClick = { position ->
                        val note = (adapter as PemilihAdapter).listPemilih[position]
                        executeOnBackground { mNotesDao.delete(note) }
                    },
                    onEditClick = { position ->
                        val pemilih = (adapter as PemilihAdapter).listPemilih[position]
                        navigateToEdit(pemilih.id)
                    },
                    onViewClick = { position ->
                        val pemilih = (adapter as PemilihAdapter).listPemilih[position]
                        navigateToView(pemilih.id)
                    }
                )
            }
        }
    }

    private fun navigateToEdit(noteId: Int) {
        val intent = Intent(this@MainActivity, EditActivity::class.java)
        intent.putExtra("note_id", noteId)
        startActivity(intent)
    }

    private fun navigateToView(noteId: Int) {
        val intent = Intent(this@MainActivity, LihatDataActivity::class.java)
        intent.putExtra("note_id", noteId)
        startActivity(intent)
    }

    private fun executeOnBackground(task: () -> Unit) {
        executorService.execute(task)
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }
}
