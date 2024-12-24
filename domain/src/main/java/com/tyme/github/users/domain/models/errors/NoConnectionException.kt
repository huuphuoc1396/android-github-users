package com.tyme.github.users.domain.models.errors

/**
 * A custom exception class representing a lack of network connection.
 *
 * This class extends [Exception] and includes a default error message indicating no network connection.
 *
 * @property message The error message explaining the cause of the exception. Defaults to "No connection".
 *
 * **Usage Example:**
 * ```
 * throw NoConnectionException()
 * ```
 */
data class NoConnectionException(
    override val message: String = "No connection",
) : Exception()