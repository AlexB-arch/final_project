package com.example.afinal

import java.util.concurrent.Executor
import java.util.concurrent.atomic.AtomicBoolean

class ScopedExecutor(private val executor: Executor) : Executor {
    private val shutdown = AtomicBoolean()
    override fun execute(command: Runnable) {
        // Return early if this object has been shut down.
        if (shutdown.get()) {
            return
        }
        executor.execute {
            // Check again in case it has been shut down in the mean time.
            if (shutdown.get()) {
                return@execute
            }
            command.run()
        }
    }

    fun shutdown() {
        shutdown.set(true)
    }
}