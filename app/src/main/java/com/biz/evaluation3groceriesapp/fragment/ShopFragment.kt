package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.BestSellingAdapter
import com.biz.evaluation3groceriesapp.adapter.ExclusiveOfferAdapter
import com.biz.evaluation3groceriesapp.clicklistener.SelectListener
import com.biz.evaluation3groceriesapp.modelclass.BestSelling
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.google.firebase.database.*

class ShopFragment : Fragment(),SelectListener {

    lateinit var recyclerViewExclOffer : RecyclerView
    lateinit var recyclerViewBestSelling : RecyclerView

    private lateinit var databaseRefExclOffer: DatabaseReference
    private lateinit var databaseRefBestSelling: DatabaseReference

     var adapterBest :BestSellingAdapter? = null
     var adapterExcl :ExclusiveOfferAdapter? = null

    var listExclOffer : ArrayList<ExclusiveOffer>? = ArrayList()
    var listBestSelling : ArrayList<BestSelling>? = ArrayList()

    lateinit var gridLayoutManager : GridLayoutManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_shop, container, false)

        initVar(view)

        getData()

        setLayout()

        return view
    }

    private fun setLayout() {
        gridLayoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
        recyclerViewExclOffer.layoutManager = gridLayoutManager

        recyclerViewBestSelling.layoutManager = GridLayoutManager(context,1, LinearLayoutManager.HORIZONTAL,false)
    }

    private fun adapterExcl() {
        adapterExcl = ExclusiveOfferAdapter(listExclOffer!!)
        adapterExcl?.setSelectListener(this)
        recyclerViewExclOffer.adapter = adapterExcl
    }
    private fun adapterBest() {
        adapterBest = BestSellingAdapter(listBestSelling!!)
        adapterBest?.setSelectListener(this)
        recyclerViewBestSelling.adapter = adapterBest
    }

    private fun getData() {
        listExclOffer?.clear()
        databaseRefExclOffer.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for(data in snapshot.children){
                    val user = data.getValue(ExclusiveOffer::class.java)
                    listExclOffer?.add(user!!)
                }
                adapterExcl()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

        listBestSelling?.clear()
        databaseRefBestSelling.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children){
                    val user = data.getValue(BestSelling::class.java)
                    listBestSelling?.add(user!!)
                }
                adapterBest()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initVar(view: View) {
        recyclerViewExclOffer = view.findViewById(R.id.recyclerViewExclOffer)
        recyclerViewBestSelling = view.findViewById(R.id.recyclerViewBestSelling)

        databaseRefExclOffer = FirebaseDatabase.getInstance().getReference("ExclusiveOffer")
        databaseRefBestSelling = FirebaseDatabase.getInstance().getReference("BestSelling")

    }

    override fun onExclClicked(model: ExclusiveOffer) {
        Toast.makeText(context, model.Name, Toast.LENGTH_SHORT).show()
    }

    override fun onBestClicked(model: BestSelling) {
        Toast.makeText(context, model.Name, Toast.LENGTH_SHORT).show()
    }

    override fun onAddToCartBestClicked(model: BestSelling, addButtonImage: ImageView) {
        addButtonImage.setImageResource(R.drawable.tickmark_icon)
    }

    override fun onAddToCartExclClicked(model: ExclusiveOffer, addButtonImage: ImageView) {
        addButtonImage.setImageResource(R.drawable.tickmark_icon)
    }
}