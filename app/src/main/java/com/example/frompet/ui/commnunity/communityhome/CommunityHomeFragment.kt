package com.example.frompet.ui.commnunity.communityhome

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frompet.MatchSharedViewModel
import com.example.frompet.R
import com.example.frompet.data.model.CommunityHomeData
import com.example.frompet.databinding.FragmentCommunityhomeBinding



class CommunityHomeFragment : Fragment() {

    private var _binding: FragmentCommunityhomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter : CommunityHomeAdapter
    private lateinit var communityHomeData : MutableList<CommunityHomeData>
    private val viewModel : MatchSharedViewModel by viewModels()
    private val _viewModel by lazy {
        ViewModelProvider(
            this,
            CategoryViewModelFactory(requireContext())
        )[CategoryViewModel::class.java]
    }
    private val communityHomeAdapter by lazy {
        CommunityHomeAdapter(
            onClicked = { item, position ->
                _viewModel.listClickCategory(item.petName)
                _viewModel.getHomeCategory()
            }
        )
    }
    private val imageSliderAdapter: ImageSliderAdapter by lazy { ImageSliderAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentCommunityhomeBinding.inflate(inflater,container,false)

        binding.imageSlider.adapter = imageSliderAdapter

        viewModel.getTopMatchedUsersThisWeek { topUsers->
            _binding?.let { binding ->
                imageSliderAdapter.submitList(topUsers)
                binding.dotsIndicator.setViewPager2(binding.imageSlider)
                startAutoScroll()
            }
        }
        _viewModel.selectPetType.observe(viewLifecycleOwner){selectPetType ->
            if(!selectPetType.isNullOrEmpty()){
                _viewModel.getHomeCategory()
            }
        }

        viewModel.getTotalMatchedCount{matchedCount->
            _binding?.tvNoticeText?.text = " 총 ${matchedCount}쌍이 매칭되었습니다!"
        }

        _viewModel.commuHomeDataList.observe(viewLifecycleOwner){CategoryList ->
            communityHomeAdapter.submitList(CategoryList)
        }
        communityHomeAdapter.onClicked = {item , position ->
            _viewModel.listClickCategory(item.petName)

            Log.e("zzzzzzzz","${item.petName},$position")
        }

        startNoticeTextAniMation()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    private fun initView() = with(binding) {
        categoryBt.layoutManager = GridLayoutManager(requireContext(), 4)
        categoryBt.adapter = communityHomeAdapter
        Log.e("RecyclerView", "RecyclerView adapter set with ${communityHomeAdapter.itemCount} items")

        _viewModel.commuHomeDataList.observe(viewLifecycleOwner){CateHomeList ->
            communityHomeAdapter.submitList(CateHomeList)
        }
        _viewModel.getHomeCategory()
        categoryBt.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                val isAtEndOfList = visibleItemCount + firstVisibleItemPosition >= totalItemCount

                if (isAtEndOfList) {

                }
            }
        })
    }

    private fun startAutoScroll() {
        val handler = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                _binding?.let { binding ->
                    val itemCount = imageSliderAdapter.itemCount
                    if (itemCount > 0) {
                        val nextItem = (binding.imageSlider.currentItem + 1) % itemCount
                        binding.imageSlider.setCurrentItem(nextItem, true)
                        handler.postDelayed(this, 3000)
                    }
                }
            }
        }
        handler.postDelayed(runnable, 3000)
    }

    private fun startNoticeTextAniMation(){
    val slideUp = AnimationUtils.loadAnimation(context, R.anim.slide_up)
    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down)

    slideUp.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}

        override fun onAnimationEnd(animation: Animation?) {
            _binding?.tvNoticeText?.startAnimation(slideDown)
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })

    slideDown.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) {}

        override fun onAnimationEnd(animation: Animation?) {
            Handler(Looper.getMainLooper()).postDelayed({
                _binding?.tvNoticeText?.startAnimation(slideUp)
            }, 1000)
        }

        override fun onAnimationRepeat(animation: Animation?) {}
    })

    _binding?.tvNoticeText?.startAnimation(slideUp)

}




    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}