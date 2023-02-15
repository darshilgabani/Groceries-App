package com.biz.evaluation3groceriesapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.ExclusiveOfferAdapter
import com.biz.evaluation3groceriesapp.adapter.ExploreAdapter
import com.biz.evaluation3groceriesapp.adapter.FavouriteAdapter
import com.biz.evaluation3groceriesapp.clicklistener.ExploreClickListener
import com.biz.evaluation3groceriesapp.modelclass.ExclusiveOffer
import com.biz.evaluation3groceriesapp.modelclass.Explore
import com.biz.evaluation3groceriesapp.modelclass.Favourite
import com.google.firebase.database.*

class ExploreFragment : Fragment(),ExploreClickListener {
    lateinit var exploreRecyclerView: RecyclerView
    lateinit var exploreProgressBar: ProgressBar

    private lateinit var databaseRefExplore: DatabaseReference

    lateinit var gridLayoutManager: GridLayoutManager

    var listExplore: ArrayList<Explore>? = ArrayList()

    private var adapterExplore: ExploreAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)

        initVar(view)

        getData()

        setLayout()

        adapterExplore()

        return view
    }

    private fun setLayout() {
        gridLayoutManager = GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false)
        exploreRecyclerView.layoutManager = gridLayoutManager
    }

    private fun adapterExplore() {
        adapterExplore = ExploreAdapter(listExplore!!)
        adapterExplore?.setSelectListener(this)
        exploreRecyclerView.adapter = adapterExplore
    }

    private fun getData() {
        listExplore?.clear()
        databaseRefExplore.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {

                    val exploreData = data.getValue(Explore::class.java)

                    listExplore?.add(exploreData!!)

                }

                adapterExplore?.notifyDataSetChanged()


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun initVar(view: View) {
        exploreRecyclerView = view.findViewById(R.id.exploreRecyclerView)
        exploreProgressBar = view.findViewById(R.id.exploreProgressBar)

        databaseRefExplore = FirebaseDatabase.getInstance().getReference("Explore")

    }

    override fun onItemClicked(id: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        Navigation.findNavController(requireView())
            .navigate(R.id.action_exploreFragment_to_categoriesProductFragment, bundle)

//        Toast.makeText(requireContext(), id, Toast.LENGTH_SHORT).show()
    }

}