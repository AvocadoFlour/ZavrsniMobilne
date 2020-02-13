package hr.tomislav.stipic.chatpractice.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import hr.tomislav.stipic.chatpractice.LastMessage.LastMessageRow
import hr.tomislav.stipic.chatpractice.R
import hr.tomislav.stipic.chatpractice.objects.ChatMessage
import hr.tomislav.stipic.chatpractice.objects.User
import hr.tomislav.stipic.chatpractice.startScreens.RegistrationActivity
import kotlinx.android.synthetic.main.activity_messages.*

class MessagesActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "MessagesActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        messages_overview_recycler_view.adapter = adapter

        messages_overview_recycler_view.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        //postavljanje klika na adapter poruka koji vodi u poruke s tim korisnikom
        adapter.setOnItemClickListener { item, _ ->
            Log.d(TAG, "Testiranje klika na porukue u izborniku poruka")
            val intent = Intent(this, ChatActivity::class.java)

            val row = item as LastMessageRow
            row.chatPartnerUser

            intent.putExtra(UserItem.USER_KEY, row.chatPartnerUser)
            startActivity(intent)
        }

        //setUpDummyLatestMessages()
        listenForLastMessages()

        verifyUserIsLoggedIn()
        fetchCurrentUser()
    }

    val latestMessagesMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LastMessageRow(it))
        }
    }

    private fun listenForLastMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-mesages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                Log.d(TAG, "Citanje iz baze...")
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessagesMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }

    private val adapter = GroupAdapter<GroupieViewHolder>()


    private fun setUpDummyLatestMessages() {
    //     adapter.add(LastMessageRow())
    //    adapter.add(LastMessageRow())
    //    adapter.add(LastMessageRow())
    }

    private fun fetchCurrentUser() {

        val uid = FirebaseAuth.getInstance().uid
        Log.d("TEST", uid.toString())
        val ref = FirebaseDatabase.getInstance().getReference("/user/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("MessagesActivity", "Trenutni korisni: ${currentUser?.username}")
                username_display_textview.text = currentUser?.username
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
        }
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                verifyUserIsLoggedIn()
        }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

}
