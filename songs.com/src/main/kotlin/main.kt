import com.github.kittinunf.fuel.Fuel
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

fun main (args: Array<String>) {
   val url = "https://next.json-generator.com/api/json/get/EkeSgmXycS"



    val (request, response, result) = Fuel.get(url).responseObject(Song.SongArrayDeserealizer())
    val (songs, err) = result


    Database.connect(
            "jdbc:postgresql:music",
            "org.postgresql.Driver",
            "postgres",
            "Douglasdb"
    )

    transaction {
        SchemaUtils.create(Library)
        SchemaUtils.create(Favorites)

        if (songs != null) {
            for (x in songs) {
                Library.insert {
                    it[song] = x.song
                    it[artistName] = x.artistName
                }
            }
        }

        val mainMenu = """
        MENU:
        1. Buscar canciones por nombre.
        2. Buscar canciones por artista.
        3. Mostrar canciones favoritas.
        4. Salir.
    """.trimIndent()

        var wantToContinue: Boolean = true

        do {
            println(mainMenu)
            var option = readLine()!!.toInt()

            when (option) {
                1 -> {
                    println("Cuál es el nombre la canción que desea buscar? ")
                    val name = readLine()!!

                    for (song in Library.select { Library.song like name }) {
                        println("${song[Library.id]}. ${song[Library.song]} by ${song[Library.artistName]} ")
                    }

                    print("Desea guardar una canción como favorita? ")
                    val favorite = readLine()!!.toUpperCase()

                    if(favorite == "SI") {
                        print("Ingrese el ID de la canción que desea agregar a sus favoritos: ")
                        val favoriteID = readLine()!!.toInt()

                        for (x in Library.select { Library.id eq favoriteID }) {
                            Favorites.insert{
                                it[song] = x[Library.song]
                                it[artistName] = x[Library.artistName]
                            }
                        }
                    }
                }

                2 -> {
                    print("Ingrese el nombre del artista: ")
                    val artist = readLine()!!

                    for (song in Library.select { Library.song like artist }) {
                        println("${song[Library.id]}. ${song[Library.song]} by ${song[Library.artistName]} ")
                    }

                    print("Desea guardar una canción como favorita?")
                    val favorite = readLine()!!.toUpperCase()

                    if(favorite == "SI") {
                        print("Ingrese el ID de la canción que desea agregar a sus favoritos: ")
                        val favoriteID = readLine()!!.toInt()

                        for (x in Library.select { Library.id eq favoriteID }) {
                            Favorites.insert{
                                it[song] = x[Library.song]
                                it[artistName] = x[Library.artistName]
                            }
                            }
                        }
                    }

                3 -> {
                    for(song in Favorites.selectAll()) {
                        println("${song[Favorites.id]}. ${song[Favorites.song]} by ${song[Favorites.artistName]}")
                    }
                }

                4 -> {
                    wantToContinue = false
                } //Sale
            }

        } while (wantToContinue)
    }

        Thread.sleep(5000)
}