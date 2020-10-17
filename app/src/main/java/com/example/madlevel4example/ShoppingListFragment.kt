package com.example.madlevel4example

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_reminders.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ShoppingListFragment : Fragment() {

    private lateinit var productRepository: ProductRepository
    private val mainScope = CoroutineScope(Dispatchers.Main)
    private val shoppingList = arrayListOf<Product>()
    private val productAdapter = ProductAdapter(shoppingList)

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        productRepository = ProductRepository(requireContext())
        getProductsFromDatabase()

        fab.setOnClickListener {
            showProductDialog()
        }
    }

    @SuppressLint("InflateParams")
    private fun showProductDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.add_product_dialog_title))
        val dialogLayout =
            layoutInflater.inflate(R.layout.add_product_dialog, null)
        val productName = dialogLayout.findViewById<EditText>(R.id.txt_product_name)
        val amount = dialogLayout.findViewById<EditText>(R.id.txt_amount)

        builder.setView(dialogLayout)
        builder.setPositiveButton(R.string.dialog_ok_btn) { _: DialogInterface, _: Int ->
            addProduct(productName, amount)
        }
        builder.show()
    }

    private fun getProductsFromDatabase() {
        mainScope.launch {
            val shoppingList = withContext(Dispatchers.IO) {
                productRepository.getAllProducts()
            }
            this@ShoppingListFragment.shoppingList.clear()
            this@ShoppingListFragment.shoppingList.addAll(shoppingList)
            productAdapter.notifyDataSetChanged()
        }
    }

    private fun initViews() {
        rvReminders.layoutManager =
            LinearLayoutManager(context, RecyclerView.VERTICAL,false)
        rvReminders.adapter = productAdapter
        rvReminders.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        observeAddReminderResult()
        createItemTouchHelper().attachToRecyclerView(rvReminders)
    }

    private fun observeAddReminderResult() {
        setFragmentResultListener(REQ_PRODUCT_KEY) { key, bundle ->
                var name = ""
                var quantity: Int = 0

                bundle.getString(BUNDLE_PRODUCT_KEY_NAME)?.let {
                    name = it
                } ?: Log.e("SecondFragment", "Request triggered, but empty title text!")

                bundle.getInt(BUNDLE_PRODUCT_KEY_QUANTITY)?.let {
                    quantity = it
                } ?: Log.e("SecondFragment", "Request triggered, but empty url text!")

                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.insertProduct(Product(name, quantity))
                    }
                    getProductsFromDatabase()
                }
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper {

        val callback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition

                val productToDelete = shoppingList[position]
                mainScope.launch {
                    withContext(Dispatchers.IO) {
                        productRepository.deleteProduct(productToDelete)
                    }
                    getProductsFromDatabase()
                }
            }
        }
        return ItemTouchHelper(callback)
    }

}