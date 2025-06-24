package com.example.breakfree.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.breakfree.R

class LoginFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        val textGoToRegister = view.findViewById<TextView>(R.id.textGoToRegister)
        val buttonLogin = view.findViewById<Button>(R.id.buttonLogin)
        val editTextUsername = view.findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)

        textGoToRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        buttonLogin.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Retrieve credentials from SharedPreferences
            val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            val savedUsername = prefs.getString("username", null)
            val savedPassword = prefs.getString("password", null)

            if (username == savedUsername && password == savedPassword) {
                Toast.makeText(requireContext(), "Login successful!", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_addictionsFragment)
            } else {
                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
} 