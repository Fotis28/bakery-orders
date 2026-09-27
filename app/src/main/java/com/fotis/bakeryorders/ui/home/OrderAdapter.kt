package com.fotis.bakeryorders.ui.home

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.text.InputFilter
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.fotis.bakeryorders.R
import com.fotis.bakeryorders.ui.gallery.CakeData
import java.util.Calendar

class OrderAdapter(
    private val orders: List<CakeData>,
    private val shop:String
) : RecyclerView.Adapter<OrderAdapter.OrderViewHolder>() {


    private val expandedStates = MutableList(orders.size) { false }
    private var lastExpandedPosition: Int = -1


    private val mutableOrders = orders.toMutableList()

    /** All Room and Firestore writes go through here; the adapter only renders. */
    private val repository = OrderRepository(shop)


    inner class OrderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnExpand: ImageButton? = itemView.findViewById(R.id.btnExpand)
        val btnChecked: ImageButton? = itemView.findViewById(R.id.ButtonCkeckedOrder)
        val deleteButton: ImageButton? = itemView.findViewById(R.id.ButtonDeleteOrder)
        val nameText: TextView? = itemView.findViewById(R.id.NameAndLastNameOfTheClient)
        val AlllayoutOrder: ViewGroup? = itemView.findViewById(R.id.AlllayoutOrder)

        val productsLayout: ViewGroup? = itemView.findViewById(R.id.materialView)


        val productName: TextView? = itemView.findViewById(R.id.tv_product_name)
        val productTilefono: TextView? = itemView.findViewById(R.id.tv_product_Tilefono)
        val productEidos: TextView? = itemView.findViewById(R.id.tv_product_Eidos)
        val productDay: TextView? = itemView.findViewById(R.id.tv_product_Day)
        val productGeusi: TextView? = itemView.findViewById(R.id.tv_product_Geusi)
        val productSxima: TextView? = itemView.findViewById(R.id.tv_product_Sxima)
        val productSxolia: TextView? = itemView.findViewById(R.id.tv_product_Sxolia)
        val productTime: TextView? = itemView.findViewById(R.id.tv_product_Time)
        val productVafla: TextView?= itemView.findViewById(R.id.tv_product_Vafla)

        val btnEdit1: ImageButton? = itemView.findViewById(R.id.btn_edit1)
        val btnEdit2: ImageButton? = itemView.findViewById(R.id.btn_Edit2)
        val btnEdit3: ImageButton? = itemView.findViewById(R.id.btn_Edit3)
        val btnEdit4: ImageButton? = itemView.findViewById(R.id.btn_Edit4)
        val btnEdit5: ImageButton? = itemView.findViewById(R.id.btn_Edit5)
        val btnEdit6: ImageButton? = itemView.findViewById(R.id.btn_Edit6)
        val btnEdit7: ImageButton? = itemView.findViewById(R.id.btn_Edit7)
        val btnEdit8: ImageButton? = itemView.findViewById(R.id.btn_Edit8)
        val btnEdit9: ImageButton? = itemView.findViewById(R.id.btn_Edit9)

        val btnShowProducts: LinearLayout? = itemView.findViewById(R.id.btnShowProducts)
        val textInputLayout: ViewGroup? = itemView.findViewById(R.id.textInputLayout)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate( R.layout.insite_dialog_orders_recyclerview2, parent, false)


        return OrderViewHolder(view)
    }


    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = mutableOrders[position]

        val isExpanded = expandedStates[position]

        holder.nameText?.text = "Παραγγελία #$position"


        //holder.productsLayout?.visibility = if (isExpanded) View.VISIBLE else View.GONE


        if (shop != "Shop1"){
            holder.btnChecked?.visibility=View.GONE
            holder.deleteButton?.visibility=View.GONE
        }


        if((shop=="Shop1") && (order.IsChecked==true))
        {
            holder.productsLayout?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.status_checked
                )
            )
            holder.btnEdit1?.visibility = View.GONE
            holder.btnEdit2?.visibility = View.GONE
            holder.btnEdit3?.visibility = View.GONE
            holder.btnEdit4?.visibility = View.GONE
            holder.btnEdit5?.visibility = View.GONE
            holder.btnEdit6?.visibility = View.GONE
            holder.btnEdit7?.visibility = View.GONE
            holder.btnEdit8?.visibility = View.GONE
            holder.btnEdit9?.visibility = View.GONE
            holder.deleteButton?.visibility = View.GONE
            holder.btnChecked?.visibility = View.GONE
        }
        else{
            holder.productsLayout?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.status_unchecked
                )
            )
        }


        val context = holder.itemView.context
        val orderId = order.cakeorderid

        if (order.isExpanded) {


            holder.productsLayout?.visibility = View.VISIBLE


            holder.productName?.text = order.NameAndLastName
            holder.productTilefono?.text = order.Tilefono
            holder.productEidos?.text = order.Eidos
            holder.productDay?.text = order.Day
            holder.productGeusi?.text = order.Geusi
            holder.productSxima?.text = order.Sxima
            if (order.Sxolia != null) {
                holder.productSxolia?.text = "Σχόλια: " + order.Sxolia
            }

            holder.productTime?.text = order.Time
            holder.productVafla?.text = order.Vafla


            if (shop != "Shop1") {
                holder.btnEdit1?.visibility = View.GONE
                holder.btnEdit2?.visibility = View.GONE
                holder.btnEdit3?.visibility = View.GONE
                holder.btnEdit4?.visibility = View.GONE
                holder.btnEdit5?.visibility = View.GONE
                holder.btnEdit6?.visibility = View.GONE
                holder.btnEdit7?.visibility = View.GONE
                holder.btnEdit8?.visibility = View.GONE
                holder.btnEdit9?.visibility = View.GONE
                holder.btnChecked?.visibility = View.GONE
                holder.deleteButton?.visibility = View.GONE

                holder.productsLayout?.background = null
            }


            holder.btnEdit1?.setOnClickListener {
                val context = holder.itemView.context
                val input = EditText(context).apply {
                    hint = "Νέο Όνομα"
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    //keyListener = DigitsKeyListener.getInstance(
                    //     "ΆΈΉΊΌΎΏΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫΪΫάέήίόύώϊϋΐΰαβγδεζηθικλμνξοπρσςτυφχψωABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "
                    //)

                    val allowedChars =
                        "ΆΈΉΊΌΎΏΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫΪΫάέήίόύώϊϋΐΰαβγδεζηθικλμνξοπρσςτυφχψωABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "

                    filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                        val filtered = source.filter { allowedChars.contains(it) }
                        if (filtered.length == source.length) source else filtered
                    })

                }

                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Ονόματος")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val newName = input.text.toString()

                        if (newName.isNotBlank()) {
                            holder.productName?.text = newName
                            repository.updateField(
                                context = context,
                                order = order,
                                field = "NameAndLastName",
                                value = newName,
                                updatedOrder = order.copy(NameAndLastName = newName)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το όνομα δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }

            holder.btnEdit2?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context
                val input = EditText(context).apply {
                    hint = "Νέο Τηλέφωνο"
                    inputType = InputType.TYPE_CLASS_PHONE
                    filters = arrayOf(
                        InputFilter.LengthFilter(10), // Μέγιστο 10 ψηφία
                        InputFilter { source, _, _, _, _, _ ->
                            if (source.matches(Regex("[0-9]+"))) source else ""
                        }
                    )
                }


                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Τηλέφωνου")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val newPhone = input.text.toString()
                        val isValidPhone = newPhone.matches(Regex("^(69\\d{8}|2310\\d{6})\$"))
                        if (isValidPhone) {
                            // Ενημέρωση του TextView με το νέο Τηλέφωνο
                            holder.productTilefono?.text = newPhone

                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Tilefono",
                                value = newPhone,
                                updatedOrder = order.copy(Tilefono = newPhone)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το τηλέφωνο δεν μπορεί να είναι κενό ή να μην ξεκιναει με 2310 ή 69",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }

            holder.btnEdit3?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()


                val context = holder.itemView.context
                val input = Spinner(context)


                val flavors = arrayListOf("Απλή τούρτα", "Ζαχαρόπαστα", "Βουτηροκρεμα")
                val adapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, flavors)
                input.adapter = adapter


                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Είδους")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val selectedFlavor = input.selectedItem.toString()

                        if (selectedFlavor.isNotBlank()) {
                            // Ενημέρωση του TextView με το νέο Είδος
                            holder.productEidos?.text = selectedFlavor

                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Eidos",
                                value = selectedFlavor,
                                updatedOrder = order.copy(Eidos = selectedFlavor)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το είδος δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }


            holder.btnEdit4?.setOnClickListener {
                val context = holder.itemView.context
                val calendar = java.util.Calendar.getInstance()

                val datePicker = android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        val selectedDate = "$dayOfMonth-${month + 1}-$year"
                        holder.productDay?.text = selectedDate

                        repository.updateField(
                            context = context,
                            order = order,
                            field = "Day",
                            value = selectedDate,
                            updatedOrder = order.copy(Day = selectedDate)
                        ) { success ->
                            val message = if (success) "Order updated" else "Failed to update Firestore"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }
                    },
                    calendar.get(java.util.Calendar.YEAR),
                    calendar.get(java.util.Calendar.MONTH),
                    calendar.get(java.util.Calendar.DAY_OF_MONTH)
                )
                datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
                datePicker.show()
            }
            holder.btnEdit5?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context
                val input = Spinner(context)


                val flavors =
                    context.resources.getStringArray(R.array.String_Epilogi_Geusis_Tourtas)
                val adapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, flavors)
                input.adapter = adapter


                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Είδους")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val selectedFlavor = input.selectedItem.toString()

                        if (selectedFlavor.isNotBlank()) {
                            // Ενημέρωση του TextView με το νέο Είδος
                            holder.productGeusi?.text = selectedFlavor

                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Geusi",
                                value = selectedFlavor,
                                updatedOrder = order.copy(Geusi = selectedFlavor)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το είδος δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }

            holder.btnEdit6?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context
                val input = Spinner(context)


                val flavors = context.resources.getStringArray(R.array.String_tourtas)
                val adapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, flavors)
                input.adapter = adapter


                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Είδους")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val selectedFlavor = input.selectedItem.toString()

                        if (selectedFlavor.isNotBlank()) {
                            // Ενημέρωση του TextView με το νέο Είδος
                            holder.productSxima?.text = selectedFlavor

                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Sxima",
                                value = selectedFlavor,
                                updatedOrder = order.copy(Sxima = selectedFlavor)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το είδος δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }

            holder.btnEdit7?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context
                val input = EditText(context).apply {
                    hint = "Προσθεσετε Σχόλια"
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
                    setText(order.Sxolia) // Προσθετει παλια σχολια για αλλαγη
                    setSelection(text.length)


                    val allowedChars =
                        "ΆΈΉΊΌΎΏΑΒΓΔΕΖΗΘΙΚΛΜΝΞΟΠΡΣΤΥΦΧΨΩΪΫΪΫάέήίόύώϊϋΐΰαβγδεζηθικλμνξοπρσςτυφχψωABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz "

                    filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
                        val filtered = source.filter { allowedChars.contains(it) }
                        if (filtered.length == source.length) source else filtered
                    })

                }

                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Σχολια")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val newName = input.text.toString()

                        if (newName.isNotBlank()) {
                            holder.productSxolia?.text = newName
                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Sxolia",
                                value = newName,
                                updatedOrder = order.copy(Sxolia = newName)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το όνομα δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }
            holder.btnEdit8?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context

                // Παίρνουμε την τρέχουσα ώρα ως default
                val calendar = Calendar.getInstance()
                val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                val currentMinute = calendar.get(Calendar.MINUTE)

                TimePickerDialog(context, { _, hourOfDay, minute ->
                    // Format ώρας π.χ. 14:05
                    if (isValidTime(hourOfDay, minute)) {
                        val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                        holder.productTime?.text = formattedTime

                        repository.updateField(
                            context = context,
                            order = order,
                            field = "Time",
                            value = formattedTime,
                            updatedOrder = order.copy(Time = formattedTime)
                        ) { success ->
                            val message = if (success) "Order updated" else "Failed to update Firestore"
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                        }

                    }
                    else{
                        Toast.makeText(context, "Επιλέξτε ώρα μεταξύ 05:30 και 21:30", Toast.LENGTH_SHORT).show()
                    }
                }, currentHour, currentMinute, true).show() // true: 24ωρη μορφή
            }
            holder.btnEdit9?.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Edit order: ${order.cakeorderid}",
                    Toast.LENGTH_SHORT
                ).show()
                val context = holder.itemView.context
                val input = Spinner(context)


                val Vafla = context.resources.getStringArray(R.array.String_Apofasi_Nai_Oxi)
                val adapter =
                    ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, Vafla)
                input.adapter = adapter


                AlertDialog.Builder(context)
                    .setTitle("Αλλαγή Είδους")
                    .setView(input)
                    .setPositiveButton("Αποθήκευση") { _, _ ->
                        val selectedVafla = input.selectedItem.toString()

                        if (selectedVafla.isNotBlank()) {
                            holder.productVafla?.text = selectedVafla

                            repository.updateField(
                                context = context,
                                order = order,
                                field = "Vafla",
                                value = selectedVafla,
                                updatedOrder = order.copy(Vafla = selectedVafla)
                            ) { success ->
                                val message = if (success) "Order updated" else "Failed to update Firestore"
                                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(
                                context,
                                "Το είδος δεν μπορεί να είναι κενό",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .setNegativeButton("Ακύρωση", null)
                    .show()
            }


        } else {
            holder.productsLayout?.visibility = View.GONE
            //holder.AlllayoutOrder?.setPadding(0,0,0,0)
            // (holder.AlllayoutOrder?.layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, 0)

        }


        holder.btnExpand?.setOnClickListener {
            order.isExpanded = !order.isExpanded
            notifyItemChanged(position)

            // Ενημερώνουμε το τελευταίο expanded στοιχείο

            notifyItemChanged(position,"expand_state_changed") // Ενημέρωση της τρέχουσας θέσης
        }


        holder.deleteButton?.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Delete order: ${order.cakeorderid}", Toast.LENGTH_SHORT).show()

            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setMessage("Θέλετε σίγουρα να διαγράψετε αυτή την παραγγελία?")
                .setCancelable(false) // Το κουμπί πίσω δεν θα κλείσει το παράθυρο
                .setPositiveButton("Yes") { dialog, id ->
                    repository.deleteOrder(context, order) { success ->
                        if (success) {
                            mutableOrders.removeAt(position)
                            expandedStates.removeAt(position)
                            notifyItemRemoved(position)

                            if (lastExpandedPosition == position) {
                                lastExpandedPosition = -1
                            } else if (lastExpandedPosition > position) {
                                lastExpandedPosition -= 1
                            }

                            Toast.makeText(context, "Order deleted", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Error deleting order", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .setNegativeButton("No") { dialog, id ->
                    // Αν ο χρήστης πατήσει "No", κλείνει το παράθυρο
                    dialog.dismiss()
                }

            // Εμφάνιση του AlertDialog
            val alert = builder.create()
            alert.show()

            // Add deletion logic here (Room & Firestore if needed)
        }

        holder.btnChecked?.setOnClickListener {
            holder.productsLayout?.setBackgroundColor(
                ContextCompat.getColor(
                    holder.itemView.context,
                    R.color.light_green
                )
            )
            holder.btnEdit1?.visibility = View.GONE
            holder.btnEdit2?.visibility = View.GONE
            holder.btnEdit3?.visibility = View.GONE
            holder.btnEdit4?.visibility = View.GONE
            holder.btnEdit5?.visibility = View.GONE
            holder.btnEdit6?.visibility = View.GONE
            holder.btnEdit7?.visibility = View.GONE
            holder.btnEdit8?.visibility = View.GONE
            holder.btnEdit9?.visibility = View.GONE
            holder.deleteButton?.visibility = View.GONE
            holder.btnChecked?.visibility = View.GONE
            repository.markAsChecked(context, order) { success ->
                if (!success) {
                    Toast.makeText(context, "Error updating order", Toast.LENGTH_SHORT).show()
                }
            }

            order.IsChecked = true // άλλαξε το αντικείμενο στην τρέχουσα λίστα
            notifyItemChanged(holder.adapterPosition) // refresh το view

        }


    }


    override fun getItemCount() = mutableOrders.size


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