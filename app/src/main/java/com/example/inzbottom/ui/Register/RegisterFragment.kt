package com.example.inzbottom.ui.Register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.inzbottom.R
import Api.ApiService
import Data.RegisterRequest
import Data.RegisterResponse
import Network.RetrofitClient
import com.example.inzbottom.ui.notifications.NotificationsFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameEditText = view.findViewById<EditText>(R.id.name)
        val emailEditText = view.findViewById<EditText>(R.id.email)
        val passwordEditText = view.findViewById<EditText>(R.id.password)
        val confirmPasswordEditText = view.findViewById<EditText>(R.id.c_password)
        val registerButton = view.findViewById<Button>(R.id.register_button)

        registerButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val c_password = confirmPasswordEditText.text.toString()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && c_password.isNotEmpty()) {
                if (password == c_password) {
                    register(name, email, password, c_password)
                } else {
                    Toast.makeText(requireContext(), "Passwords do not match", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun register(name: String, email: String, password: String, c_password: String) {
        val request = RegisterRequest(name, email, password, c_password)
        val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.register(request)

        call.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                if (response.isSuccessful) {
                    val registerResponse = response.body()
                    if (registerResponse != null) {
                        // Show a notification
                        showWelcomeNotification(name)
                        // Navigate to another fragment if needed
                        findNavController().navigate(R.id.action_registerFragment_to_homeFragment)
                    } else {
                        Toast.makeText(requireContext(), "Registration failed: No response body", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Registration failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Registration failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showWelcomeNotification(name: String) {
        // Assuming you want to use NotificationsFragment for the Toast
        val fragment = NotificationsFragment()
        fragment.showWelcomeNotification(name)
    }
}
