package com.example.inzbottom.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.inzbottom.MainActivity
import com.example.inzbottom.R
import Api.ApiService
import Data.LoginRequest
import Data.LoginResponse
import Network.RetrofitClient
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.inzbottom.ui.Register.RegisterFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LoginActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if the token is already saved
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("auth_token", null)

        if (token != null) {
            Log.d(TAG, "Token found: $token")
            // Token found, proceed to MainActivity
            navigateToMainActivity()
        } else {
            Log.d(TAG, "No token found")
            // No token found, show the login screen
            setContentView(R.layout.fragment_login)

            val usernameEditText = findViewById<EditText>(R.id.username)
            val passwordEditText = findViewById<EditText>(R.id.password)
            val loginButton = findViewById<Button>(R.id.login_button)
            val registerButton = findViewById<Button>(R.id.registerButton)



            loginButton.setOnClickListener {
                val username = usernameEditText.text.toString()
                val password = passwordEditText.text.toString()
                if (username.isNotEmpty() && password.isNotEmpty()) {
                    login(username, password)
                } else {
                    Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
                }
            }
            registerButton.setOnClickListener {
                navigateToRegisterActivity()
            }
        }
    }

    private fun login(username: String, password: String) {
        val request = LoginRequest(username, password)
        val apiService: ApiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.login(request)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Save the token in SharedPreferences
                        saveToken(loginResponse.token)

                        // Navigate to MainActivity
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: No response body", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "Login failed: ${response.message()}", Toast.LENGTH_SHORT).show()
                    println("Login failed: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity, "Login failed: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Login error", t)
            }
        })
    }

    private fun saveToken(token: String) {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("auth_token", token)
        editor.apply()
        Log.d(TAG, "Token saved: $token")
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close the login activity so the user can't go back to it
    }

    private fun navigateToRegisterActivity() {
        val intent = Intent(this, RegisterFragment::class.java)
        startActivity(intent)
        finish() // Close the login activity so the user can't go back to it
    }


    @Composable
    fun myApp(){
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "loginscreen"){
            composable("loginscreen"){}
            composable(route = "registerscreen"){}
        }
    }
}
