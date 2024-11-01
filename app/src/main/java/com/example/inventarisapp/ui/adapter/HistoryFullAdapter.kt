package com.example.inventarisapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.inventarisapp.R
import com.example.inventarisapp.data.response.ProductsItem
import com.example.inventarisapp.databinding.ListHistory2Binding
import com.example.inventarisapp.utils.CurrencyUtils
import com.example.inventarisapp.utils.DateUtils

class HistoryFullAdapter : RecyclerView.Adapter<HistoryFullAdapter.HistoryFullViewHolder>() {

    private val listProduct = ArrayList<ProductsItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun getDataProduct(data: ArrayList<ProductsItem>){
        val diffCallback = DiffutilCallback(listProduct, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listProduct.clear()
        listProduct.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryFullViewHolder {
        val binding = ListHistory2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryFullViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: HistoryFullViewHolder, position: Int) {
        val products = listProduct[position]
        holder.bind(products)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listProduct[position]
            )
        }
    }



    class HistoryFullViewHolder (private val _binding: ListHistory2Binding): RecyclerView.ViewHolder(_binding.root) {
        fun bind(products: ProductsItem){
            _binding.tvProductName2.text = products.productName
            _binding.tvTotalPrice2.text = CurrencyUtils.formatToRupiah(products.totalPrice)
            _binding.tvQuantity2.text = products.quantity.toString()
            _binding.tvDate2.text = DateUtils.formatDateString(products.date)

            val categoryIcon = when(products.category){
                "Elektronik" -> R.drawable.electronics
                "Pakaian" -> R.drawable.clothes
                "Makanan dan Minuman" -> R.drawable.food
                "Alat Tulis dan Perlengkapan Kantor" -> R.drawable.office
                "Peralatan Rumah Tangga" -> R.drawable.household
                "Bahan Bangunan" -> R.drawable.construction
                "Kendaraan dan Aksesori" -> R.drawable.vehicle
                "Peralatan Olahraga" -> R.drawable.sports
                "Mainan dan Hobi" -> R.drawable.game_hobby
                "Kesehatan dan Kecantikan" -> R.drawable.cosmetics
                else -> R.drawable.default_icon
            }
            _binding.ivIcon2.setImageResource(categoryIcon)
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback{
        fun onItemClicked(data: ProductsItem)
    }

    class DiffutilCallback(private val oldList: List<ProductsItem>, private val newList: List<ProductsItem>): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList == newList
        }

    }
}