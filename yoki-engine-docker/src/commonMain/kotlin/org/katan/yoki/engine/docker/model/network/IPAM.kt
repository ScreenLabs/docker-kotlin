package org.katan.yoki.engine.docker.model.network

import kotlinx.serialization.*

/**
 * @property driver Name of the IPAM driver to use.
 * @property config List of IPAM configuration options.
 * @property options Driver-specific options, specified as a map.
 */
@Serializable
public data class IPAM(
    @SerialName("Driver") public val driver: String,
    @SerialName("Config") public val config: List<Map<String, String>>? = null,
    @SerialName("Options") public val options: Map<String, String>? = null
)