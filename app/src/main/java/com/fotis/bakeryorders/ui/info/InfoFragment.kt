package com.fotis.bakeryorders.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeOrderRepository
import com.fotis.bakeryorders.ui.gallery.CakeOrderViewModel
import com.fotis.bakeryorders.ui.gallery.CakeOrderViewModelFactory
import com.fotis.bakeryorders.ui.gallery.SearchType
import com.fotis.bakeryorders.ui.home.AppDatabase

import com.fotis.bakeryorders.ui.home.OrderInfoAdapter

class InfoFragment: Fragment() {

    private lateinit var viewModel: CakeOrderViewModel
    private lateinit var adapter: OrderInfoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.search_cake, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val editText = view.findViewById<EditText>(R.id.editTextQuery)
        val radioGroup = view.findViewById<RadioGroup>(R.id.radioGroup)
        val btnSearch = view.findViewById<Button>(R.id.btnSearch)
        val recyclerView = view.findViewById<RecyclerView>(R.id.resultsRecyclerView)


        val respository =
            CakeOrderRepository(AppDatabase.getDatabase(requireContext()).cakeDataDao())
        val factory = CakeOrderViewModelFactory(respository)
        viewModel = ViewModelProvider(this, factory).get(CakeOrderViewModel::class.java)



        adapter = OrderInfoAdapter(emptyList(), "")
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter


        viewModel.results.observe(viewLifecycleOwner) {
            adapter.updateData(it)
        }

        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioByName -> {
                    editText.hint = "Πληκτρολογήστε όνομα πελάτη"
                }
                R.id.radioByDay -> {
                    editText.hint = "Πληκτρολογήστε ημερομηνία (π.χ. 20/05/2025)"
                }
                R.id.radioByType -> {
                    editText.hint = "Πληκτρολογήστε είδος τούρτας( Ζαχαρόπαστα , Απλή τούρτα,Βουτυρόκρεμα"
                }
                R.id.radioByTime -> {
                    editText.hint = "Πληκτρολογήστε ώρα έναρξης (π.χ. 10:00)"
                }
                R.id.radioChecked -> {
                    editText.hint = "Δεν απαιτείται είσοδος"
                }
                R.id.radioByPhone-> {
                    editText.hint = "Πληκτρολογήστε τηλέφωνο πελάτη να ξεκιναει με 69 η 2310 και να εχει 10 γραμματα "
                }
            }
        }


        btnSearch.setOnClickListener {
            val query = editText.text.toString()
            val type = when (radioGroup.checkedRadioButtonId) {
                R.id.radioByName -> SearchType.BY_NAME
                R.id.radioByDay -> SearchType.BY_DAY
                R.id.radioByType -> SearchType.BY_TYPE
                R.id.radioByTime -> SearchType.BY_TIME
                R.id.radioChecked -> SearchType.CHECKED_ONLY
                else -> SearchType.BY_NAME
            }
            viewModel.search(query, type)
        }
    }








}