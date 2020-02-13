package hr.tomislav.stipic.chatpractice.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import hr.tomislav.stipic.chatpractice.R
import hr.tomislav.stipic.chatpractice.objects.ChatMessage
import hr.tomislav.stipic.chatpractice.objects.User
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.received_chat_message.view.*
import kotlinx.android.synthetic.main.sent_chat_message.view.*

class ChatActivity : AppCompatActivity() {

    companion object {
        private val TAG = "ChatActivity"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        chat_messages_recycler_view.adapter = adapter

        toUser = intent.getParcelableExtra<User>(UserItem.USER_KEY)
        supportActionBar?.title = toUser?.username

        //setupDummyData()
        listenForMessages()

        send_chat_message_button.setOnClickListener {
            Log.d(TAG, "Attempting to send message...")
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val toId = toUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId")

        // addChildEventListener omogucuje slusanje novih poruka, odnosno, kada nova poruka
        // dode biti ce prikazana unutar recyclera
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage.text)

                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = MessagesActivity.currentUser ?: return
                        adapter.add(SentChatItem(chatMessage.text, currentUser))
                    } else {
                        adapter.add(ReceivedChatItem(chatMessage.text, toUser!!))
                    }
                }

                chat_messages_recycler_view.scrollToPosition(adapter.itemCount - 1)

            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private fun performSendMessage() {
        val text = input_chat_message.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(UserItem.USER_KEY)
        val toId = user.uid

        if (fromId == null) return

        //val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()

        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()

        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis())

        reference.setValue(chatMessage).addOnSuccessListener {
            Log.d(TAG, "Saved the message to FireBaye ${reference.key}")
            input_chat_message.text.clear()
            chat_messages_recycler_view.scrollToPosition(adapter.itemCount - 1)
        }

        toReference.setValue(chatMessage)

        val lastMessageReference = FirebaseDatabase.getInstance().getReference("/latest-mesages/$fromId/$toId")
        lastMessageReference.setValue(chatMessage)
        val lastMessageToReference = FirebaseDatabase.getInstance().getReference("/latest-mesages/$toId/$fromId")
        lastMessageToReference.setValue(chatMessage)
    }

    private fun setupDummyData() {
        val adapter = GroupAdapter<GroupieViewHolder>()

        //adapter.add(ReceivedChatItem("From message"))
       // adapter.add(SentChatItem("To message\nMESSAGEEE"))

        chat_messages_recycler_view.adapter = adapter
    }

}

class ReceivedChatItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.received_chat_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.received_chat_message_view.text = text

        //ucitaj sliku korisnika koji je trenutno ulogiran i poslao poruku
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.received_chat_message_user_photo
        if (uri == "No picture") {
            Picasso.get().load(R.drawable.app_icon)
                .into(targetImageView)
        } else {
            Picasso.get().load(user.profileImageUrl)
                .into(targetImageView)
        }
    }
}

class SentChatItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.sent_chat_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.sent_chat_message_view.text = text

        //ucitaj sliku korisnika koji salje poruku trenutno ulogiranom korisniku
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.sent_chat_message_user_photo
        if (uri == "No picture") {
            Picasso.get().load(R.drawable.app_icon)
                .into(targetImageView)
        } else {
            Picasso.get().load(user.profileImageUrl)
                .into(targetImageView)
        }
    }
}