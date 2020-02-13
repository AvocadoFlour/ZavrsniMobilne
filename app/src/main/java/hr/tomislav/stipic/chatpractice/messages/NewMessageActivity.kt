package hr.tomislav.stipic.chatpractice.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import hr.tomislav.stipic.chatpractice.R
import hr.tomislav.stipic.chatpractice.objects.User
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select user"

        val adapter = GroupAdapter<GroupieViewHolder>()

        fetchUsers(adapter)

        new_message_recycler_view.adapter = adapter

    }

    private fun fetchUsers(adapter: GroupAdapter<GroupieViewHolder>) {
        val ref = FirebaseDatabase.getInstance().getReference("/user")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    Log.d("TESTESTEST", it.toString())
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        if (user.uid != FirebaseAuth.getInstance().uid) {
                            adapter.add(UserItem(user))
                        }
                    }
                }

                adapter.setOnItemClickListener {item, view ->


                    val userItem = item as UserItem
                    val intent = Intent(view.context, ChatActivity::class.java)
                    //intent.putExtra(UserItem.USER_KEY, userItem.user.username)
                    intent.putExtra(UserItem.USER_KEY, userItem.user)
                    startActivity(intent)

                    finish()

                }
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

}

class UserItem(val user: User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        // BIt će pozivan za svaki objekt u listi
        viewHolder.itemView.new_message_layout_contact_name.text = user.username
        if (user.profileImageUrl == "No picture") {
            Picasso.get().load(R.drawable.app_icon)
                .into(viewHolder.itemView.new_message_layout_contact_picture)
        } else {
            Picasso.get().load(user.profileImageUrl)
                .into(viewHolder.itemView.new_message_layout_contact_picture)
        }
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }

    companion object{
        const val USER_KEY = "USER_KEY"
    }

}