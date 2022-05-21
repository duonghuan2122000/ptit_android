package com.sora.dbhshopping.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sora.dbhshopping.R
import com.sora.dbhshopping.adapter.ProductRecyclerViewAdapter
import com.sora.dbhshopping.screen.ProductDetailActivity
import com.sora.dbhshopping.viewmodel.ProductViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView

    private lateinit var adapter: ProductRecyclerViewAdapter

    private lateinit var swipeRefresh: SwipeRefreshLayout

    private lateinit var searchView: SearchView

    private val productViewModel: ProductViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeRefresh = view.findViewById(R.id.home_refresh)

        adapter = ProductRecyclerViewAdapter()

        recyclerView = view.findViewById(R.id.home_recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)

        productViewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })

        adapter.setOnItemClickListener {
            val intent = Intent(activity, ProductDetailActivity::class.java).apply {
                putExtra("product", it)
            }

            startActivity(intent)
        }

        swipeRefresh.setOnRefreshListener {
            productViewModel.refreshProducts()
            swipeRefresh.isRefreshing = false
        }

        searchView = view.findViewById(R.id.home_search_view)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                productViewModel.setQueryProduct(p0 ?: "")
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                if(p0 == null || p0?.isEmpty()){
                    productViewModel.setQueryProduct("")
                }
                return false
            }

        })
    }

    override fun onResume() {
        super.onResume()

        productViewModel.productsLiveData.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
        })
    }
}