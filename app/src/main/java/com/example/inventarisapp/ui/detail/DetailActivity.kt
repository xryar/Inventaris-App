package com.example.inventarisapp.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.inventarisapp.data.response.ProductsItem
import com.example.inventarisapp.databinding.ActivityDetailBinding
import com.example.inventarisapp.utils.CurrencyUtils
import com.example.inventarisapp.utils.DateUtils

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        supportActionBar?.title = "Detail Data"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupView()
    }

    @Suppress("DEPRECATION")
    private fun setupView(){
        val detail = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(EXTRA_DATA, ProductsItem::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_DATA) as? ProductsItem
        }
        if (detail != null){
            binding.apply {
                tvProductName.text = detail.productName
                tvCategory.text = detail.category
                tvQuantity.text = detail.quantity.toString()
                tvTotalPrice.text = CurrencyUtils.formatToRupiah(detail.totalPrice)
                tvDate.text = DateUtils.formatDateString(detail.date)
                Glide.with(this@DetailActivity)
                    .load(detail.image)
                    .into(ivDetailPhoto)
            }
        }else{
            Toast.makeText(this, "Terjadi Error", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                @Suppress("DEPRECATION")
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object{
        const val EXTRA_DATA = "data"
    }
}