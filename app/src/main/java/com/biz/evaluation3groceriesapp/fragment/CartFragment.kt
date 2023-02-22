package com.biz.evaluation3groceriesapp.fragment

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CartClickListener
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.biz.evaluation3groceriesapp.modelclass.Categories
import com.biz.evaluation3groceriesapp.utils.currentDate
import com.biz.evaluation3groceriesapp.utils.currentTime
import com.google.android.gms.tasks.Task
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*
import org.w3c.dom.Text

class CartFragment : Fragment(), CartClickListener {

    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartProgressBar: ProgressBar
    private lateinit var checkOutButton: CardView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartTextView: TextView

    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefOrder: DatabaseReference
    private lateinit var databaseRefPromo: DatabaseReference

    lateinit var skeletonLoading: LottieAnimationView

    val indexList: ArrayList<String> = ArrayList()
//    val promoList: ArrayList<String> = ArrayList()
    val promoList = mutableMapOf<String, String>()

    var exist : Boolean? = null

    var listCart: ArrayList<Cart>? = ArrayList()

    //    private var count: Int? = null
    private var formattedPrice: String? = null

    private var adapterCart: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        initVar(view)

//        getData()

//        getPromo()

        getCartProductIndex()

        onClick()

        setLayout()

        adapterCart()

        return view
    }

    private fun onClick() {
        checkOutButton.setOnClickListener {
            val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val view =
                LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_dialog, null)

            val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
            val discountTextView = view.findViewById<TextView>(R.id.discountTextView)
            val textView18 = view.findViewById<TextView>(R.id.textView18)
            val placeOrderButton = view.findViewById<CardView>(R.id.placeOrderButton)
            val closeButton = view.findViewById<ImageView>(R.id.closeButton)


            val behavior = dialog.behavior
            dialog.setCancelable(true)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            priceTextView.text = "$ $formattedPrice"

            closeButton.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext(), R.style.FailedDialogTheme)
                val dialogLayout = layoutInflater.inflate(R.layout.failed_dialog, null)
                alertDialog.setView(dialogLayout)
                val failedDialog = alertDialog.create()
                failedDialog.setCancelable(true)

                val dialogCloseButton = dialogLayout.findViewById<ImageView>(R.id.dialogCloseButton)
                val backHomeTextView = dialogLayout.findViewById<TextView>(R.id.backHomeTextView)

                dialogCloseButton.setOnClickListener {
                    failedDialog.dismiss()
                }

                backHomeTextView.setOnClickListener {
                    failedDialog.dismiss()
                    dialog.dismiss()
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_cartFragment_to_shopFragment)
                }

                failedDialog.show()

            }

            textView18.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext(), R.style.FailedDialogTheme)
                val dialogLayout = layoutInflater.inflate(R.layout.promocode_dialog, null)
                alertDialog.setView(dialogLayout)
                val promoDialog = alertDialog.create()
                promoDialog.setCancelable(true)

                val dialogCloseButton = dialogLayout.findViewById<ImageView>(R.id.dialogCloseButton)
                val promoEditText = dialogLayout.findViewById<EditText>(R.id.promoEditText)
                val cancelButton = dialogLayout.findViewById<TextView>(R.id.cancelButton)
                val okButton = dialogLayout.findViewById<CardView>(R.id.okButton)

                dialogCloseButton.setOnClickListener {
                    promoDialog.dismiss()
                }
                cancelButton.setOnClickListener {
                    promoDialog.dismiss()
                }

                okButton.setOnClickListener {
                    promoDialog.dismiss()
                    val promoText = promoEditText.text
                    discountTextView.text = promoText

                    getPromo()
                }


                promoDialog.show()
            }

            placeOrderButton.setOnClickListener {
                if (exist == false) {
                    Toast.makeText(requireContext(), "Your Cart is Empty!", Toast.LENGTH_SHORT).show()
                } else {

                
                val arrayList1 : ArrayList<String> = ArrayList()

                    databaseRefAddToCart.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val newOrderRef = databaseRefOrder.push()

                        for (data in snapshot.children) {

                            val productId = data.value
                            arrayList1.add(productId.toString())

                            databaseRefProduct.child(productId.toString()).child("ItemCount")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val count = snapshot.value.toString()
                                        val productRef =
                                            newOrderRef.child("Product").child(productId.toString())

                                        productRef.child("Count").setValue(count)
                                        productRef.child("Id").setValue(productId.toString())
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }
                                })

                            databaseRefProduct.child(productId.toString()).child("Added")
                                .setValue(false)
                            databaseRefProduct.child(productId.toString()).child("ItemCount")
                                .setValue(1)
                        }

                        if (snapshot.childrenCount.toInt() == arrayList1.size) {

                            val currentTime = currentTime()
                            val currentDate = currentDate()

                            databaseRefOrder.child(newOrderRef.key.toString()).child("Time")
                                .setValue(currentTime)
                            databaseRefOrder.child(newOrderRef.key.toString()).child("Date")
                                .setValue(currentDate)

                            databaseRefAddToCart.removeValue().addOnCompleteListener {
                                dialog.dismiss()
                                Toast.makeText(
                                    requireContext(),
                                    "Order Placed Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                                if (getView() != null) {
                                    Navigation.findNavController(requireView()).navigate(R.id.action_cartFragment_to_orderAcceptFragment)
                                }
                            }

                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })
                }
            }

            dialog.setContentView(view)
            dialog.show()

        }
    }

    private fun getPromo() {
        databaseRefPromo.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children){
                    val key = data.key.toString()
                    val value = data.value.toString()
                    promoList[key] = value
                }

                if (promoList!= null){
                    for ((key, value) in promoList) {
                        println("Key: $key, Value: $value")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })


    }

    private fun getCartProductIndex() {
        skeletonLoading.visibility = View.VISIBLE
        listCart?.clear()
        indexList.clear()
        databaseRefAddToCart.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    if (isAdded) {
                        exist = false
                        getCartProductIndex()
                        adapterCart?.notifyDataSetChanged()
                        formattedPrice = "0.0"
                        totalPriceTextView.text = "$ $formattedPrice"
                        emptyCartTextView.visibility = View.VISIBLE
                        skeletonLoading.visibility = View.GONE
                    }
                } else {
                    for (data in snapshot.children) {
                        val index = data.value.toString()
                        indexList.add(index)
                    }
                    if (indexList.size == snapshot.childrenCount.toInt()) {
                        getAllData()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getAllData() {
        databaseRefProduct.child(indexList[0])
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val cartData = snapshot.getValue(Cart::class.java)
                    listCart?.add(cartData!!)

                    if (indexList.isNotEmpty()) {
                        indexList.removeAt(0)
                    }

                    if (indexList.isEmpty()) {
                        adapterCart?.notifyDataSetChanged()

                        val itemList = adapterCart!!.getItemList()
                        var totalPrice = 0.0
                        for (item in itemList) {
                            totalPrice += item.Price.drop(1).toDouble()
                                .times(item.ItemCount)
                        }
                        formattedPrice = String.format("%.2f", totalPrice)
                        totalPriceTextView.text = "$ $formattedPrice"

                        skeletonLoading.visibility = View.GONE

//                        categoriesProgressBar.visibility = View.GONE
//                        if (isAdded == true){
//                            Toast.makeText(context?.applicationContext, "Added to Cart Successfully", Toast.LENGTH_SHORT).show()
//                        }else if (isAdded == false){
//                            Toast.makeText(context?.applicationContext, "Removed From Cart to Successfully", Toast.LENGTH_SHORT).show()
//                        }
//                        skeletonLoading.visibility = View.GONE
                    } else {
                        getAllData()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun getData() {

        databaseRefAddToCart.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (!snapshot.exists()) {
                    if (isAdded) {
                        emptyCartTextView.visibility = View.VISIBLE
                    }
                } else {
                    cartProgressBar.visibility = View.VISIBLE
                    for (data in snapshot.children) {

                        val user = data.value.toString()

                        val lastElement = snapshot.children.last().value

                        databaseRefProduct.child(user).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {


                                val cartData = snapshot.getValue(Cart::class.java)

                                if (cartData != null) {
                                    listCart?.add(cartData)
                                    cartProgressBar.visibility = View.GONE
                                }

                                if (lastElement == snapshot.key) {
                                    adapterCart?.notifyDataSetChanged()

                                    val itemList = adapterCart!!.getItemList()
                                    var totalPrice = 0.0
                                    for (item in itemList) {
                                        totalPrice += item.Price.drop(1).toDouble()
                                            .times(item.ItemCount)
                                    }
                                    formattedPrice = String.format("%.2f", totalPrice)
                                    totalPriceTextView.text = "$ $formattedPrice"

                                }

                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setLayout() {
//        cartRecyclerView.layoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
        cartRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)
    }

    private fun adapterCart() {
        adapterCart = CartAdapter(listCart!!)
        adapterCart?.setSelectListener(this)
        cartRecyclerView.adapter = adapterCart
    }

    private fun initVar(view: View) {
        cartRecyclerView = view.findViewById(R.id.cartRecyclerView)
        cartProgressBar = view.findViewById(R.id.cartProgressBar)
        checkOutButton = view.findViewById(R.id.checkOutButton)
        totalPriceTextView = view.findViewById(R.id.totalPriceTextView)
        emptyCartTextView = view.findViewById(R.id.emptyCartTextView)

        skeletonLoading = view.findViewById(R.id.skeletonLoading)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefPromo = FirebaseDatabase.getInstance().getReference("PromoCode")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")
        databaseRefOrder = FirebaseDatabase.getInstance().reference.child("Order")

    }

    override fun onCloseButtonClicked(id: String) {
//        cartProgressBar.visibility = View.VISIBLE
        skeletonLoading.visibility = View.VISIBLE
        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {
            databaseRefProduct.child(id).child("Added").setValue(false).addOnSuccessListener {

//            adapterCart?.notifyDataSetChanged()
                Toast.makeText(
                    requireContext().applicationContext,
                    "Removed From Cart to Successfully",
                    Toast.LENGTH_SHORT
                ).show()
                getCartProductIndex()
            }
//            listCart?.clear()
//            getData()
//            cartProgressBar.visibility = View.INVISIBLE
        }
    }

    override fun onPlusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {
        skeletonLoading.visibility = View.VISIBLE
//        cartProgressBar.visibility = View.VISIBLE
        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    if ((itemCount as Long).toInt() == 1) {
                        holder.minusImageView.setImageResource(R.drawable.minus_button)
                    }

                    val updatedCount = (itemCount as Long).toInt() + 1

//                    cartProgressBar.visibility = View.VISIBLE
                    databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                        .addOnSuccessListener {

                            timesPrice(data, holder, updatedCount)

                            holder.countTextView.text = updatedCount.toString()
//                            listCart?.clear()
                            getCartProductIndex()
//                            getData()
//                        adapterCart?.notifyDataSetChanged()
//                        adapterCart()
//                            cartProgressBar.visibility = View.INVISIBLE
//                            skeletonLoading.visibility = View.GONE

                            Toast.makeText(requireContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show()
                        }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onMinusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {

        if (data.ItemCount>1) {
            skeletonLoading.visibility = View.VISIBLE
        }
        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    if ((itemCount as Long).toInt() == 1) {
                        holder.minusImageView.setImageResource(R.drawable.minus_button)
                    }


                    if ((itemCount as Long).toInt() > 1) {
//                        cartProgressBar.visibility = View.VISIBLE

                        val updatedCount = (itemCount as Long).toInt() - 1

//                        cartProgressBar.visibility = View.VISIBLE
                        databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                            .addOnSuccessListener {

                                timesPrice(data, holder, updatedCount)

                                holder.countTextView.text = updatedCount.toString()
//                                listCart?.clear()
//                                getData()
                                getCartProductIndex()
//                        adapterCart?.notifyDataSetChanged()
//                        adapterCart()
//                                cartProgressBar.visibility = View.INVISIBLE
                                Toast.makeText(requireContext(), "Item Removed Successfully", Toast.LENGTH_SHORT).show()
                            }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    private fun timesPrice(data: Cart, holder: CartAdapter.ViewHolder, count: Int) {
        val timesPrice = data.Price.drop(1).toDouble().times(count)
        val formattedPrice = String.format("%.2f", timesPrice)
        holder.priceTextView.text = "$$formattedPrice"
    }
}



