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

class RegisterFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        val textGoToLogin = view.findViewById<TextView>(R.id.textGoToLogin)
        val buttonRegister = view.findViewById<Button>(R.id.buttonRegister)
        val editTextUsername = view.findViewById<EditText>(R.id.editTextUsername)
        val editTextPassword = view.findViewById<EditText>(R.id.editTextPassword)

        textGoToLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        buttonRegister.setOnClickListener {
            val username = editTextUsername.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save credentials to SharedPreferences
            val prefs = requireContext().getSharedPreferences("auth", Context.MODE_PRIVATE)
            prefs.edit().putString("username", username).putString("password", password).apply()

            Toast.makeText(requireContext(), "Registration successful!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_registerFragment_to_addictionsFragment)
        }

        return view
    }
} 