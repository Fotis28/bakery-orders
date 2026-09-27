package com.fotis.bakeryorders.ui.home

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.fotis.bakeryorders.R
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class CakeOrderWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    override fun doWork(): Result {
        checkOrdersAndNotify()
        


        return Result.success()
    }

    private fun checkOrdersAndNotify() {
        val db = FirebaseFirestore.getInstance()
        val todayFormat = SimpleDateFormat("d-M-yyyy", Locale.getDefault())
        val todayKey = todayFormat.format(Calendar.getInstance().time)



        db.collection("CakeOrders").document("Shop1").collection("CakeOrders")
            .whereEqualTo("Day", todayKey)
            .get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.isEmpty) {
                    sendNotification(snapshot.size())
                }
            }
            .addOnFailureListener {
            }
    }

    private fun sendNotification(cakesCount: Int) {
        val channelId = "cake_orders_today"
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Ειδοποιήσεις Τούρτες",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.baseline_cake_24)
            .setContentTitle("Παραγγελίες Σήμερα")
            .setContentText("Έχουμε $cakesCount τούρτες για σήμερα!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        notificationManager.notify(2, builder.build())
    }
}
