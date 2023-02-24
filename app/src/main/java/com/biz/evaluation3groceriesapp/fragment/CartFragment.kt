package com.biz.evaluation3groceriesapp.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CartClickListener
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.biz.evaluation3groceriesapp.utils.currentDate
import com.biz.evaluation3groceriesapp.utils.currentTime
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.*

class CartFragment : Fragment(), CartClickListener {

    private var itemAdded: Boolean? = null
    private lateinit var promoText: String
    private lateinit var cartRecyclerView: RecyclerView
    private lateinit var cartProgressBar: ProgressBar
    private lateinit var checkOutButton: CardView
    private lateinit var totalPriceTextView: TextView
    private lateinit var emptyCartTextView: TextView

    private lateinit var grandTotalTextView: TextView
    private lateinit var discountPriceTextView: TextView
    private lateinit var discountTextView: TextView
    private lateinit var promoEditText: EditText
    private lateinit var discountLayout: ConstraintLayout
    private lateinit var bottomSheetProgressBar: ProgressBar

    private lateinit var databaseRefAddToCart: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefOrder: DatabaseReference
    private lateinit var databaseRefPromo: DatabaseReference

    lateinit var skeletonLoading: LottieAnimationView

    val indexList: ArrayList<String> = ArrayList()

    val promoList = mutableMapOf<String, String>()

    var exist: Boolean? = null
    var matched: Boolean? = null

    private var discountValue: String? = null
    private var formattedPrice: String? = null
    private var formattedGrandTotal: String? = null

    var listCart: ArrayList<Cart>? = ArrayList()


    private var adapterCart: CartAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_cart, container, false)

        initVar(view)

        getCartProductIndex()

        onClick()

        setLayout()

        adapterCart()

        return view
    }

    private fun onClick() {
        checkOutButton.setOnClickListener {

            val dialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialogTheme)
            val view = LayoutInflater.from(requireContext()).inflate(R.layout.bottom_sheet_dialog, null)

            val priceTextView = view.findViewById<TextView>(R.id.priceTextView)
            discountTextView = view.findViewById<TextView>(R.id.discountTextView)
            bottomSheetProgressBar = view.findViewById(R.id.bottomSheetProgressBar)
            val placeOrderButton = view.findViewById<CardView>(R.id.placeOrderButton)
            val closeButton = view.findViewById<ImageView>(R.id.closeButton)

            val promoCodeLayout = view.findViewById<ConstraintLayout>(R.id.promoCodeLayout)
            matched = null

            grandTotalTextView = view.findViewById(R.id.grandTotalTextView)
            discountLayout = view.findViewById(R.id.discountLayout)
            discountPriceTextView = view.findViewById(R.id.discountPriceTextView)


            val behavior = dialog.behavior
            dialog.setCancelable(true)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED

            priceTextView.text = "$$formattedPrice"

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

            promoCodeLayout.setOnClickListener {
                val alertDialog = AlertDialog.Builder(requireContext(), R.style.FailedDialogTheme)
                val dialogLayout = layoutInflater.inflate(R.layout.promocode_dialog, null)
                alertDialog.setView(dialogLayout)
                val promoDialog = alertDialog.create()
                promoDialog.setCancelable(true)

                val dialogCloseButton = dialogLayout.findViewById<ImageView>(R.id.dialogCloseButton)
                promoEditText = dialogLayout.findViewById<EditText>(R.id.promoEditText)
                val cancelButton = dialogLayout.findViewById<TextView>(R.id.cancelButton)
                val okButton = dialogLayout.findViewById<CardView>(R.id.okButton)

                dialogCloseButton.setOnClickListener {
                    promoDialog.dismiss()
                }
                cancelButton.setOnClickListener {
                    promoDialog.dismiss()
                }

                okButton.setOnClickListener {
                    if (promoEditText.text.toString() == "") {
                        Toast.makeText(
                            requireContext(),
                            "Please Enter Promo Code!",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        promoDialog.dismiss()
                        promoText = promoEditText.text.toString()
                        getPromo()
                    }

                }


                promoDialog.show()
            }

            placeOrderButton.setOnClickListener {
                if (exist == false) {
                    Toast.makeText(requireContext(), "Your Cart is Empty!", Toast.LENGTH_SHORT)
                        .show()
                } else {
bottomSheetProgressBar.visibility = View.VISIBLE
                    val arrayList1: ArrayList<String> = ArrayList()

                    databaseRefAddToCart.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            val newOrderRef = databaseRefOrder.push()

                            for (data in snapshot.children) {

                                val productId = data.value
                                arrayList1.add(productId.toString())

                                databaseRefProduct.child(productId.toString()).child("ItemCount")
                                    .addListenerForSingleValueEvent(object : ValueEventListener {
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            val count = snapshot.value.toString()
                                            val productRef = newOrderRef.child("Product").child(productId.toString())

                                            productRef.child("Count").setValue(count).addOnSuccessListener {
                                                databaseRefProduct.child(productId.toString()).child("Added")
                                                    .setValue(false)
                                                databaseRefProduct.child(productId.toString()).child("ItemCount")
                                                    .setValue(1)
                                            }
                                            productRef.child("Id").setValue(productId.toString())
                                        }

                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                    })


                            }

                            if (snapshot.childrenCount.toInt() == arrayList1.size) {

                                val currentTime = currentTime()
                                val currentDate = currentDate()

                                databaseRefOrder.child(newOrderRef.key.toString()).child("Time").setValue(currentTime)
                                databaseRefOrder.child(newOrderRef.key.toString()).child("Date").setValue(currentDate)

                                databaseRefOrder.child(newOrderRef.key.toString()).child("Item Total").setValue(formattedPrice)

                                if (discountValue!=null) {
                                    databaseRefOrder.child(newOrderRef.key.toString()).child("Discount Price").setValue(discountValue)
                                    databaseRefOrder.child(newOrderRef.key.toString()).child("Grand Total").setValue(formattedGrandTotal)
                                }

                                databaseRefAddToCart.removeValue().addOnCompleteListener {
                                    bottomSheetProgressBar.visibility = View.GONE
                                    dialog.dismiss()
                                    Toast.makeText(
                                        requireContext(),
                                        "Order Placed Successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    if (getView() != null) {
                                        Navigation.findNavController(requireView())
                                            .navigate(R.id.action_cartFragment_to_orderAcceptFragment)
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
        bottomSheetProgressBar.visibility = View.VISIBLE
        databaseRefPromo.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {
                    val key = data.key.toString()
                    val value = data.value.toString()
                    promoList[key] = value
                }

                for ((key, value) in promoList) {
                    if (promoText == key) {
                        matched = true
                        discountValue = value
                        break
                    } else {
                        matched = false
                    }
                }

                if (matched == true) {
                    discountTextView.text = promoText
                    discountPriceTextView.text = "-$$discountValue"

                    discountLayout.visibility = View.VISIBLE

                    val grandTotal = formattedPrice!!.toDouble() - discountValue!!.toDouble()
                    formattedGrandTotal = String.format("%.2f", grandTotal)
                    grandTotalTextView.text = "$$formattedGrandTotal"

                    bottomSheetProgressBar.visibility = View.GONE

                    Toast.makeText(requireContext(), "You got the $ $discountValue Discount!", Toast.LENGTH_SHORT).show()
                } else if (matched == false) {
                    bottomSheetProgressBar.visibility = View.GONE
                    Toast.makeText(requireContext(), "Please Enter the valid Promo Code!", Toast.LENGTH_SHORT).show()
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

                    } else {
                        getAllData()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }

    private fun setLayout() {
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

    override fun onCloseButtonClicked(data : Cart) {
        skeletonLoading.visibility = View.VISIBLE
        databaseRefAddToCart.child(data.Id).removeValue().addOnSuccessListener {
            databaseRefProduct.child(data.Id).child("Added").setValue(false).addOnSuccessListener {

                Toast.makeText(requireContext().applicationContext, "Removed From Cart to Successfully", Toast.LENGTH_SHORT).show()
                getCartProductIndex()
            }
        }
    }

    override fun onPlusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {
        cartProgressBar.visibility = View.VISIBLE
        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    val updatedCount = (itemCount as Long).toInt() + 1

                    if (updatedCount == 1){
                        holder.minusImageView.setImageResource(R.drawable.minus_button)
                    }else{
                        holder.minusImageView.setImageResource(R.drawable.enabled_minus)
                    }

                    databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                        .addOnSuccessListener {

                            timesPrice(data, holder, updatedCount)

                            holder.countTextView.text = updatedCount.toString()

                            val newPrice  = formattedPrice?.toDouble()?.plus(data.Price.drop(1).toDouble())
                            formattedPrice = String.format("%.2f", newPrice)
                            totalPriceTextView.text = "$ $formattedPrice"
                            cartProgressBar.visibility = View.GONE
                            Toast.makeText(requireContext(), "Item Added Successfully", Toast.LENGTH_SHORT).show()

                        }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

    }

    override fun onMinusButtonClicked(data: Cart, holder: CartAdapter.ViewHolder) {

        databaseRefProduct.child(data.Id).child("ItemCount")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val itemCount = snapshot.value

                    if ((itemCount as Long).toInt() > 1) {
                        cartProgressBar.visibility = View.VISIBLE

                        val updatedCount = (itemCount as Long).toInt() - 1

                        if (updatedCount == 1){
                            holder.minusImageView.setImageResource(R.drawable.minus_button)
                        }else{
                            holder.minusImageView.setImageResource(R.drawable.enabled_minus)
                        }

                        databaseRefProduct.child(data.Id).child("ItemCount").setValue(updatedCount)
                            .addOnSuccessListener {

                                timesPrice(data, holder, updatedCount)

                                holder.countTextView.text = updatedCount.toString()

                                val newPrice  = formattedPrice?.toDouble()?.minus(data.Price.drop(1).toDouble())
                                formattedPrice = String.format("%.2f", newPrice)
                                totalPriceTextView.text = "$ $formattedPrice"
                                cartProgressBar.visibility = View.GONE
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



