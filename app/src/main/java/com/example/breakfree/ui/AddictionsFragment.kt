package com.example.breakfree.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.breakfree.R
import com.example.breakfree.data.Addiction
import com.example.breakfree.data.AppDatabase
import com.example.breakfree.data.Quote
import com.example.breakfree.data.QuoteApiService
import com.example.breakfree.data.ActivityApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import android.view.inputmethod.InputMethodManager

class AddictionsFragment : Fragment() {
    private lateinit var adapter: AddictionAdapter
    private lateinit var db: AppDatabase
    private lateinit var quoteApi: QuoteApiService
    private lateinit var activityApi: ActivityApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_addictions, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerViewAddictions)
        val editText = view.findViewById<EditText>(R.id.editTextAddiction)
        val buttonAdd = view.findViewById<Button>(R.id.buttonAdd)
        val buttonFetchQuote = view.findViewById<Button>(R.id.buttonFetchQuote)
        val textQuote = view.findViewById<TextView>(R.id.textQuote)
        val buttonFetchActivity = view.findViewById<Button>(R.id.buttonFetchActivity)
        val textActivity = view.findViewById<TextView>(R.id.textActivity)

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

        // Retrofit for quotes
        quoteApi = Retrofit.Builder()
            .baseUrl("https://zenquotes.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuoteApiService::class.java)

        // Retrofit for activities (now Advice Slip API)
        activityApi = Retrofit.Builder()
            .baseUrl("https://api.adviceslip.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivityApiService::class.java)

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
                // Hide keyboard
                val imm = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
                loadAddictions()
            }
        }

        buttonFetchQuote.setOnClickListener {
            lifecycleScope.launch {
                textQuote.text = "Loading..."
                try {
                    val quotes = withContext(Dispatchers.IO) {
                        quoteApi.getRandomQuote()
                    }
                    if (quotes.isNotEmpty()) {
                        val quote = quotes[0]
                        textQuote.text = "\"${quote.q}\"\n- ${quote.a}"
                    } else {
                        textQuote.text = "No quote found."
                    }
                } catch (e: Exception) {
                    textQuote.text = "Failed to load quote."
                }
            }
        }

        buttonFetchActivity.setOnClickListener {
            lifecycleScope.launch {
                textActivity.text = "Loading..."
                try {
                    val adviceResponse = withContext(Dispatchers.IO) {
                        activityApi.getAdvice()
                    }
                    textActivity.text = adviceResponse.slip.advice
                } catch (e: Exception) {
                    textActivity.text = "Failed to load activity: ${e.message}"
                }
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