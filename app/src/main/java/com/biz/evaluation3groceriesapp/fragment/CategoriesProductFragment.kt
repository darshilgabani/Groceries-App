package com.biz.evaluation3groceriesapp.fragment

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.CategoriesAdapter
import com.biz.evaluation3groceriesapp.clicklistener.CategoriesClickListener
import com.biz.evaluation3groceriesapp.modelclass.Categories
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.*

class CategoriesProductFragment : Fragment(), CategoriesClickListener {
    private var cartAddValue: Any? = null

    private var isAdded : Boolean? = null

    lateinit var skeletonLoading: LottieAnimationView

    lateinit var categoriesTitle: TextView
    lateinit var categoriesRecyclerView: RecyclerView
    lateinit var categoriesProgressBar: ProgressBar
    lateinit var backButton: ImageView

    private lateinit var databaseRefExplore: DatabaseReference
    private lateinit var databaseRefCategories: DatabaseReference
    private lateinit var databaseRefProduct: DatabaseReference
    private lateinit var databaseRefAddToCart: DatabaseReference

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

        getProductIndex()

        onClick()

        setLayout()

        adapterCategories()

        return view
    }

    private fun onClick() {
        backButton.setOnClickListener {
            Navigation.findNavController(requireView()).navigateUp()
        }
    }

    private fun setLayout() {
        categoriesRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val totalItems = parent.adapter?.itemCount ?: 0
                val index = parent.getChildAdapterPosition(view)
                if (index % 2 == 0) {
                    outRect.left = 60
                    outRect.right = 20
                }else {
                    outRect.left = 20
                    outRect.right = 60
                }

                if (totalItems % 2 == 0){
                    if (index == totalItems - 1 || index == totalItems-2){
                        outRect.bottom = 40
                    }
                }else{
                    if (index == totalItems - 1){
                        outRect.bottom = 40
                    }
                }
            }
        })

        categoriesRecyclerView.layoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
    }

    private fun adapterCategories() {
        adapterCategories = CategoriesAdapter(listCategories!!)
        adapterCategories?.setSelectListener(this)
        categoriesRecyclerView.adapter = adapterCategories
    }

    private fun getProductIndex() {
        skeletonLoading.visibility = View.VISIBLE
        listCategories?.clear()
        databaseRefCategories.child(clickedId!!).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val index = data.value.toString()
                    indexList.add(index)
                }
                if (indexList.size == snapshot.childrenCount.toInt()) {
                    getAllData()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun getAllData() {
        databaseRefProduct.child(indexList[0])
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    val productData = snapshot.getValue(Categories::class.java)
                    listCategories?.add(productData!!)

                    if (indexList.isNotEmpty()) {
                        indexList.removeAt(0)
                    }

                    if (indexList.isEmpty()) {
                        adapterCategories?.notifyDataSetChanged()

//                        if (isAdded == true){
//                            Toast.makeText(context?.applicationContext, "Added to Cart Successfully", Toast.LENGTH_SHORT).show()
//                        }else if (isAdded == false){
//                            Toast.makeText(context?.applicationContext, "Removed From Cart to Successfully", Toast.LENGTH_SHORT).show()
//                        }
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

    private fun initVar(view: View) {
        categoriesTitle = view.findViewById(R.id.categoriesTitle)
        categoriesRecyclerView = view.findViewById(R.id.categoriesRecyclerView)
        categoriesProgressBar = view.findViewById(R.id.categoriesProgressBar)
        backButton = view.findViewById(R.id.backButton)
        skeletonLoading = view.findViewById(R.id.skeletonLoading)

        isAdded = null

        databaseRefExplore = FirebaseDatabase.getInstance().getReference("Explore")
        databaseRefCategories = FirebaseDatabase.getInstance().getReference("Categories")
        databaseRefProduct = FirebaseDatabase.getInstance().getReference("Products")
        databaseRefAddToCart = FirebaseDatabase.getInstance().reference.child("AddToCart")

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
//        skeletonLoading.visibility = View.VISIBLE
        categoriesProgressBar.visibility = View.VISIBLE

        databaseRefProduct.child(id).child("Added")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    cartAddValue = snapshot.value
                    if (cartAddValue == false) {
                        databaseRefAddToCart.child(id).setValue(id).addOnSuccessListener {
                                databaseRefProduct.child(id).child("Added").setValue(true).addOnSuccessListener {
                                    addButtonImage.setImageResource(R.drawable.tickmark_icon)
//                                    getProductIndex()
//                                    isAdded = true
                                    categoriesProgressBar.visibility = View.GONE
                                }

                            }
                    } else if (cartAddValue == true) {
                        databaseRefAddToCart.child(id).removeValue().addOnSuccessListener {

                            databaseRefProduct.child(id).child("Added").setValue(false).addOnSuccessListener {
                                addButtonImage.setImageResource(R.drawable.plus_image)
//                                getProductIndex()
//                                isAdded = false
                                categoriesProgressBar.visibility = View.GONE
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