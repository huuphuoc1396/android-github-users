package com.tyme.github.users.domain.models.errors

/**
 * A custom exception class representing an unauthorized access error.
 *
 * This class extends [RuntimeException] and includes a default error message indicating that the access is unauthorized.
 *
 * @property message The error message explaining the cause of the exception. Defaults to "Unauthorized".
 *
 * **Usage Example:**
 * ```
 * throw UnauthorizedException()
 * ```
 */
data class UnauthorizedException(
    override val message: String = "Unauthorized",
) : Exception()