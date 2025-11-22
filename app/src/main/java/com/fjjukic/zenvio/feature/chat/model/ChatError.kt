package com.fjjukic.zenvio.feature.chat.model

sealed class ChatError(message: String) : Exception(message) {
    // No internet, airplane mode, DNS fail
    class NoInternet : ChatError("No internet connection. Please check your internet connection.")

    // API reachable but failed or network unstable
    class NetworkFailure : ChatError("Network error occurred. Please try again.")

    // On job canceled
    class CancellationFailure : ChatError("")

    // Unexpected error
    class UnknownError : ChatError("Something went wrong. Please try again.")
}