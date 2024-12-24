package com.tyme.github.users.domain.extensions

/**
 * Extension function for nullable [Boolean] that returns `false` if the value is `null`.
 *
 * @return `false` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Boolean?.orFalse() = this ?: false

/**
 * Extension function for nullable [Boolean] that returns `true` if the value is `null`.
 *
 * @return `true` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Boolean?.orTrue() = this ?: true

/**
 * Extension function for nullable [Int] that returns `0` if the value is `null`.
 *
 * @return `0` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Int?.orZero() = this ?: 0

/**
 * Extension function for nullable [Long] that returns `0L` if the value is `null`.
 *
 * @return `0L` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Long?.orZero() = this ?: 0L

/**
 * Extension function for nullable [Float] that returns `0f` if the value is `null`.
 *
 * @return `0f` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Float?.orZero() = this ?: 0f

/**
 * Extension function for nullable [Double] that returns `0.0` if the value is `null`.
 *
 * @return `0.0` if the receiver is `null`, otherwise returns the receiver value.
 */
fun Double?.orZero() = this ?: 0.0

/**
 * Extension function for nullable [String] that returns an empty string if the value is `null`.
 *
 * @return an empty string if the receiver is `null`, otherwise returns the receiver value.
 */
fun String?.orEmpty() = this ?: ""

/**
 * Extension function for nullable [List] that returns an empty list if the value is `null`.
 *
 * @return an empty list if the receiver is `null`, otherwise returns the receiver value.
 */
fun <T> List<T>?.orEmpty() = this ?: emptyList()

/**
 * Extension function for nullable [Map] that returns an empty map if the value is `null`.
 *
 * @return an empty map if the receiver is `null`, otherwise returns the receiver value.
 */
fun <K, V> Map<K, V>?.orEmpty() = this ?: emptyMap()
