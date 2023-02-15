package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CartAdapter
import com.biz.evaluation3groceriesapp.adapter.FavouriteAdapter
import com.biz.evaluation3groceriesapp.clicklistener.FavClickListener
import com.biz.evaluation3groceriesapp.modelclass.Cart
import com.biz.evaluation3groceriesapp.modelclass.Favourite
import com.google.firebase.database.*

class FavouriteFragment : Fragment(),FavClickListener {
    lateinit var favRecyclerView : RecyclerView
    lateinit var favProgressBar : ProgressBar
    lateinit var emptyFavTextView : TextView

    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefFavourite: DatabaseReference

    var listFav: ArrayList<Favourite>? = ArrayList()

    private var adapterFav: FavouriteAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favourite, container, false)

        initVar(view)

        getData()

        setLayout()

        adapterFav()

        return view
    }

    private fun setLayout() {
        favRecyclerView.layoutManager = LinearLayoutManager(context?.applicationContext)
    }

    private fun adapterFav() {
        adapterFav = FavouriteAdapter(listFav!!)
        adapterFav?.setSelectListener(this)
        favRecyclerView.adapter = adapterFav
    }

    private fun getData() {
        databaseRefFavourite.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                
                if (!snapshot.exists()) {
                    if (isAdded) {
                        emptyFavTextView.visibility = View.VISIBLE
                    }
                }else{
                    favProgressBar.visibility = View.VISIBLE
                    listFav?.clear()
                    for (data in snapshot.children) {

                        val user = data.value.toString()

                        val lastElement = snapshot.children.last().value

                        databaseRefProduct.child(user).addValueEventListener(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                val favData = snapshot.getValue(Favourite::class.java)

                                if (favData != null) {
                                    listFav?.add(favData)
                                    favProgressBar.visibility = View.GONE
                                }

                                if (lastElement == snapshot.key) {
                                    adapterFav?.notifyDataSetChanged()
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
                TODO("Not yet implemented")
            }
        })
    }

    private fun initVar(view: View) {
        favRecyclerView = view.findViewById(R.id.favRecyclerView)
        emptyFavTextView = view.findViewById(R.id.emptyFavTextView)
        favProgressBar = view.findViewById(R.id.favProgressBar)

        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefFavourite = FirebaseDatabase.getInstance().getReference("Favourite")
    }

    override fun onItemClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        Navigation.findNavController(requireView()).navigate(R.id.action_favouriteFragment_to_productDetailsFragment2, bundle)
    }

}