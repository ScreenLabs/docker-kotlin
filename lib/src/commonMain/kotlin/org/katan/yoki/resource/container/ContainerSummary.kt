package org.katan.yoki.resource.container

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
public data class ContainerSummary internal constructor(
    @SerialName("Id") val id: String,
    @SerialName("Names") val names: List<String>? = null,
    @SerialName("Image") val image: String,
    @SerialName("ImageID") val imageCreatedFrom: String? = null,
    @SerialName("Command") val command: String? = null,
    @SerialName("Created") val createdAtRaw: Long,
    @SerialName("SizeRw") val sizeRw: Long? = null,
    @SerialName("SizeRootFs") val sizeRootFs: Long? = null,
    @SerialName("State") val state: String,
    @SerialName("Status") val status: String,
    @SerialName("Labels") val labels: Map<String, String>,
    // TODO Ports, HostConfig, NetworkConfig, Mounts
) {

    public val name: String by lazy { names?.firstOrNull() ?: id }
    public val createdAt: Instant by lazy { Instant.fromEpochMilliseconds(createdAtRaw) }
}