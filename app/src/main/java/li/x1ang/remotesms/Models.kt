package li.x1ang.remotesms



data class PhoneMessage(
        val timestamp: Long,
        val sender_id: String,
        val content: String
)