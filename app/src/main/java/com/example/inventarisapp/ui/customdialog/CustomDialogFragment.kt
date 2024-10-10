package com.example.inventarisapp.ui.customdialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.inventarisapp.databinding.CustomDialogBinding


class CustomDialogFragment : DialogFragment() {

    private lateinit var binding: CustomDialogBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireActivity())
        val inflater = requireActivity().layoutInflater
        binding = CustomDialogBinding.inflate(inflater, null, false)

        val category = arguments?.getString(CATEGORY) ?: ""
        val isCategoryEditable = arguments?.getBoolean(IS_CATEGORY_EDITABLE) ?: false

        val categoryList = listOf("Groceries", "Food", "Utilities", "Shopping", "Transportation", "Mobile Phone", "Rent", "Entertainment", "Health", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spCategory.adapter = adapter

        if (isCategoryEditable) {
            binding.spCategory.isEnabled = true
            binding.spCategory.setSelection(categoryList.indexOf(category))
        } else {
            binding.spCategory.setSelection(categoryList.indexOf(category))
            binding.spCategory.isEnabled = false
        }

        val title = binding.edTitle.text
        binding.labelDate.setOnClickListener {
            showDatePickerDialog()
        }

        val quantity = binding.edQuantity.text
        val price = binding.edPrice.text

        binding.btnSave.setOnClickListener {
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        builder.setView(binding.root)

        return builder.create()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =DatePickerDialog(requireContext(), {_, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            binding.textDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    companion object {
        private const val CATEGORY = "category"
        private const val IS_CATEGORY_EDITABLE = "isCategoryEditable"

        fun newInstance(category: String, isCategoryEditable: Boolean): CustomDialogFragment {
            val fragment = CustomDialogFragment()
            val args = Bundle()
            args.putString(CATEGORY, category)
            args.putBoolean(IS_CATEGORY_EDITABLE, isCategoryEditable)
            fragment.arguments = args
            return fragment
        }
    }
}