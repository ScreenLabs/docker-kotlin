package org.katan.yoki

public expect interface Closeable

/**
 * Gets the value of the specified environment variable.
 * An environment variable is a system-dependent external named value
 */
internal expect fun env(key: String): String?

/**
 * Returns if the current platform is a UNIX-based platform.
 */
internal expect fun isUnixPlatform(): Boolean
