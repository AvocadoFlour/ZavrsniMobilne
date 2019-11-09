package hr.tomislav.stipic.chatpractice.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import hr.tomislav.stipic.chatpractice.R
import hr.tomislav.stipic.chatpractice.objects.User
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val user = intent.getParcelableExtra<User>(UserItem.USER_KEY)
        supportActionBar?.title = user.username

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())
        adapter.add(ReceivedChatItem())
        adapter.add(SentChatItem())


        chat_messages_recycler_view.adapter = adapter
    }
}

class ReceivedChatItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.received_chat_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
}

class SentChatItem: Item<GroupieViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.sent_chat_message
    }

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
}