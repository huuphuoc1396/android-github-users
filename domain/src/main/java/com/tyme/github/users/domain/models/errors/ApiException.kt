package com.tyme.github.users.domain.models.errors

/**
 * A custom exception class representing an API error.
 *
 * This class extends [RuntimeException] and includes an HTTP error code along with the error message.
 *
 * @property code The HTTP status code associated with the API error. Defaults to `0`.
 * @property message The error message explaining the cause of the exception. Defaults to an empty string.
 *
 * **Usage Example:**
 * ```
 * throw ApiException(code = 404, message = "Not Found")
 * ```
 */
data class ApiException(
    val code: Int = 0,
    override val message: String = "",
) : RuntimeException()