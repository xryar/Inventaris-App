package com.example.inventarisapp.ui.upload

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.inventarisapp.MainActivity
import com.example.inventarisapp.ViewModelFactory
import com.example.inventarisapp.authentication.login.TokenSession
import com.example.inventarisapp.databinding.ActivityUploadBinding
import com.example.inventarisapp.ui.home.HomeFragment
import com.example.inventarisapp.utils.getImageUri
import com.example.inventarisapp.utils.reduceFileImage
import com.example.inventarisapp.utils.uriToFile
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding
    private val viewModel by viewModels<UploadViewModel>{
        ViewModelFactory.getInstance(this)
    }
    private var currentImageUri: Uri? = null
    private var selectedDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        val categoryList = listOf(
            "Elektronik", "Pakaian", "Makanan dan Minuman", "Alat Tulis dan Perlengkapan Kantor",
            "Peralatan Rumah Tangga", "Bahan Bangunan", "Kendaraan dan Aksesori", "Peralatan Olahraga",
            "Mainan dan Hobi", "Kesehatan dan Kecantikan"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategory.adapter = adapter

        selectedDate = getCurrentDate()
        binding.textDate.text = selectedDate
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.btnSave.setOnClickListener { uploadData() }

    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val  launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media Selected")
        }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun uploadData() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val selectedCategory = binding.spCategory.selectedItem.toString()
            Log.d("Image File", "showImage: ${imageFile.path}")

            val tokenSession = TokenSession(this)
            val token = tokenSession.passToken().toString()

            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val multipartBody = MultipartBody.Part.createFormData(
                "image",
                imageFile.name,
                requestImageFile
            )

            val productName = binding.edTitle.text.toString().toRequestBody("text/plain".toMediaType())
            val category = selectedCategory.toRequestBody("text/plain".toMediaType())
            val quantity = binding.edQuantity.text.toString().toRequestBody("text/plain".toMediaType())
            val price = binding.edPrice.text.toString().toRequestBody("text/plain".toMediaType())

            showLoading(true)
            viewModel.uploadData(
                "Bearer $token",
                multipartBody,
                productName,
                category,
                quantity,
                price
            )
            viewModel._uploadData.observe(this) {
                showLoading(false)
                AlertDialog.Builder(this).apply {
                    setTitle("Yeah!")
                    setMessage("Anda berhasil upload")
                    setPositiveButton("Kembali") { _, _ ->
                        val intent = Intent(context, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                    show()
                }
            }
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImage.setImageURI(it)
        }
    }

    private fun getCurrentDate(): String{
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return dateFormat.format(currentDate)
    }

    private fun showLoading(isLoading: Boolean){
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}





