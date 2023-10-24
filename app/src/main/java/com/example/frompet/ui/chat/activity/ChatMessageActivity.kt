package com.example.frompet.ui.chat.activity

import com.example.frompet.ui.chat.dialog.ChatExitDailog
import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeZone
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frompet.MatchSharedViewModel
import com.example.frompet.data.model.ChatItem
import com.example.frompet.data.model.ChatMessage
import com.example.frompet.ui.chat.adapter.ChatMessageAdapter
import com.example.frompet.databinding.ActivityChatMessageBinding
import com.example.frompet.data.model.User
import com.example.frompet.ui.chat.viewmodel.ChatViewModel
import com.example.frompet.ui.chat.viewmodel.MessageViewModel


import com.google.firebase.auth.FirebaseAuth
import java.util.Date
import java.util.Locale


class ChatMessageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatMessageBinding
    private val messageViewModel: MessageViewModel by viewModels()
    private val chatViewModel: ChatViewModel by viewModels()
    private val matchSharedViewModel: MatchSharedViewModel by viewModels()
    private lateinit var receiverId: String
    private lateinit var adapter: ChatMessageAdapter
    private val auth = FirebaseAuth.getInstance()
    private val typingTimeoutHandler = Handler(Looper.getMainLooper())
    private val typingTimeoutRunnable = Runnable {
        messageViewModel.setTypingStatus(receiverId,false)
    }

    companion object {
        const val USER = "user"
        const val PICK_IMAGE_FROM_ALBUM = 0
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatMessageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            setupRecyclerView()
            observeViewModels()

            val user: User? = intent.getParcelableExtra(USER)
            user?.let {
                receiverId = it.uid
                handleChatActions(it) }

            backBtn.setOnClickListener {
                goneNewMessage()
                finish()
            }
            ivExit.setOnClickListener {
                showExitDailog()
            }

            ivSendImage.setOnClickListener { goGallery() }
        }
    }

    override fun onBackPressed() {
        goneNewMessage()
        super.onBackPressed()
    }

    override fun onDestroy() {
        goneNewMessage()
        super.onDestroy()
    }

    private fun setupRecyclerView() {
        adapter = ChatMessageAdapter(this@ChatMessageActivity)
        with(binding) {
            rvMessage.adapter = adapter
            val layoutManager = LinearLayoutManager(this@ChatMessageActivity)
            layoutManager.stackFromEnd = true
            rvMessage.layoutManager = layoutManager
        }
    }

    private fun observeViewModels() {
        messageViewModel.chatMessages.observe(this) { messages ->
            val chatItems = convertToChatItems(messages)
            adapter.submitList(chatItems) {
                binding.rvMessage.post {
                    binding.rvMessage.scrollToPosition(chatItems.size - 1)
                }
            }
        }


        messageViewModel.isTyping.observe(this, Observer { isTyping ->
            binding.tvTyping.text = if (isTyping) "입력중..." else ""
        })
    }

    private fun showExitDailog() {
        ChatExitDailog(this).showExitDailog {
            val user: User? = intent.getParcelableExtra(USER)
            user?.let { selectUser ->
                val currentUserId = auth.currentUser?.uid ?: return@let
                val chatRoomId = messageViewModel.chatRoom(currentUserId, selectUser.uid)
                matchSharedViewModel.removeMatchedUser(selectUser.uid)

                chatViewModel.removeChatRoom(chatRoomId)
                finish()
            }
        }
    }
    private fun convertToChatItems(messages: List<ChatMessage>): List<ChatItem> {
        val chatItems = mutableListOf<ChatItem>()
        var lastDate: String? = null
        val sdf = SimpleDateFormat("yyyy년MM월dd일", Locale.KOREA)
        sdf.timeZone = TimeZone.getTimeZone("Asia/Seoul")

        val currentDate = sdf.format(Date())

        for (message in messages) {
            val messageDate = sdf.format(Date(message.timestamp))
            if (lastDate == null || messageDate != lastDate) {
                if (messageDate == currentDate) {
                    chatItems.add(ChatItem.DateHeader("오늘"))
                } else {
                    chatItems.add(ChatItem.DateHeader(messageDate))
                }
                lastDate = messageDate
            }
            chatItems.add(ChatItem.MessageItem(message))
        }

        return chatItems
    }


    private fun handleChatActions(user: User) {
        displayInfo(user)
        messageViewModel.checkTypingStatus(user.uid)

        val currentUserId = auth.currentUser?.uid ?: return
        val chatRoomId = messageViewModel.chatRoom(currentUserId, user.uid)
        messageViewModel.observeChatMessages(chatRoomId)
        messageViewModel.observeTypingStatus(user.uid)
        messageViewModel.observeUserProfile(user.uid)

        with(binding) {
            ivSendBtn.setOnClickListener {
                val message = etMessage.text.toString()
                if (message.isNotEmpty()) {
                    messageViewModel.sendMessage(user.uid, message)
                    etMessage.text.clear()
                }
            }

            etMessage.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s.isNullOrEmpty()) {
                        messageViewModel.setTypingStatus(user.uid, false)
                    } else {
                        messageViewModel.setTypingStatus(user.uid, true)
                        typingTimeoutHandler.removeCallbacks(typingTimeoutRunnable)
                        typingTimeoutHandler.postDelayed(typingTimeoutRunnable, 5000)
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        messageViewModel.loadPreviousMessages(chatRoomId)
    }


    private fun goneNewMessage() {
        val user: User? = intent.getParcelableExtra(USER)
        user?.let {
            val currentUserId = auth.currentUser?.uid ?: return
            val chatRoomId = messageViewModel.chatRoom(currentUserId, user.uid)
            messageViewModel.goneNewMessages(chatRoomId)
        }
    }

    private fun goGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_ALBUM)
    }

    private fun displayInfo(user: User) {
        binding.tvFriendName.text = user.petName
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            val photoUri = data?.data
            photoUri?.let {
                val user: User? = intent.getParcelableExtra(USER)
                user?.let { selectedUser ->
                    messageViewModel.uploadImage(it, selectedUser)
                }
            }
        }
    }
}