package com.padabajka.dating.core.repository.api.model.messenger

enum class MessageStatus(val raw: String) {
    Sending("sending"),
    FailedToSend("failed_to_send"),
    Sent("sent");

    companion object {
        private val mapper = entries.associateBy { it.raw }

        fun parse(raw: String): MessageStatus = mapper[raw]!!
    }
}
