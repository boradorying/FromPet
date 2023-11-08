package com.example.frompet.ui.commnunity.communitydetail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.frompet.R
import com.example.frompet.data.model.CommentData
import com.example.frompet.data.model.CommunityData
import com.example.frompet.data.model.User
import com.example.frompet.databinding.ActivityCommunityDetailBinding

import com.example.frompet.ui.commnunity.community.CommunityViewModel
import com.example.frompet.util.getAddressGeocoder

import com.example.frompet.util.showToast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class CommunityDetailActivity : AppCompatActivity() {

    private var _binding: ActivityCommunityDetailBinding? = null
    private val binding get() = _binding!!

    private val currentUser = FirebaseAuth.getInstance().currentUser
    private val communityViewModel: CommunityViewModel by viewModels()

    private val store = FirebaseFirestore.getInstance()


    private var communityData: CommunityData? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CommentAdapter
    private lateinit var replyCountTextView: TextView



    companion object {
        const val COMMUNITY_DATA = "communityData"
        const val DOCS_ID = "docsId"
        const val REQUEST_CODE_COMMENT_MODIFY = 1004
    }


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityCommunityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        recyclerView = binding.rvReply
        adapter = CommentAdapter { commentData ->
            showBottomSheet(commentData)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        communityData = intent.getParcelableExtra(DOCS_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            communityData = intent.getParcelableExtra(COMMUNITY_DATA, CommunityData::class.java)
        } else {
            communityData = intent.extras?.getParcelable(COMMUNITY_DATA) as CommunityData?
        }





        communityData = intent.getParcelableExtra(DOCS_ID)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            communityData = intent.getParcelableExtra(COMMUNITY_DATA, CommunityData::class.java)

        } else {
            communityData = intent.extras?.getParcelable(COMMUNITY_DATA) as CommunityData?

        }


        // 화면에 표시
        val title = binding.tvDetailTitle
        val contents = binding.tvDetailContents
        val tag = binding.chipTag
        val lastTime = binding.tvLastTime
        replyCountTextView = binding.tvReplyCount
        val address =binding.tvAddress




        // CommunityData에서 가져오기
        communityData?.let {
            title.text = it.title
            contents.text = it.contents
            tag.text = it.tag
            lastTime.text = formatDate(it.timestamp)
            fetchUserLocation(this, communityData!!.uid) { latitude, longitude ->
                val addressText = getAddressGeocoder(this, latitude, longitude)
                address.text = addressText
            }
            loadUserData(it.uid)
            setChipColor(it.tag)
            Log.d("tag","what is tag${it.tag}")
        }



        binding.backBtn.setOnClickListener {
            finish()
        }

        binding.threedots.setOnClickListener {
            showPopup(it, communityData?.docsId) // 팝업 메뉴 표시
        }


        binding.btnDetailEnroll.setOnClickListener {
            // 댓글 추가 버튼 클릭 시 호출되는 함수
            addComment()
        }
        loadComments()

    }
    fun fetchUserLocation(context: Context, uid: String, onLocationFetched: (Double, Double) -> Unit) {
        val locationRef = FirebaseDatabase.getInstance().getReference("location/$uid")
        locationRef.get().addOnSuccessListener { dataSnapshot ->
            val latitude = dataSnapshot.child("latitude").getValue(Double::class.java) ?: 0.0
            val longitude = dataSnapshot.child("longitude").getValue(Double::class.java) ?: 0.0
            onLocationFetched(latitude, longitude)
        }.addOnFailureListener {
        }
    }


    private fun setChipColor(tag: String) {
        val chipColor = when (tag) {
            "나눔" -> R.color.colorTagShare
            "사랑" -> R.color.colorTagLove
            "산책" -> R.color.colorTagWalk
            "정보교환" -> R.color.colorTagExchange
            else -> R.color.dark_gray
        }
        binding.chipTag.chipBackgroundColor = getColorStateList(chipColor)
    }



    private fun loadUserData(uid: String) = with(binding) {
        store.collection("User").document(uid)
            .get()
            .addOnSuccessListener { docsSnapshot ->
                val user = docsSnapshot.toObject(User::class.java)
                user?.let {

                    ivPetProfile.load(user.petProfile) {
                        error(R.drawable.sampleiamge)
                    }
                    tvPetName.text = user.petName
                }
            }
            .addOnFailureListener { e ->
                showToast("사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT)
            }
    }

    private fun formatDate(timestamp: Long?): String {
        timestamp ?: return "알 수 없음"
        val now = System.currentTimeMillis()
        val diff = now - timestamp

        val seconds = TimeUnit.MILLISECONDS.toSeconds(diff)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
        val hours = TimeUnit.MILLISECONDS.toHours(diff)
        val days = TimeUnit.MILLISECONDS.toDays(diff)

        return when {
            seconds < 60 -> "방금 전"
            minutes < 60 -> "${minutes}분 전"
            hours < 24 -> "${hours}시간 전"
            days == 1L -> "어제"
            else -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                sdf.format(Date(timestamp))
            }
        }
    }

    private fun showPopup(v: View, docsId: String?) {
        val popup = PopupMenu(this, v) // 팝업 객체 선언
        menuInflater.inflate(R.menu.popup_menu, popup.menu) // 메뉴 레이아웃 inflate



        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.delete -> {
                    // 삭제
                    deleteCommunity(docsId)
                    true
                }

                R.id.cut -> {
                    updateActivity()
                    true
                }

                else -> false
            }
        }
        popup.show()
    }


    private fun updateActivity() {
        val intent: Intent = Intent(this, CommunityDetailUpdateActivity::class.java)
        intent.putExtra(COMMUNITY_DATA, communityData)
        startActivity(intent)
        finish()
    }

    private fun deleteCommunity(docsId: String?) {
        docsId?.let {
            communityViewModel.deleteCommunityData(it)
            observeDeleteStatus()
        }
    }

    private fun observeDeleteStatus() {
        communityViewModel.deleteResult.observe(this) { isSuccess ->
            if (isSuccess) {
                showToast("게시물이 삭제되었습니다.", Toast.LENGTH_SHORT)
                setResult(Activity.RESULT_OK, Intent().apply { putExtra(DOCS_ID, communityData?.docsId) })
                finish()
            } else {
                showToast("게시물 삭제권한이 없습니다.", Toast.LENGTH_SHORT)
            }
        }
    }


    private fun addComment() {
        val etComments = binding.etDetailComments
        val commentText = etComments.text.toString()
        val commentId = store.collection("Community")
            .document(communityData?.docsId ?: "")
            .collection("Comment")
            .document().id

        if (commentText.isNotEmpty()) {
            val uid = currentUser?.uid
            if (uid != null) {

                store.collection("User")
                    .document(uid)
                    .get()
                    .addOnSuccessListener { userSnapshot ->
                        val user = userSnapshot.toObject(User::class.java)
                        if (user != null) {
                            val commentData = CommentData(
                                commentId,
                                content = commentText,
                                authorUid = currentUser?.uid ?: "",
                                postDocumentId = communityData?.docsId ?: "",
                                timestamp = System.currentTimeMillis()
                            )

                            store.collection("Community")
                                .document(communityData?.docsId ?: "")
                                .collection("Comment")
                                .document(commentId)
                                .set(commentData)
                                .addOnSuccessListener {
                                    showToast("댓글이 추가되었습니다", Toast.LENGTH_SHORT)
                                    etComments.text.clear()
                                }
                                .addOnFailureListener {
                                    showToast("댓글 추가에 실패했습니다", Toast.LENGTH_SHORT)
                                }
                        } else {
                            showToast("사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT)
                        }
                    }
                    .addOnFailureListener {
                        showToast("사용자 정보를 불러오는데 실패했습니다.", Toast.LENGTH_SHORT)
                    }
            }
        } else {
            showToast("댓글 내용을 입력하세요", Toast.LENGTH_SHORT)
        }
    }



    private fun loadComments() {
        val commentsRef = store.collection("Community")
            .document(communityData?.docsId ?: "")
            .collection("Comment")

        val query = commentsRef.orderBy("timestamp", Query.Direction.ASCENDING)

        query.addSnapshotListener { querySnapshot, error ->
            if (error != null) {
                return@addSnapshotListener
            }

            querySnapshot?.let {
                val comments = it.toObjects(CommentData::class.java)
                adapter.submitList(comments)
                val commentCount = comments.size
                replyCountTextView.text = commentCount.toString()
            }
        }
    }
    private fun showBottomSheet(commentData: CommentData) {

        val view = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)


        val modifyTextView = view.findViewById<TextView>(R.id.bottom_sheet_modify)
        val deleteTextView = view.findViewById<TextView>(R.id.bottom_sheet_delete)


        val dialog = BottomSheetDialog(this)
        dialog.setContentView(view)


        modifyTextView.setOnClickListener {
            val intent = Intent(this, CommentModify::class.java)
            intent.putExtra("commentText", commentData.content) // 댓글 내용을 인텐트에 추가!
            startActivity(intent)
            dialog.dismiss()
        }

        deleteTextView.setOnClickListener {
            val commentDocumentRef = store.collection("Community")
                .document(communityData?.docsId ?: "")
                .collection("Comment")
                .document(commentData.commentId)
            commentDocumentRef.delete()
                .addOnSuccessListener {
                    showToast("댓글이 삭제되었습니다", Toast.LENGTH_SHORT)
                    dialog.dismiss()
                }
                .addOnFailureListener {
                    showToast("댓글 삭제에 실패했습니다", Toast.LENGTH_SHORT)
                    dialog.dismiss()
                }
        }



        dialog.show()

        val dimView = View(this)
        dimView.setBackgroundColor(Color.parseColor("#80000000"))
        val parentLayout = findViewById<ViewGroup>(android.R.id.content)
        parentLayout.addView(dimView, ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        dialog.setOnDismissListener {
            parentLayout.removeView(dimView)
        }
    }

}