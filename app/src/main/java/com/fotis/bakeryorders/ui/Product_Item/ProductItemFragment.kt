package com.fotis.bakeryorders.ui.Product_Item

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fotis.bakeryorders.R

import com.fotis.bakeryorders.databinding.FragmentProductItemBinding
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagAdapter
import com.fotis.bakeryorders.ui.Product_Item.Data.Bag.BagData
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxAdapter
import com.fotis.bakeryorders.ui.Product_Item.Data.Box.BoxData
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupAdapter
import com.fotis.bakeryorders.ui.Product_Item.Data.Cup.CupData
import com.fotis.bakeryorders.ui.home.AppDatabase
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.Product
import com.fotis.bakeryorders.ui.Product_Item.Data.Products.ProductRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


class ProductItemFragment :Fragment(){


private var _binding: FragmentProductItemBinding? = null

// This property is only valid between onCreateView and
// onDestroyView.
private val binding get() = _binding!!
    //private lateinit var productAdapter: ProductAdapter
    //private var i: Int =1;

    //Δηλωση των adapter για τον καθε table
    private  lateinit var boxAdapter: BoxAdapter
    private lateinit var bagAdapter: BagAdapter
    private lateinit var cupAdapter: CupAdapter


    val db = Firebase.firestore


   // private val viewModel: ProductitemViewModel


    private lateinit var productItemViewModel: ProductItemViewModel
    override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View {

       // Αυτός ειναι ο κώδικας που δημιουργει ενα view model απο την ProductItemViewModelFactory ΚΑΙ  παρνει τα δεδομενα απο το ProductAdapter
        val repository = ProductRepository(
            AppDatabase.getDatabase(requireContext()).productDao(),
            AppDatabase.getDatabase(requireContext()).bagDao(),
            AppDatabase.getDatabase(requireContext()).boxDao(),
            AppDatabase.getDatabase(requireContext()).cupDao() )

        productItemViewModel =
        ViewModelProvider(this,  ProductItemViewModelFactory(repository)).get(ProductItemViewModel::class.java)

        // Molis anoigei to fragment kali thn sirnatish
        // Αν δεν υπάρχουν προϊόντα, δημιουργούμε default
        lifecycleScope.launch {
            val count = productItemViewModel.getcountProducts()
            if (count == 0) {
                insertDefaultData()
            }
        }




        _binding = FragmentProductItemBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bagAdapter = BagAdapter(onDeleteClick ={ bag ->
            productItemViewModel.deleteBag(bag)
            deleteBagFromFirestore(bag.id)
            productItemViewModel.decreaseProductQuantity("BAG")// or however you want to delete
        } ,onQuantityChange = { bag ->
            // Do something when quantity changes, maybe update the bag in ViewModel
            productItemViewModel.updateBag(bag)
        })

        binding.recyclerViewProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts.adapter = bagAdapter

        boxAdapter = BoxAdapter(onDeleteClick ={ box -> productItemViewModel.deleteBox(box)
            deleteBoxFromFirestore(box.id)
              productItemViewModel.decreaseProductQuantity("BOX")}, onQuantityChange = { box -> productItemViewModel.updateBox(box) })
        binding.recyclerViewProducts2.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts2.adapter = boxAdapter

        cupAdapter = CupAdapter(onDeleteClick ={ cup -> productItemViewModel.deleteCup(cup)
          deleteCupFromFirestore(cup.id)
            productItemViewModel.decreaseProductQuantity("CUP")},onQuantityChange = { cup -> productItemViewModel.updateCup(cup) })
        binding.recyclerViewProducts3.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewProducts3.adapter = cupAdapter


        // Παρακολούθηση των LiveData για ενημέρωση adapters
        productItemViewModel.cupItems.observe(viewLifecycleOwner) { cups ->
            cupAdapter.setData(cups) // Update the adapter with the new data
        }

        productItemViewModel.boxItems.observe(viewLifecycleOwner) { boxes ->
            boxAdapter.setData(boxes) // Update the adapter with the new data
        }

        productItemViewModel.bagItems.observe(viewLifecycleOwner) { bags ->
            bagAdapter.setData(bags) // Update the adapter with the new data
        }





        // Λογική για το κουμπί που ανοίγει/κλείνει τη λίστα
        binding.btnExpand.setOnClickListener {
            if (binding.recyclerViewProducts.visibility == View.GONE) {
                binding.recyclerViewProducts.visibility = View.VISIBLE

            } else {
                binding.recyclerViewProducts.visibility = View.GONE
            }
        }

        binding.btnExpand2.setOnClickListener {
            if (binding.recyclerViewProducts2.visibility == View.GONE) {
                binding.recyclerViewProducts2.visibility = View.VISIBLE

            } else {
                binding.recyclerViewProducts2.visibility = View.GONE
            }
        }

        binding.btnExpand3.setOnClickListener {
            if (binding.recyclerViewProducts3.visibility == View.GONE) {
                binding.recyclerViewProducts3.visibility = View.VISIBLE

            } else {
                binding.recyclerViewProducts3.visibility = View.GONE
            }
        }

        binding.ButtonPlusBox.setOnClickListener{
            val dialogView= layoutInflater.inflate(R.layout.dialog_create_for_products,null)
            val etName = dialogView.findViewById<EditText>(R.id.et_box_name)
            val etQuantity = dialogView.findViewById<EditText>(R.id.et_box_quantity)
            AlertDialog.Builder(requireContext())
                .setTitle("Προσθήκη νέου Box 📦")
                .setView(dialogView)
                .setPositiveButton("Αποθήκευση") { _, _ ->
                    val name = etName.text.toString().trim()
                    val quantityText = etQuantity.text.toString().trim()
                    val quantity = quantityText.toIntOrNull() ?: 0

                    if (name.isNotEmpty()) {
                        createNewBox(name, quantity)
                        productItemViewModel.increaseProductQuantity("BOX")
                    } else {
                        Toast.makeText(requireContext(), "Το όνομα δεν μπορεί να είναι κενό", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Άκυρο", null)
                .show()



        }

        binding.ButtonPlusCup.setOnClickListener{
            val dialogView= layoutInflater.inflate(R.layout.dialog_create_for_products,null)
            val etName = dialogView.findViewById<EditText>(R.id.et_box_name)
            val etQuantity = dialogView.findViewById<EditText>(R.id.et_box_quantity)
            AlertDialog.Builder(requireContext())
                .setTitle("Προσθήκη νέου Cup 📦")
                .setView(dialogView)
                .setPositiveButton("Αποθήκευση") { _, _ ->
                    val name = etName.text.toString().trim()
                    val quantityText = etQuantity.text.toString().trim()
                    val quantity = quantityText.toIntOrNull() ?: 0

                    if (name.isNotEmpty()) {
                        createNewCup(name, quantity)
                        productItemViewModel.increaseProductQuantity("CUP")
                    } else {
                        Toast.makeText(requireContext(), "Το όνομα δεν μπορεί να είναι κενό", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Άκυρο", null)
                .show()

        }

        binding.ButtonPlusBag.setOnClickListener{
            val dialogView= layoutInflater.inflate(R.layout.dialog_create_for_products,null)
            val etName = dialogView.findViewById<EditText>(R.id.et_box_name)
            val etQuantity = dialogView.findViewById<EditText>(R.id.et_box_quantity)
            AlertDialog.Builder(requireContext())
                .setTitle("Προσθήκη νέου Προιόντος 📦")
                .setView(dialogView)
                .setPositiveButton("Αποθήκευση") { _, _ ->
                    val name = etName.text.toString().trim()
                    val quantityText = etQuantity.text.toString().trim()
                    val quantity = quantityText.toIntOrNull() ?: 0

                    if (name.isNotEmpty()) {
                        createNewBag(name, quantity)
                        productItemViewModel.increaseProductQuantity("BAG")
                    } else {
                        Toast.makeText(requireContext(), "Το όνομα δεν μπορεί να είναι κενό", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Άκυρο", null)
                .show()


        }

        /*binding.btnExpand2.setOnClickListener {
            if (binding.recyclerViewProducts.visibility == View.GONE) {
                binding.recyclerViewProducts.visibility = View.VISIBLE

            } else {
                binding.recyclerViewProducts.visibility = View.GONE
            }
        }*/


       /* val textView: TextView = binding.textHome
        productItemViewModel.text.observe(viewLifecycleOwner) {
        textView.text = it
      }*/


        binding.btnSave.setOnClickListener{
            giveOneToYellowCups()
            giveOneToYellowBoxs()
            giveOneToYellowBags()
            giveOneToRedBags()
            giveOneToRedBoxs()
            giveOneToRedCups()


        }










/*
                createProduct("BOX",11)
                createProduct("BAG",7)
                createProduct("CUP",3)



        createNewCup("Freddo άσρπο",0)
        createNewCup("Freddo μαύρο",0)
        createNewCup("Φραπέ",0)




        createNewBag("BIO Ψωμιού Μικρή",1)
        createNewBag("BIO Ψωμιού Μεγάλη",0)
        createNewBag("Ζαχαροπλαστική Μεγάλη",3)
        createNewBag("BIO Ζαχαροπλαστική Μικρή",2)
        createNewBag("Πολυτελίας Ζαχαροπλαστική",5)
        createNewBag("Μικρό Πίτας",1)
        createNewBag("Μεγάλο Πίτας",4)


        createNewBox("No.2",1)
        createNewBox("No.4",0)
        createNewBox("No.6",3)
        createNewBox("No.8",2)
        createNewBox("No.10",5)
        createNewBox("No.30",1)
        createNewBox("No.30 Καθρεύτης",4)
        createNewBox("Σ1",4)
        createNewBox("Σ2",4)
        createNewBox("Τ25",4)
        createNewBox("Τ32",4)

*/
  //  seeAllProducts()

       return root
      }

    private fun insertDefaultData() {
        createProduct("BOX",11)
        createProduct("BAG",7)
        createProduct("CUP",3)

        createNewCup("Freddo άσρπο",0)
        createNewCup("Freddo μαύρο",0)
        createNewCup("Φραπέ",0)




        createNewBag("BIO Ψωμιού Μικρή",1)
        createNewBag("BIO Ψωμιού Μεγάλη",0)
        createNewBag("Ζαχαροπλαστική Μεγάλη",3)
        createNewBag("BIO Ζαχαροπλαστική Μικρή",2)
        createNewBag("Πολυτελίας Ζαχαροπλαστική",5)
        createNewBag("Μικρό Πίτας",1)
        createNewBag("Μεγάλο Πίτας",4)


        createNewBox("No.2",1)
        createNewBox("No.4",0)
        createNewBox("No.6",3)
        createNewBox("No.8",2)
        createNewBox("No.10",5)
        createNewBox("No.30",1)
        createNewBox("No.30 Καθρεύτης",4)
        createNewBox("Σ1",4)
        createNewBox("Σ2",4)
        createNewBox("Τ25",4)
        createNewBox("Τ32",4)
    }


    private fun giveOneToRedBags() {

        val redBags = bagAdapter.getRedBags()
      if(redBags.isNotEmpty()){
        for (bag in redBags) {
            val docRef = db.collection("ProductStack")
                .document("BAG")
                .collection("BAGS").document(bag.id.toString())
            docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                        // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedBag = bag.copy(quantity = 1)
                                bagAdapter.updateLocalQuantity(updatedBag)
                            }

                    }
                }.addOnFailureListener { e ->
                }

        }


      }
    }




    private fun giveOneToRedBoxs() {

        val redBoxs = boxAdapter.getRedBoxs()
        if(redBoxs.isNotEmpty()){
            for (box in redBoxs) {
                val docRef = db.collection("ProductStack")
                    .document("BOX")
                    .collection("BOXS").document(box.id.toString())
                docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                            // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedBox = box.copy(quantity = 1)
                               boxAdapter.updateLocalQuantity(updatedBox)
                            }

                    }
                }.addOnFailureListener { e ->
                }

            }


        }

    }


    private fun giveOneToRedCups() {

        val redCups = cupAdapter.getRedCups()
        if(redCups.isNotEmpty()){
            for (cup in redCups) {
                val docRef = db.collection("ProductStack")
                    .document("CUP")
                    .collection("CUPS").document(cup.id.toString())
                docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                            // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedCup = cup.copy(quantity = 1)
                                cupAdapter.updateLocalQuantity(updatedCup)
                            }

                    }
                }.addOnFailureListener { e ->
                }

            }


        }

    }

    private fun giveOneToYellowCups() {

        val YellowCups = cupAdapter.getYellowCups()
        if(YellowCups.isNotEmpty()){
            for (cup in YellowCups) {
                val docRef = db.collection("ProductStack")
                    .document("CUP")
                    .collection("CUPS").document(cup.id.toString())
                docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                            // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedCup = cup.copy(quantity = 2)
                                cupAdapter.updateLocalQuantity(updatedCup)
                            }

                    }
                }.addOnFailureListener { e ->
                }

            }


        }

    }


    private fun giveOneToYellowBags() {

        val YellowBags = bagAdapter.getYellowBags()
        if (YellowBags.isNotEmpty()) {
            for (bag in YellowBags) {
                val docRef = db.collection("ProductStack")
                    .document("BAG")
                    .collection("BAGS").document(bag.id.toString())
                docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                            // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedBag = bag.copy(quantity = 2)
                                bagAdapter.updateLocalQuantity(updatedBag)
                            }

                    }
                }.addOnFailureListener { e ->
                }

            }


        }
    }

    private fun giveOneToYellowBoxs() {

        val YellowBoxs = boxAdapter.getYellowBoxs()
        if(YellowBoxs.isNotEmpty()){
            for (box in YellowBoxs) {
                val docRef = db.collection("ProductStack")
                    .document("BOX")
                    .collection("BOXS").document(box.id.toString())
                docRef.get().addOnSuccessListener { document ->
                    val externalStock = document.getLong("quantity")?.toInt() ?: 0
                    if (externalStock > 0) {
                        // 1. Update Firestore: μειώνουμε το stock
                        docRef.update("quantity", externalStock - 1)

                            // 2. Update local Bag: αυξάνουμε quantity
                            .addOnSuccessListener {
                                val updatedBox = box.copy(quantity = 2)
                                boxAdapter.updateLocalQuantity(updatedBox)
                            }

                    }
                }.addOnFailureListener { e ->
                }

            }


        }

    }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }


    fun createBag(name:String ,quantity:Int){
        val newBag = BagData(productName="BAG",name=name,quantity =quantity)
        productItemViewModel.insertBag(newBag)
    }



    fun createCup(name:String ,quantity:Int){

        val newCup = CupData(productName="CUP",name=name,quantity =quantity)


        productItemViewModel.insertCup(newCup)
    }

    fun createProduct(name:String ,quantity:Int){
        val newProduct = Product(name,quantity)
        productItemViewModel.createProduct(newProduct)
    }

    fun deleteProduct(product: Product){
        productItemViewModel.deleteProduct(product)
    }
    fun updateProduct(product: Product){
        productItemViewModel.updateProduct(product)
    }


   // Δημιουργια του Box  μονο μεσα στο room για προγραμματισμό και ελεγχω
    fun createBox(name:String ,quantity:Int){

    val newBox = BoxData(productName="BOX",name=name,quantity =quantity)

     productItemViewModel.insertBox(newBox)
}






        // Καινουργιο box  και αποθηκευεται μεσα στην εξωτερικη βαση
        fun createNewBox(

            name:String,
            quantity : Int  ,

        ) {


            val newBox = BoxData(
                id =0,
                productName = "BOX",
                name = name,
                quantity = quantity,


            )

            productItemViewModel.insertBoxWithProductAndReturn(newBox){ insertedId ->
                // Μόλις πάρεις το σωστό id από τη Room
                if (insertedId <= 0) {
                    Toast.makeText(requireContext(), "Αποτυχία αποθήκευσης στη βάση δεδομένων", Toast.LENGTH_SHORT).show()
                    return@insertBoxWithProductAndReturn
                }



                val finalOrder = newBox.copy(id = insertedId.toInt())
                val ordernumber = finalOrder.id.toString()


                newBox.quantity=100
                db.collection("ProductStack").document("BOX").collection("BOXS")
                    .document(ordernumber)
                    .set(newBox.toMapBox()) // Use your toMap extension or function
                    .addOnSuccessListener {
                        Snackbar.make(requireView(), "Το προιόν καταχωρήθηκε επιτυχώς ✅", Snackbar.LENGTH_SHORT).show()


                    }
                    .addOnFailureListener {

                        Snackbar.make(requireView(),"Αποτυχία καταχώρησης προιόντος ❌", Snackbar.LENGTH_SHORT).setAction("Δοκίμασε ξανά"){}.show()

                    }

            }
        }

    private fun deleteBoxFromFirestore(id: Int) {
        val docId = id.toString()
        db.collection("ProductStack")
            .document("BOX")
            .collection("BOXS")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                Snackbar.make(requireView(), "Διαγράφηκε από Firestore ✅", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), "Αποτυχία διαγραφής από Firestore ❌", Snackbar.LENGTH_SHORT).show()
            }
    }


    fun createNewCup(

        name:String,
        quantity : Int  ,

        ) {


        val newCup = CupData(
            id =0,
            productName = "CUP",
            name = name,
            quantity = quantity,


            )

        productItemViewModel.insertCupWithProductAndReturn(newCup){ insertedId ->
            // Μόλις πάρεις το σωστό id από τη Room
            if (insertedId <= 0) {
                Toast.makeText(requireContext(), "Αποτυχία αποθήκευσης στη βάση δεδομένων", Toast.LENGTH_SHORT).show()
                return@insertCupWithProductAndReturn
            }



            val finalOrder = newCup.copy(id = insertedId.toInt())
            val ordernumber = finalOrder.id.toString()


            newCup.quantity=100
            db.collection("ProductStack").document("CUP").collection("CUPS")
                .document(ordernumber)
                .set(newCup.toMapCup()) // Use your toMap extension or function
                .addOnSuccessListener {
                    Snackbar.make(requireView(), "Το προιόν καταχωρήθηκε επιτυχώς ✅", Snackbar.LENGTH_SHORT).show()


                }
                .addOnFailureListener {

                    Snackbar.make(requireView(),"Αποτυχία καταχώρησης προιόντος ❌", Snackbar.LENGTH_SHORT).setAction("Δοκίμασε ξανά"){}.show()

                }

        }
    }

    private fun deleteCupFromFirestore(id: Int) {
        val docId = id.toString()
        db.collection("ProductStack")
            .document("CUP")
            .collection("CUPS")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                Snackbar.make(requireView(), "Διαγράφηκε από Firestore ✅", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), "Αποτυχία διαγραφής από Firestore ❌", Snackbar.LENGTH_SHORT).show()
            }
    }

    fun createNewBag(name:String, quantity : Int  ){
        val newBag = BagData(
            id =0,
            productName = "BAG",
            name = name,
            quantity = quantity,)

        productItemViewModel.insertBagWithProductAndReturn(newBag){ insertedId ->
            // Μόλις πάρεις το σωστό id από τη Room
            if (insertedId <= 0) {
                Toast.makeText(requireContext(), "Αποτυχία αποθήκευσης στη βάση δεδομένων", Toast.LENGTH_SHORT).show()
                return@insertBagWithProductAndReturn
            }



            val finalOrder = newBag.copy(id = insertedId.toInt())
            val ordernumber = finalOrder.id.toString()


            newBag.quantity=100
            db.collection("ProductStack").document("BAG").collection("BAGS")
                .document(ordernumber)
                .set(newBag.toMapBag()) // Use your toMap extension or function
                .addOnSuccessListener {
                    Snackbar.make(requireView(), "Το προιόν καταχωρήθηκε επιτυχώς ✅", Snackbar.LENGTH_SHORT).show()


                }
                .addOnFailureListener {

                    Snackbar.make(requireView(),"Αποτυχία καταχώρησης προιόντος ❌", Snackbar.LENGTH_SHORT).setAction("Δοκίμασε ξανά"){}.show()

                }

        }



    }

    private fun deleteBagFromFirestore(id: Int) {
        val docId = id.toString()
        db.collection("ProductStack")
            .document("BAG")
            .collection("BAGS")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                Snackbar.make(requireView(), "Διαγράφηκε από Firestore ✅", Snackbar.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Snackbar.make(requireView(), "Αποτυχία διαγραφής από Firestore ❌", Snackbar.LENGTH_SHORT).show()
            }
    }

}

