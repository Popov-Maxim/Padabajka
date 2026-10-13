package com.padabajka.dating.core.sync

import com.padabajka.dating.core.repository.api.model.auth.UserId

/** Creates a manager with its own coroutine scope and socket for one user session. */
fun interface SyncManagerFactory {
    fun create(userId: UserId): SyncManager
}
