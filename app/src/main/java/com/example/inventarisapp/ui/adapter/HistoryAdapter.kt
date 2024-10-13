package com.example.inventarisapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.inventarisapp.data.response.ProductsItem
import com.example.inventarisapp.databinding.ListHistoryBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    private val listProduct = ArrayList<ProductsItem>()
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun getDataProduct(data: ArrayList<ProductsItem>){
        val diffCallback = DiffutilCallback(listProduct, data)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        listProduct.clear()
        listProduct.addAll(data)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ListHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val products = listProduct[position]
        holder.bind(products)
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(
                listProduct[position]
            )
        }
    }

    class HistoryViewHolder (private val _binding: ListHistoryBinding): RecyclerView.ViewHolder(_binding.root) {
        fun bind(products: ProductsItem){
            _binding.tvInfo.text = products.productName
            _binding.tvDate.text = products.date
            _binding.tvQuantity.text = products.quantity.toString()
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun interface OnItemClickCallback{
        fun onItemClicked(data: ProductsItem)
    }

    class DiffutilCallback(private val oldList: List<ProductsItem>, private val newList :List<ProductsItem>) : DiffUtil.Callback(){
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