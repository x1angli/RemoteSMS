package li.x1ang.remotesms

const val DINGTALK_ENDPOINT = "https://oapi.dingtalk.com/robot/send?access_token="

data class PhoneMessage(
        val timestamp: Long,
        val senderId: String,
        val content: String
)