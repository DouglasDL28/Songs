import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Song (val year: String, val country: String, val region: String, val artistName: String, val song: String, val artistGender: String, val groupOrSolo: String, val place: String, val points: String, val isFinal: String, val isSonginEnglish: String, val songQuality: String, val normalizedPoints: String, val energy: String, val duration: String, val acousticness: String, val danceability: String, val tempo: String, val speechiness: String, val key: String, val liveness: String, val timeSignature: String, val mode: String, val valance: String, val hapiness: String) {
    class SongArrayDeserealizer: ResponseDeserializable<Array<Song>> {
        override fun deserialize (content: String): Array<Song>? {
            return Gson().fromJson(content, Array<Song>::class.java)
        }
    }
}