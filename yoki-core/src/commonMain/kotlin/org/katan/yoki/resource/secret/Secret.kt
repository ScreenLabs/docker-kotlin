package org.katan.yoki.resource.secret

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Secrets are sensitive data that can be used by Docker services.
 *
 * @property id The id of the secret.
 * @property createdAt The instant this secret was created.
 * @property updatedAt The instant this secret was updated.
 */
@Serializable
public data class Secret(
    @SerialName("ID") public val id: String,
    @SerialName("CreatedAt") public val createdAt: Instant,
    @SerialName("UpdatedAt") public val updatedAt: Instant
    // TODO version
)
