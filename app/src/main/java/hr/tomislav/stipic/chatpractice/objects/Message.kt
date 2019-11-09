package hr.tomislav.stipic.chatpractice.objects

import java.sql.Timestamp

class Message(val sender: String, message: String,timestamp: Timestamp) {
    constructor() : this("","", Timestamp(0))
}