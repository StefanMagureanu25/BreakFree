package com.example.breakfree.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakfree.R
import com.example.breakfree.data.Addiction
import com.example.breakfree.data.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddictionsFragment : Fragment() {
    private lateinit var adapter: AddictionAdapter
    private lateinit var db: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addictions, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAddictions)
        val editText = view.findViewById<EditText>(R.id.editTextAddiction)
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)

        db = AppDatabase.getDatabase(requireContext())
        adapter = AddictionAdapter(listOf()) { addiction ->
            // On delete button click, delete the addiction
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.addictionDao().delete(addiction)
                }
                Toast.makeText(requireContext(), "Deleted: ${addiction.name}", Toast.LENGTH_SHORT).show()
                loadAddictions()
            }
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        loadAddictions()

        buttonAdd.setOnClickListener {
            val name = editText.text.toString().trim()
            if (name.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter an addiction name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val addiction = Addiction(name = name, dateStarted = Date().time)
            lifecycleScope.launch {
                withContext(Dispatchers.IO) {
                    db.addictionDao().insert(addiction)
                }
                editText.text.clear()
                loadAddictions()
            }
        }

        return view
    }

    private fun loadAddictions() {
        lifecycleScope.launch {
            val addictions = withContext(Dispatchers.IO) {
                db.addictionDao().getAll()
            }
            adapter.updateData(addictions)
        }
    }
} 