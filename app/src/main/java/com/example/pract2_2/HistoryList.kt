package com.example.pract2_2

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pract2_2.databinding.ActivityHistoryListBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryList : Activity() {

    private lateinit var binding: ActivityHistoryListBinding
    private lateinit var dobavit:Button
    private lateinit var udalit:Button
    private lateinit var list:RecyclerView
    private lateinit var database:PlaylistDatabase
    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Room.databaseBuilder(
            applicationContext,
            PlaylistDatabase::class.java,"playlist-database"
        )
            .build()

        list = findViewById(R.id.list)
        dobavit = findViewById(R.id.dob)
        udalit = findViewById(R.id.udal)
        list.layoutManager = LinearLayoutManager(this)
        adapter = PlaylistAdapter()
        list.adapter = adapter

        GlobalScope.launch (Dispatchers.IO) {
            val data = database.playlistDao().getRecentPlaylist()
            withContext(Dispatchers.Main){
                adapter.submitList(data)
            }
        }
        adapter.setOnDeleteClickListener { playlist ->
            GlobalScope.launch (Dispatchers.IO) {
                database.playlistDao().deletePlaylist(playlist)

                val data = database.playlistDao().getRecentPlaylist()
                withContext(Dispatchers.Main){
                    adapter.submitList(data)
                }
            }
        }
        adapter.setOnEditClickListener { playlist -> EditMenu(playlist)  }
    }
    private fun EditMenu(playlist:Playlist)
    {
        val mes = AlertDialog.Builder(this)
        val lay = LinearLayout(this)

        lay.orientation = LinearLayout.VERTICAL
        val name = playlist.name
        val singer = playlist.singer
        mes.setTitle("Редактор")
        val ed1 = EditText(this)
        val ed2 = EditText(this)
        ed1.hint = "Имя артиста"
        ed2.hint = "Описание"
        ed1.setText("${name}")
        ed2.setText("${singer}")
        lay.addView(ed1)
        lay.addView(ed2)
        mes.setView(lay)
        mes.setPositiveButton("Сохранить") {_,_ ->
            val newname = ed1.text.toString()
            val newsinger = ed2.text.toString()
            if(newname.isEmpty() || newsinger.isEmpty() )
            {
                val alertDialog1 = AlertDialog.Builder(this)
                alertDialog1.setTitle("Ошибка")
                alertDialog1.setMessage("Заполните все поля")
                alertDialog1.setPositiveButton("ОК") {_,_ ->
                    showAddDialog()
                }
                val alertDialog2 = alertDialog1.create()
                alertDialog2.show()
            }
            else
            {
                val update = playlist.copy(name = newname, singer = newsinger)
                GlobalScope.launch(Dispatchers.IO) {
                    database.playlistDao().updatePlayslists(update)
                    val data = database.playlistDao().getRecentPlaylist()
                    withContext(Dispatchers.Main) {
                        adapter.submitList(data)
                    }
                }
            }
        }
        mes.setNegativeButton("Нет", DialogInterface.OnClickListener{ dialog, which->
            Toast.makeText(this,"Окс", Toast.LENGTH_LONG).show()
        })
        mes.show()

        }

    fun dobavit(view: View) {
        showAddDialog()

    }
    private fun showAddDialog(){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Добавление")
        val input = LinearLayout(this)
        input.orientation = LinearLayout.VERTICAL
        val nameEdit = EditText(this)
        nameEdit.hint = "Название"
        val singerEdit = EditText(this)
        singerEdit.hint = "Описание"

        input.addView(nameEdit)
        input.addView(singerEdit)
        alertDialog.setView(input)

        alertDialog.setPositiveButton("Добавить") {_,_ ->
            val nameSt = nameEdit.text.toString()
            val singerSt = singerEdit.text.toString()

            if(nameSt.isNotBlank() || singerSt.isNotBlank())
            {
                val newString = Playlist(name = nameSt, singer = singerSt)
                GlobalScope.launch(Dispatchers.IO) {
                    database.playlistDao().insertPlaylist(newString)
                    val playlists = database.playlistDao().getRecentPlaylist()
                    withContext(Dispatchers.Main) {
                        adapter.submitList(playlists)
                    }
                }
            }
            else
            {
                val alertDialog1 = AlertDialog.Builder(this)
                alertDialog1.setTitle("Ошибка")
                alertDialog1.setMessage("Заполните все поля")
                alertDialog1.setPositiveButton("ОК") {_,_ ->
                    showAddDialog()
                }
                val alertDialog2 = alertDialog1.create()
                alertDialog2.show()
            }
        }
        alertDialog.setNegativeButton("Отмена") {dialog, _ ->
            dialog.dismiss()
        }
        val alertDialog2 = alertDialog.create()
        alertDialog2.show()

    }
    fun udalit(view: View) {
        val mes = AlertDialog.Builder(this)
        mes.setTitle("Вопрос")
        mes.setMessage("Вы хотите удалить все данные?")
        mes.setPositiveButton("YES OF COURSE", DialogInterface.OnClickListener { dialog, which->
            GlobalScope.launch  (Dispatchers.Main) {
                database.playlistDao().deleteAllPlayslists()

                val data = database.playlistDao().getRecentPlaylist()
                withContext(Dispatchers.Main)
                {
                    adapter.submitList(data)
                }
            }
        })
        mes.setNegativeButton("Не надо", DialogInterface.OnClickListener{ dialog, which->
            Snackbar.make(view,"Окс",Snackbar.LENGTH_LONG)
                .show()
        })
        mes.show()
    }
}