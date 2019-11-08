package hr.tomislav.stipic.chatpractice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.Item
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select user"

        val adapter = GroupAdapter<GroupieViewHolder>()

        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())
        adapter.add(UserItem())


        new_message_recycler_view.adapter = adapter

    }
}

class UserItem: Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // BIt će pozivan za svaki objekt u listi
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}