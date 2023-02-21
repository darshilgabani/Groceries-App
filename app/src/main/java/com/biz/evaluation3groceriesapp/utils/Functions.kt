package com.biz.evaluation3groceriesapp.utils

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.fragment.ShopFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

lateinit var databaseRefProduct : DatabaseReference
private lateinit var databaseRefAddToCart: DatabaseReference

private var cartAddValue: Any? = null

fun addToCart(id: String, addButtonImage: ImageView,context:Context,pBLoading : ProgressBar) {
    databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
    databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

    databaseRefProduct.child(id).child("Added")
        .addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cartAddValue = snapshot.value
                if (cartAddValue == false) {
                    databaseRefAddToCart.child(id).setValue(id)
                        .addOnSuccessListener {
                            addButtonImage.setImageResource(R.drawable.tickmark_icon)
                            databaseRefProduct.child(id).child("Added").setValue(true)
                            pBLoading.visibility = View.INVISIBLE
                            Toast.makeText(context.applicationContext, "Added to Cart Successfully", Toast.LENGTH_SHORT).show()
                        }
                } else if (cartAddValue == true) {
                    databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
                        addButtonImage.setImageResource(R.drawable.plus_image)
                        databaseRefProduct.child(id).child("Added").setValue(false)
                        pBLoading.visibility = View.INVISIBLE
                        Toast.makeText(
                            context.applicationContext,
                            "Removed From Cart to Successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

}

fun currentTime() : String{
    val currentDateTime = Calendar.getInstance().time
    val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
    val formattedTime = timeFormat.format(currentDateTime)

    return formattedTime
}
fun currentDate() : String{
    val currentDateTime = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedDate = dateFormat.format(currentDateTime)

    return formattedDate
}
