package com.example.madlevel4example

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_add_reminder.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
const val REQ_PRODUCT_KEY = "req_reminder"
const val BUNDLE_PRODUCT_KEY_NAME = "bundle_product_name"
const val BUNDLE_PRODUCT_KEY_QUANTITY = "bundle_product_quantity"

class AddReminderFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_reminder, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        btnAddReminder.setOnClickListener {
//            onAddReminder()
//        }

        super.onViewCreated(view, savedInstanceState)
    }

//    private fun onAddReminder() {
//        // TODO INPUT AANPASSEN
//        val reminderText = etReminderName.text.toString()
//        val urlTitle = input_title.text.toString()
//        val Url = input_url.text.toString()
//
//        if(reminderText.isNotBlank()) {
//            setFragmentResult(REQ_PRODUCT_KEY, bundleOf(
//                BUNDLE_PRODUCT_KEY_NAME to urlTitle, BUNDLE_PRODUCT_KEY_QUANTITY to Url
//            ))
//
//            findNavController().popBackStack()
//        } else {
//            Toast.makeText(
//                activity,
//                R.string.not_valid_reminder, Toast.LENGTH_SHORT
//            ).show()
//        }
//    }
}