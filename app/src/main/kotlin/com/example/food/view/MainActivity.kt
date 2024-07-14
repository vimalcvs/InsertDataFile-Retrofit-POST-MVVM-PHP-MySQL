package com.example.food.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.food.R
import com.example.food.databinding.ActivityMainBinding
import com.example.food.viewmodel.ViewModelFood
import dagger.hilt.android.AndroidEntryPoint
import java.io.File

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModelFood: ViewModelFood by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var selectedImageFile: File
    private var latestTmpUri: Uri? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                latestTmpUri = it
                binding.imageView.setImageURI(latestTmpUri)
                selectedImageFile = uriToFile(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        binding.imageView.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.btnInsertFood.setOnClickListener {
            val foodName = binding.editTextFoodName.text.toString()
            val foodQty = binding.editTextFoodQuantity.text.toString()
            if (::selectedImageFile.isInitialized) {
                viewModelFood.insertFood(foodName, foodQty, selectedImageFile)
            } else {
                Toast.makeText(this, "Please select an image!", Toast.LENGTH_SHORT).show()
            }
            hideKeyboard()
        }
        observeViewModel()
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun observeViewModel() {
        viewModelFood.isLoading.observe(this) { isLoading ->
            binding.pvProgress.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModelFood.isNoNetwork.observe(this) { isNoNetwork ->
            binding.tvNoNetwork.visibility = if (isNoNetwork) View.VISIBLE else View.GONE
        }

        viewModelFood.insertResponse.observe(this) { response ->
            response?.let {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uriToFile(uri: Uri): File {
        val file = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        contentResolver.openInputStream(uri)?.use { inputStream ->
            file.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }
        return file
    }
}
