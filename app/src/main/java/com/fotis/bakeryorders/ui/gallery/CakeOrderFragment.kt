package com.fotis.bakeryorders.ui.gallery

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController

import com.fotis.bakeryorders.databinding.FragmentCakeOrderBinding
import com.fotis.bakeryorders.ui.home.AppDatabase
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Calendar

class CakeOrderFragment : Fragment() {

    private var _binding: FragmentCakeOrderBinding? = null
    private var flagKilla: Boolean = false
    private var flagMera: Boolean = false
    private var flagOra: Boolean = false
    private var flagThlefono: Boolean = false
    private var flagOnomateponimo: Boolean = false

    private val binding get() = _binding!!


    private lateinit var dateEdt: EditText // Το EditText για την ημερομηνία
    private lateinit var timeEdt: EditText// Το EditText για την Ωρα
    private lateinit var VaflaEdt: TextView// To VaflaEdt για την ενδηξη του email
    private lateinit var SpinnerVaflaEdt: Spinner //Το αποτελεσμα του Spinner

    private lateinit var OnomaPelati: EditText// To onomateponemo tou pelati
    private lateinit var Tilefono_Pelati: EditText//To tilefono tou pelati
    private lateinit var ButtonOloklirosis: Button//To Koumpi oloklhrosis
    private lateinit var idEdtNumberText: EditText// To EditText για τα κιλα της tourtas


    private lateinit var SpinnerEidos: Spinner//To Spinner για τα είδη της tourτας
    private lateinit var SpinnerGeusi: Spinner//To Spinner για τιςsizes της tourτας
    private lateinit var SpinnerSxima: Spinner//To Spinner για τις εξαιρέσεις της tourτας
    private lateinit var txtSxolia: EditText //To EditText για τις Γευση της tourτας

    private lateinit var cakeOrderViewModel: CakeOrderViewModel
    private lateinit var cakeDataDao: CakeDataDao


    val db = Firebase.firestore


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        val respository =
            CakeOrderRepository(AppDatabase.getDatabase(requireContext()).cakeDataDao())
        val factory = CakeOrderViewModelFactory(respository)
        cakeOrderViewModel = ViewModelProvider(this, factory).get(CakeOrderViewModel::class.java)

        _binding = FragmentCakeOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root



        SpinnerEidos = binding.SpEidosTourtas
        var spinnerEidos = SpinnerEidos.getSelectedItem().toString()

        SpinnerSxima = binding.spinner
        var spinnerSxima = SpinnerSxima.getSelectedItem().toString()

        SpinnerGeusi = binding.SpinnerGeusisToyrtas
        var spinnerGeusi = SpinnerGeusi.getSelectedItem().toString()


        // Επιλογη του αριθμου στην συναρτηση
        dateEdt = binding.idDateEdt

        //Elexnos an exei ora mesa
        dateEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                flagMera = true
                Elenxos_Oloklirosi_Paraggelias()

            }
        })


        dateEdt.setOnClickListener {
            showDatePicker()
        }

        // Επιλογη του Ωρας στην συναρτηση
        timeEdt = binding.idTimeEdt

        //Elenxos an exei text mesa stin ora
        timeEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                flagOra = true
                Elenxos_Oloklirosi_Paraggelias()

            }
        })

        timeEdt.setOnClickListener {
            showTimePicker(timeEdt)
            if(timeEdt.text.toString().isNotEmpty())
            flagOra = true
            else
                flagOra = false
            Elenxos_Oloklirosi_Paraggelias()
        }


        // Επιλογη του Ναι στην Βαφλα  εκτρυπωση στην συναρτηση
        VaflaEdt = binding.Emailmessage
        SpinnerVaflaEdt = binding.RdBVaflaEktiposi

        SpinnerVaflaEdt.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent.getItemAtPosition(position).toString()
                //Toast.makeText(requireContext(), "Επιλέχθηκε: $selectedItem", Toast.LENGTH_SHORT).show()
                if (selectedItem.equals("Ναι")) {
                    VaflaEdt.visibility = View.VISIBLE
                } else {
                    VaflaEdt.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Αν δεν επιλεγεί τίποτα
            }
        }


        //Elenxos onomatos
        OnomaPelati = binding.OnomateponimoPelati

        OnomaPelati.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                // Optional: validation here
                if (OnomaPelati.text.toString().trim().length > 2) {
                    flagOnomateponimo = true
                    Elenxos_Oloklirosi_Paraggelias()
                }

                // Hide keyboard
                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.windowToken, 0)

                // Optionally: move to next field
                // Tilefono_Pelati.requestFocus()

                true
            } else {
                false
            }
        }
        //showKeyboard(OnomaPelati)

        OnomaPelati.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {

                if (s.toString().length > 2) {

                    flagOnomateponimo = true
                    Elenxos_Oloklirosi_Paraggelias()
                }


            }
        })


// Ελεχος αριθμου και εφμανισει του λαθους
        Tilefono_Pelati = binding.ThilefonoPelati

        Tilefono_Pelati.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                v.clearFocus() // Χάνουμε την εστίαση για να αποθηκευτεί σωστά το κείμενο
                validatePhoneNumber()

                if (flagThlefono) {
                    // Hide keyboard if validation passed
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)

                    // Optionally: Move focus to next input (e.g., phone)
                    //Tilefono_Pelati.requestFocus()
                }

                true
            } else {
                false
            }
        }

        idEdtNumberText = binding.idEdtNumber


        //Επιλογη αριθμου στο πεδιο για τα κιλα της τουρτας
        idEdtNumberText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                validateKilaTourtas()

                if (flagKilla) {
                    // Hide keyboard if validation passed
                    val imm =
                        requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)

                    // Optionally: Move focus to next input (e.g., phone)
                    //Tilefono_Pelati.requestFocus()
                }

                true
            } else {
                false
            }
        }

        ButtonOloklirosis = binding.btnOrder
        ButtonOloklirosis.setOnClickListener {


            //Για διαγραφη ολου του πινακα
           // deletAllTable()

                //if(ButtonOloklirosis.isEnabled==true){
          createNewOderCake(
                dateEdt.text.toString(),
                timeEdt.text.toString(),
                SpinnerVaflaEdt.selectedItem.toString(),
                OnomaPelati.text.toString(),
                Tilefono_Pelati.text.toString(),
                idEdtNumberText.text.toString().toDouble(),
                SpinnerEidos.selectedItem.toString(),
                SpinnerGeusi.selectedItem.toString(),
                SpinnerSxima.selectedItem.toString(),
                txtSxolia.text.toString(),
                 false,
              false

            )

            // }



        }



        return root
    }


    //Συναρτηση για την επιλογη ημερομηνιας
    private fun showDatePicker() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),  // Χρησιμοποιούμε το context του Fragment
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedDate = "$selectedDay-${selectedMonth + 1}-$selectedYear"
                dateEdt.setText(selectedDate)
            },
            year,
            month,
            day
        )
        datePickerDialog.datePicker.minDate = c.timeInMillis
        datePickerDialog.show()
    }


    // Συναρτηση για την επιλογη της ωρας
    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                if (isValidTime(selectedHour, selectedMinute)) {
                    val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                    editText.setText(selectedTime)
                } else {
                    // Εμφάνιση ειδοποίησης ή προτροπής αν η ώρα δεν είναι έγκυρη
                    Toast.makeText(requireContext(), "Επιλέξτε ώρα μεταξύ 05:30 και 21:30", Toast.LENGTH_SHORT).show()
                }
            },
            hour,
            minute,
            true // true for 24-hour format, false for AM/PM
        )

        timePickerDialog.show()
    }


    fun Elenxos_Oloklirosi_Paraggelias() {
        ButtonOloklirosis = binding.btnOrder
        txtSxolia = binding.idEdtSxolia

        if ((flagKilla && (flagMera && flagOra)) && (flagThlefono && flagOnomateponimo)) {
            ButtonOloklirosis.isEnabled = true


        } else {
            ButtonOloklirosis.isEnabled = false
        }


    }


    private fun validatePhoneNumber() {
        val phoneNumber = Tilefono_Pelati.text?.toString()?.trim() ?: ""
        val Tilefono_Pelati_Layout = binding.OnomateponimoPelatiTilefonoLayout
        if (phoneNumber.isEmpty()) {
            return
        }
        if ((phoneNumber.startsWith("2310") || phoneNumber.startsWith("69")) && phoneNumber.length == 10) {
            Tilefono_Pelati_Layout.error = null // Καθαρίζει το error
            Tilefono_Pelati_Layout.isErrorEnabled = false

            flagThlefono = true

        } else {
            Tilefono_Pelati_Layout.isErrorEnabled = true
            Tilefono_Pelati_Layout.error =
                "Το τηλέφωνο πρέπει να ξεκινά με 2310 ή 69 και να έχει 10 ψηφία"
            flagThlefono = false

        }
        Elenxos_Oloklirosi_Paraggelias()
    }


    fun validateKilaTourtas() {
        val rawInput = idEdtNumberText.text?.toString()?.trim() ?: ""

        if (rawInput.startsWith(".")) {
            binding.textInputLayoutKilosTourtas.error = "Μη έγκυρη μορφή (ξεκινά με '.')"
            flagKilla = false
        } else {
            val input = rawInput.toFloatOrNull()
            if (input == null || input > 25f) {
                binding.textInputLayoutKilosTourtas.error =
                    "Ο αριθμός πρέπει να είναι μικρότερος ή ίσος με 25"
                flagKilla = false
            } else {
                binding.textInputLayoutKilosTourtas.error = null
                idEdtNumberText.setText("$input ")
                flagKilla = true
            }
        }

        Elenxos_Oloklirosi_Paraggelias()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    fun createNewOderCake(
        dateEdt: String,
        timeEdt: String,
        SpinnerVaflaEdt: String,
        OnomaPelati: String,
        Tilefono_Pelati: String,
        idEdtNumberText: Double,
        spinnerEidos: String,
        spinnerGeusi: String,
        spinnerSxima: String,
        txtSxolia: String,
        isExpanded: Boolean,
        isCheked: Boolean
    ) {


        val newOrder = CakeData(
            cakeorderid = 0,
            NameAndLastName = OnomaPelati,
            Tilefono = Tilefono_Pelati,
            Sxima = spinnerSxima,
            Eidos = spinnerEidos,
            Geusi = spinnerGeusi,
            Vafla = SpinnerVaflaEdt,
            Day = dateEdt,
            Time = timeEdt,
            Kila = idEdtNumberText,
            Sxolia = txtSxolia,
            isExpanded = isExpanded,
            IsChecked = isCheked
        )




        cakeOrderViewModel.insertCakeOrderBack(newOrder) { insertedId ->
            // Μόλις πάρεις το σωστό id από τη Room
            if (insertedId <= 0) {
                Toast.makeText(requireContext(), "Αποτυχία αποθήκευσης στη βάση δεδομένων", Toast.LENGTH_SHORT).show()
                return@insertCakeOrderBack // Έξοδος από την μέθοδο insertCakeOrderBack
            }



            val finalOrder = newOrder.copy(cakeorderid = insertedId.toInt())
            val ordernumber = finalOrder.cakeorderid.toString()




            db.collection("CakeOrders").document("Shop1").collection("CakeOrders")
                .document(ordernumber)
                .set(finalOrder.toMap())
                .addOnSuccessListener {
                    Snackbar.make(requireView(), "Η παραγγελία καταχωρήθηκε επιτυχώς ✅", Snackbar.LENGTH_SHORT).show()
                        clearFragment()
                        findNavController().popBackStack()

                    //clearFragment()

                }
                .addOnFailureListener {

                    Snackbar.make(requireView(),"Αποτυχία καταχώρησης παραγγελίας ❌",Snackbar.LENGTH_SHORT).setAction("Δοκίμασε ξανά"){}.show()

                }

        }
    }




    fun clearFragment(){





        dateEdt.text.clear()
        timeEdt.text.clear()
        VaflaEdt.text.toString()
        SpinnerVaflaEdt.setSelection(0)
        OnomaPelati.text.clear()
        Tilefono_Pelati.text.clear()
        idEdtNumberText.text.clear()
        SpinnerEidos.setSelection(0)
        SpinnerGeusi.setSelection(0)
        SpinnerSxima.setSelection(0)
        txtSxolia.text.clear()

        flagKilla=false
        flagMera=false
        flagOra=false
        flagThlefono=false
        flagOnomateponimo=false

        Elenxos_Oloklirosi_Paraggelias()

    }



    fun deletAllTable(){
        cakeOrderViewModel.deleteAllCakeOrder()

    }

    private fun isValidTime(hour: Int, minute: Int): Boolean {
        // Επιτρέπεται μόνο ώρα μεταξύ 05:30 και 21:30
        val startHour = 5
        val startMinute = 30
        val endHour = 21
        val endMinute = 30

        val selectedTimeInMinutes = hour * 60 + minute
        val startTimeInMinutes = startHour * 60 + startMinute
        val endTimeInMinutes = endHour * 60 + endMinute

        return selectedTimeInMinutes >= startTimeInMinutes && selectedTimeInMinutes <= endTimeInMinutes
    }


    }





