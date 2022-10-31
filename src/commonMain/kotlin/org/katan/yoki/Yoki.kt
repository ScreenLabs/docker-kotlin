package org.katan.yoki

import io.ktor.client.HttpClient
import io.ktor.utils.io.core.Closeable
import kotlinx.serialization.json.Json
import org.katan.yoki.YokiConfigBuilder.Companion.DEFAULT_DOCKER_API_VERSION
import org.katan.yoki.net.createHttpClient
import org.katan.yoki.resource.container.ContainerResource
import org.katan.yoki.resource.exec.ExecResource
import org.katan.yoki.resource.image.ImageResource
import org.katan.yoki.resource.network.NetworkResource
import org.katan.yoki.resource.secret.SecretResource
import org.katan.yoki.resource.system.SystemResource
import org.katan.yoki.resource.volume.VolumeResource
import kotlin.jvm.JvmField
import kotlin.jvm.JvmStatic

/**
 * Creates a new Yoki instance with platform default socket path and [DEFAULT_DOCKER_API_VERSION] Docker API version
 * that'll be merged with specified configuration.
 *
 * @param config Yoki configuration.
 */
public inline fun Yoki(
    crossinline config: YokiConfigBuilder.() -> Unit = {}
): Yoki {
    return Yoki(
        YokiConfigBuilder()
            .forCurrentPlatform()
            .apply { apiVersion = DEFAULT_DOCKER_API_VERSION }
            .apply(config)
            .build()
    )
}

/**
 * Yoki heart, where all resource accessors and other things are located.
 *
 * Create and configure a fresh Yoki instance by calling [Yoki.create] or [org.katan.yoki.Yoki]
 *
 * Note: This class must be a singleton, that is, don't instantiate it more than once in your code, and, implements
 * [Closeable] so be sure to [close] it after use, we recommend you to use [use] block to this.
 */
@YokiDsl
public class Yoki @PublishedApi internal constructor(
    public val config: YokiConfig
) : Closeable {

    public companion object {

        /**
         * Creates a new Yoki instance with platform default socket path and targeting [DEFAULT_DOCKER_API_VERSION]
         * Docker API version.
         */
        @JvmStatic
        public fun create(): Yoki {
            return Yoki()
        }

        /**
         * Creates a new Yoki instance.
         *
         * @param config Configurations to the instance.
         */
        @JvmStatic
        public fun create(config: YokiConfig): Yoki {
            return Yoki(config)
        }

        /**
         * Creates a new Yoki instance with the specified socket path configuration.
         *
         * @param socketPath The socket path that'll be used on connection.
         */
        @JvmStatic
        public fun create(socketPath: String): Yoki {
            return Yoki {
                this.socketPath = socketPath
            }
        }

        /**
         * Creates a new Yoki instance using UNIX defaults configuration.
         */
        @JvmStatic
        public fun createWithUnixDefaults(): Yoki {
            return Yoki { useUnixDefaults() }
        }

        /**
         * Creates a new Yoki instance using HTTP defaults configuration.
         */
        @JvmStatic
        public fun createWithHttpDefaults(): Yoki {
            return Yoki { useHttpDefaults() }
        }
    }

    private val httpClient: HttpClient = createHttpClient(this)
    private val json: Json = Json {
        ignoreUnknownKeys = true
    }

    @JvmField
    public val containers: ContainerResource = ContainerResource(httpClient, json)
    @JvmField
    public val networks: NetworkResource = NetworkResource(httpClient, json)
    @JvmField
    public val volumes: VolumeResource = VolumeResource(httpClient, json)
    @JvmField
    public val secrets: SecretResource = SecretResource(httpClient, json)
    @JvmField
    public val images: ImageResource = ImageResource(httpClient, json)
    @JvmField
    public val exec: ExecResource = ExecResource(httpClient)
    @JvmField
    public val system: SystemResource = SystemResource(httpClient)

    override fun close() {
        httpClient.close()
    }
}

/**
 * DslMarker for Yoki.
 */
@DslMarker
public annotation class YokiDsl