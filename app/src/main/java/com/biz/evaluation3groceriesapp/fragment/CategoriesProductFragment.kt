package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CategoriesAdapter
import com.biz.evaluation3groceriesapp.adapter.ExploreAdapter
import com.biz.evaluation3groceriesapp.adapter.FavouriteAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CategoriesClickListener
import com.biz.evaluation3groceriesapp.modelclass.Categories
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.biz.evaluation3groceriesapp.modelclass.Favourite
import com.biz.evaluation3groceriesapp.utils.addToCart
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class CategoriesProductFragment : Fragment(), CategoriesClickListener {
    lateinit var categoriesTitle: TextView
    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var categoriesProgressBar: ProgressBar
    lateinit var backButton: ImageView

    private lateinit var databaseRefExplore: DatabaseReference
    private lateinit var databaseRefCategories: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference

    var clickedId: String? = null
    var clickedName: String? = null

    var listCategories: ArrayList<Categories>? = ArrayList()
    val indexList: ArrayList<String> = ArrayList()

    private var adapterCategories: CategoriesAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_categoriesproduct, container, false)

        initVar(view)

        getData()

        onClick()

        setLayout()

        adapterCategories()

        return view
    }

    private fun onClick() {
        backButton.setOnClickListener {
            Navigation.findNavController(requireView())
                .navigate(R.id.action_categoriesProductFragment_to_exploreFragment)
        }
    }

    private fun setLayout() {
        categoriesRecyclerView.layoutManager =
            GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }

    private fun adapterCategories() {
        adapterCategories = CategoriesAdapter(listCategories!!)
        adapterCategories?.setSelectListener(this)
        categoriesRecyclerView.adapter = adapterCategories
    }

    private fun getProductIndex() {
        categoriesProgressBar.visibility = View.VISIBLE
        listCategories?.clear()
        databaseRefCategories.child(clickedId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val index = data.value.toString()
                    indexList.add(index)
                }
                getAllData()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getAllData() {
        databaseRefProduct.child(indexList[0])
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val productData = snapshot.getValue(Categories::class.java)
                    listCategories?.add(productData!!)
                    indexList.removeAt(0)

                    if (indexList.isEmpty()) {
                        adapterCategories?.notifyDataSetChanged()
                        categoriesProgressBar.visibility = View.GONE
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

        getProductIndex()

    }

    private fun initVar(view: View) {
        categoriesTitle = view.findViewById(R.id.categoriesTitle)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        categoriesProgressBar = view.findViewById(R.id.categoriesProgressBar)
        backButton = view.findViewById(R.id.backButton)

        databaseRefExplore = FirebaseDatabase.getInstance().getReference("Explore")
        databaseRefCategories = FirebaseDatabase.getInstance().getReference("Categories")
        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")

        clickedId = arguments?.getString("ClickedID")
        clickedName = arguments?.getString("ClickedName")

        categoriesTitle.text = clickedName
    }

    override fun onItemClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        val navController = Navigation.findNavController(requireView())

        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.visibility = View.GONE

        navController.navigate(
            R.id.action_categoriesProductFragment_to_productDetailsFragment2,
            bundle
        )
    }

    override fun onAddToCartClicked(id: String, addButtonImage: ImageView) {
        addToCart(id, addButtonImage, requireContext(), categoriesProgressBar)
    }

}