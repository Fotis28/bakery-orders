package com.fotis.bakeryorders.ui.home


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.databinding.FragmentHomeBinding
import com.fotis.bakeryorders.ui.gallery.CakeData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import android.Manifest

class HomeFragment : Fragment() {
    private var viewModel: HomeViewModel? = null
   private var currentMonthHere: Int =0
    private var currentYearHere:Int =0
    private var _binding: FragmentHomeBinding? = null
    private lateinit var calendarRecyclerView: androidx.recyclerview.widget.RecyclerView

    var flagNotification: Boolean= false

    //Για ενημερωνεται αυτοματα το firestone
    private var listenerRegistration: ListenerRegistration? = null

    private var selectedShopName : String= ""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)



        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Ενημέρωσε το τοπικό selectedShopName από το ViewModel ΜΕΤΑ το ViewModelProvider
        selectedShopName = viewModel?.getSelectedShopName() ?: "Όλα τα μαγαζιά"


        val calendar = java.util.Calendar.getInstance()
        val currentYear = calendar.get(java.util.Calendar.YEAR)
        val currentMonth = calendar.get(java.util.Calendar.MONTH) // 0 = January
        val daysInMonth = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        calendarRecyclerView = binding.CalendarRecyclerView
        calendarRecyclerView.layoutManager = GridLayoutManager(requireContext(), 7)


         currentMonthHere=currentMonth
         currentYearHere =currentYear

        // Elegxos gia otan allazo framgent
        binding.radioGroupContainer.setOnCheckedChangeListener { group, checkedId ->
            if (viewModel?.gethasInitialLizedRadioGroup() == true) {
                viewModel?.setRadioGroupInitialized(true)
                return@setOnCheckedChangeListener
            }

            val shopName = when (checkedId) {
                R.id.radioButtonAllShops -> "Όλα τα μαγαζιά"
                R.id.radioButtonShop1 -> "Shop1"
                R.id.radioButtonShop2 -> "Shop2"
                R.id.radioButtonShopInstagram -> "Shop3"
                else -> ""
            }

            viewModel?.setSelectedShopName(shopName)
            selectedShopName = viewModel?.getSelectedShopName().toString()
            loadOrdersFromShop(shopName)
        }






      binding.ArrowPreviousMonth.setOnClickListener{

          MonthChangeOnClickLeft()
          /*days.clear()
          days.addAll(generateDaysForMonth(currentYear, currentMonth))
          adapter.notifyDataSetChanged()
          */
      }
      binding.ArrowNextMonth.setOnClickListener{
          MonthChangeOnClickRight()

          /*days.clear()
          days.addAll(generateDaysForMonth(currentYear, currentMonth))
          adapter.notifyDataSetChanged()
          */
      }


        Monthchanger(currentMonthHere,currentYearHere,selectedShopName)


        return root
    }











    override fun onDestroyView() {

        super.onDestroyView()
        _binding = null
    }



    private fun loadOrdersFromShop(shopName: String) {

        Monthchanger(currentMonthHere, currentYearHere, shopName)

    }


    fun generateDaysForMonth(year: Int, month: Int): List<Calendar?> {
        val days = mutableListOf<Calendar?>()

        val calendar = Calendar.getInstance()
        calendar.set(year, month, 1)
        val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) // Κυριακή = 1
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // Προσθήκη κελιών κενών πριν την πρώτη ημέρα του μήνα
        for (i in 1 until firstDayOfWeek) {
            days.add(null)
        }

        // Προσθήκη κανονικών ημερών
        for (day in 1..daysInMonth) {
            val dayCalendar = Calendar.getInstance().apply {
                set(Calendar.YEAR, year)
                set(Calendar.MONTH, month)
                set(Calendar.DAY_OF_MONTH, day)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
                timeInMillis // ← Εδώ το κλειδί!
            }
            days.add(dayCalendar)
        }

        return days
    }

    fun fetchCakesFromFirestore(shopName: String? = null, onResult: (Map<String, List<CakeData>>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val ordersMap = mutableMapOf<String, MutableList<CakeData>>()// Χάρτης: ημερομηνία -> λίστα παραγγελιών
        // Πρώτα ακυρώνουμε τυχόν προηγούμενο listener
        listenerRegistration?.remove()


        if(shopName=="Όλα τα μαγαζιά"|| shopName==null) {

            val totalShops = 3
            var shopsFetched = 0
            var isErrorOccurred = false

            for (i in 1..totalShops) {
                db.collection("CakeOrders").document("Shop$i").collection("CakeOrders").get()
                    .addOnSuccessListener { snapshot ->

                        if (snapshot.isEmpty) {
                            Log.d("Firestore", "Δεν βρέθηκαν δεδομένα για το Shop$i.")

                        } else {
                            Log.d("Firestore", "βρέθηκαν δεδομένα για το Shop${snapshot.size()}.")
                            // Προσθήκη των παραγγελιών
                            for (doc in snapshot) {
                                val dayString =
                                    doc.getString("Day") ?: continue // π.χ. "2025-04-21"


                                val cake = CakeData(
                                    cakeorderid = (doc.getLong("cakeorderid") ?: 0L).toInt(),
                                    NameAndLastName = doc.getString("NameAndLastName") ?: "",
                                    Tilefono = doc.getString("Tilefono") ?: "",
                                    Sxima = doc.getString("Sxima") ?: "",
                                    Eidos = doc.getString("Eidos") ?: "",
                                    Geusi = doc.getString("Geusi") ?: "",
                                    Vafla = doc.getString("Vafla") ?: "",
                                    Day = dayString,
                                    Time = doc.getString("Time") ?: "",
                                    Kila = doc.getDouble("Kila") ?: 0.0,
                                    Sxolia = doc.getString("Sxolia") ?: ""
                                )

                                if (!ordersMap.containsKey(dayString)) {
                                    ordersMap[dayString] = mutableListOf()
                                }
                                ordersMap[dayString]?.add(cake)

                            }
                        }

                        shopsFetched++
                        if (shopsFetched == totalShops && !isErrorOccurred) {
                            onResult(ordersMap)
                        }
                    }
                    .addOnFailureListener {
                        shopsFetched++
                        isErrorOccurred = true
                        if (shopsFetched == totalShops) {
                            onResult(ordersMap) // Κλήση του onResult ακόμη και αν υπάρχει σφάλμα
                        }
                    }


            }


        }
        else
        {/*db.collection("CakeOrders").document(shopName).collection("CakeOrders").get()
            .addOnSuccessListener { snapshot ->
                for (doc in snapshot) {
                    val dayString = doc.getString("Day") ?: continue
*/


                    listenerRegistration = db.collection("CakeOrders")
                        .document(shopName)
                        .collection("CakeOrders")
                        .addSnapshotListener { snapshot, error ->
                            if (error != null || snapshot == null) {
                                Log.e("Firestore", "Snapshot listener error", error)
                                return@addSnapshotListener
                            }
                            ordersMap.clear()
                            for (doc in snapshot) {
                                val dayString = doc.getString("Day") ?: continue
                    val cake = CakeData(
                        cakeorderid = (doc.getLong("cakeorderid") ?: 0L).toInt(),
                        NameAndLastName = doc.getString("NameAndLastName") ?: "",
                        Tilefono = doc.getString("Tilefono") ?: "",
                        Sxima = doc.getString("Sxima") ?: "",
                        Eidos = doc.getString("Eidos") ?: "",
                        Geusi = doc.getString("Geusi") ?: "",
                        Vafla = doc.getString("Vafla") ?: "",
                        Day = dayString,
                        Time = doc.getString("Time") ?: "",
                        Kila = doc.getDouble("Kila") ?: 0.0,
                        Sxolia = doc.getString("Sxolia") ?: "",
                        IsChecked = doc.getBoolean("IsChecked") ?: false
                    )
                    // 	Ελέγχει αν υπάρχει συγκεκριμένο κλειδί στον Map για να βεβαιωθώ ότι υπάρχει η λίστα για εκείνη την ημερομηνία πριν προσθέσω σε αυτή.
                    if (!ordersMap.containsKey(dayString)) {
                        ordersMap[dayString] = mutableListOf()
                    }
                    ordersMap[dayString]?.add(cake)
                }

                onResult(ordersMap)
            }

            }


    }



    fun Monthchanger(currentMonthHere:Int,currentYearHere:Int,shopName: String? = null){
        val textView = binding.MonthTV

        val days = generateDaysForMonth(currentYearHere, currentMonthHere)
        if (currentMonthHere == 0) {
            textView.text="Ιανουάριος "+currentYearHere.toString()
        }
        if (currentMonthHere == 1) {
            textView.text="Φεβρουάριος "+currentYearHere.toString()
        }
        if (currentMonthHere == 2) {
            textView.text="Μάρτιος "+currentYearHere.toString()
        }
        if (currentMonthHere == 3) {
            textView.text="Απρίλιος "+currentYearHere.toString()
        }
        if (currentMonthHere == 4) {
            textView.text="Μάιος "+currentYearHere.toString()
        }
        if (currentMonthHere == 5) {
            textView.text="Ιούνιος "+currentYearHere.toString()

        }
        if (currentMonthHere == 6) {
            textView.text="Ιούλιος "+currentYearHere.toString()
        }
        if (currentMonthHere == 7) {
            textView.text="Αύγουστος "+currentYearHere.toString()
        }
        if (currentMonthHere == 8) {
            textView.text="Σεπτέμβριος "+currentYearHere.toString()
        }
        if (currentMonthHere == 9) {
            textView.text="Οκτώβριος "+currentYearHere.toString()
        }
        if (currentMonthHere == 10) {
            textView.text="Νοέμβριος "+currentYearHere.toString()

        }
        if (currentMonthHere == 11) {
            textView.text="Δεκέμβριος "+currentYearHere.toString()
        }


        fetchCakesFromFirestore(shopName ) { ordersMap ->
            if(viewModel?.getFlagNotification()==false) {
                // Υπολόγισε τις παραγγελίες της σημερινής ημέρας
                val today = SimpleDateFormat(
                    "d-M-yyyy",
                    Locale.getDefault()
                ).format(Calendar.getInstance().time)
                val todaysOrders = ordersMap[today]?.size ?: 0
                viewModel?.setFlagNotification(true)
                sendNotification(todaysOrders) // Κάλεσε το notification
            }
            val adapter = CalendarAdapter(
                days,
                ordersMap,
                onDayClick = { selectedDay, cakeOrders ->
                    // μπορείς να δείξεις ένα popup, να πας σε άλλη σελίδα, κ.λπ.
                    // π.χ. Toast με τα ονόματα
                    if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                        //Εδω αποθηκευτε στο viewmodel το κοινο τα dedemena
                        val dialog = OrderListDialogFragment1()
                        val orderListViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
                        orderListViewModel.setOrders(cakeOrders)
                        orderListViewModel.setSelectedShopName(shopName)
                        dialog.show(parentFragmentManager, "dialog")
                    }

                }
            )
            calendarRecyclerView.adapter = adapter
        }

    }

    fun MonthChangeOnClickLeft(shopName: String? = null){
        val textView = binding.MonthTV
        if (currentMonthHere == 0) {
            currentYearHere--
            currentMonthHere = 11

        }
        else{
            currentMonthHere--
        }
        Monthchanger(currentMonthHere,currentYearHere,selectedShopName)



    }


    fun MonthChangeOnClickRight(){
        val textView = binding.MonthTV
        if (currentMonthHere == 11) {
            currentYearHere++
            currentMonthHere = 0

        }
        else{
            currentMonthHere++
        }
       Monthchanger(currentMonthHere,currentYearHere,selectedShopName)

    }

    fun sendNotification(cakesCount: Int) {
        // Ελέγχει αν έχουν δοθεί τα απαραίτητα δικαιώματα για ειδοποιήσεις
        checkAndRequestNotificationPermission()

        // Παίρνουμε το σύστημα NotificationManager για να μπορούμε να εμφανίσουμε ειδοποιήσεις
        val notificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Δηλώνουμε ένα μοναδικό ID για το κανάλι ειδοποιήσεων (χρησιμοποιείται από Android 8+)
        val channelId = "cake_orders_today"

        // Δημιουργούμε notification channel αν η συσκευή τρέχει Android 8 (API 26) ή νεότερο
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ειδοποιήσεις Τούρτες",
                NotificationManager.IMPORTANCE_DEFAULT // Προτεραιότητα εμφάνισης ειδοποίησης
            )
            notificationManager.createNotificationChannel(channel) // Εγγραφή του καναλιού στο σύστημα (χρειάζεται μόνο μία φορά)
        }
        if(cakesCount==1) {
            val builder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.baseline_cake_24)
                .setContentTitle("Παραγγελίες Σήμερα")
                .setContentText("Έχουμε $cakesCount τούρτα για σήμερα!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)


            notificationManager.notify(1, builder.build())
        }
        else{
            val builder = NotificationCompat.Builder(requireContext(), channelId)
                .setSmallIcon(R.drawable.baseline_cake_24)
                .setContentTitle("Παραγγελίες Σήμερα")
                .setContentText("Έχουμε $cakesCount τούρτες για σήμερα!")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            //Τοποθετώ 1 γιατί όταν θα έχω ξανά την ίδια ανακοίνωση τότε την αντικατιστό
            notificationManager.notify(1, builder.build())
        }

    }


    private fun checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1001 // Arbitrary request code
                )
            }
        }
    }

}