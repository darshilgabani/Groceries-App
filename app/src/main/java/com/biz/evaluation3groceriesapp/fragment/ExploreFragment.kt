package com.biz.evaluation3groceriesapp.fragment

import android.graphics.Canvas
import android.graphics.Rect
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.biz.evaluation3groceriesapp.R
import com.biz.evaluation3groceriesapp.adapter.ExploreAdapter
import com.biz.evaluation3groceriesapp.clicklistener.ExploreClickListener
import com.biz.evaluation3groceriesapp.modelclass.Explore
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

        exploreRecyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
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

        exploreRecyclerView.layoutManager = gridLayoutManager
    }

    private fun adapterExplore() {
        adapterExplore = ExploreAdapter(listExplore!!)
        adapterExplore?.setSelectListener(this)
        exploreRecyclerView.adapter = adapterExplore
    }

    private fun getData() {
        listExplore?.clear()
        exploreProgressBar.visibility = View.VISIBLE
        databaseRefExplore.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (data in snapshot.children) {

                    val exploreData = data.getValue(Explore::class.java)

                    listExplore?.add(exploreData!!)

                }

                adapterExplore?.notifyDataSetChanged()
                exploreProgressBar.visibility = View.GONE
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

    override fun onItemClicked(id: String, name: String) {
        val bundle = Bundle()
        bundle.putString("ClickedID", id)
        bundle.putString("ClickedName", name)
        Navigation.findNavController(requireView()).navigate(R.id.action_exploreFragment_to_categoriesProductFragment, bundle)
    }

}