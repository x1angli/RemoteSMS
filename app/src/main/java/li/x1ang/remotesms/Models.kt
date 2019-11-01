package li.x1ang.remotesms

data class PhoneMessage(
        val timestamp: Long,
        val senderNum: String,
        val receiverNum: String?,
        val content: String
)