package com.umar.listdemoapp

/**
 * Model data untuk karakter Marvel
 * @param characterName nama karakter (misalnya: "Thor")
 * @param name nama asli aktor (misalnya: "Chris Hemsworth")
 * @param imageResource ID sumber daya gambar (misalnya: R.drawable.thor)
 */
data class MarvelChar(
    val characterName: String,
    val name: String,
    val imageResource: Int
)

/**
 * Fungsi untuk mendapatkan semua data karakter Marvel
 * @return List dari objek MarvelChar
 */
fun getAllMarvelChars(): List<MarvelChar> {
    return listOf(
        MarvelChar("Iron Man", "Robert Downey Jr.", R.drawable.ironman),
        MarvelChar("Captain America", "Chris Evans", R.drawable.captain_america),
        MarvelChar("Thor", "Chris Hemsworth", R.drawable.thor),
        MarvelChar("Hulk", "Mark Ruffalo", R.drawable.hulk),
        MarvelChar("Black Widow", "Scarlett Johansson", R.drawable.black_widow),
        MarvelChar("Hawkeye", "Jeremy Renner", R.drawable.hawkeye),
        MarvelChar("Spider-Man", "Tom Holland", R.drawable.spiderman),
        MarvelChar("Black Panther", "Chadwick Boseman", R.drawable.black_panther),
        MarvelChar("Doctor Strange", "Benedict Cumberbatch", R.drawable.doctor_strange),
        MarvelChar("Captain Marvel", "Brie Larson", R.drawable.captain_marvel),
        MarvelChar("Ant-Man", "Paul Rudd", R.drawable.antman),
        MarvelChar("Vision", "Paul Bettany", R.drawable.vision),
        MarvelChar("Scarlet Witch", "Elizabeth Olsen", R.drawable.scarlet_witch),
        MarvelChar("Loki", "Tom Hiddleston", R.drawable.loki),
        MarvelChar("Winter Soldier", "Sebastian Stan", R.drawable.winter_soldier)
    )
}