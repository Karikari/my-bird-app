package repositories

import BirdImage
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json

/**
 * Created by Kwaku Karikari on 03/09/2023.
 */
class BirdRepository {

    private val httpClient = HttpClient {
        install(ContentNegotiation){
            json()
        }
    }

    suspend fun getBirds(): List<BirdImage> {
        return httpClient
            .get("https://sebi.io/demo-image-api/pictures.json")
            .body()
    }

    fun closehttp(){
        httpClient.close()
    }
}