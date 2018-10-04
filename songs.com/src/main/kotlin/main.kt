import com.github.kittinunf.fuel.Fuel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

fun main (args: Array<String>) {
   val url = "https://next.json-generator.com/api/json/get/EkeSgmXycS"


    Fuel.get(url).responseObject(Song.SongArrayDeserealizer()) { request, response, result ->
        val (songs, error) = result
        songs?.forEach { println(it) }

    }


    Database.connect(
            "jdbc:postgresql:music",
            "org.postgresql.Driver",
            "postgres",
            "postgres"
    )

    transaction {
        SchemaUtils.create(Library)

        Library.insert {
            it[song] = "SharkTururu"
        }


        println("Estas son las canciones:")
        for (song in Library.selectAll()) {
            println("${song[Library.id]}: ${song[Library.song]}")
        }
    }

    Thread.sleep(5000)

}