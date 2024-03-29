package com.mitteloupe.randomgenkt.datasource

/**
 * Created by Eran Boudjnah on 29/04/2018.
 */
@Suppress("unused")
class AstronomyDataSource private constructor() {

    val solarSystemPlanetNames: List<String>
        get() = listOf(
            "Mercury",
            "Venus",
            "Earth",
            "Mars",
            "Jupiter",
            "Saturn",
            "Uranus",
            "Neptune"
        )

    val stars: List<String>
        get() = listOf(
            "Acamar", "Achernar", "Achird", "Acrab", "Acrux", "Acubens", "Adhafera", "Adhara",
            "Adhil", "Ain", "Ainalrami", "Aladfar", "Albaldah", "Albali", "Albireo", "Alchiba",
            "Alcor", "Alcyone", "Aldebaran", "Alderamin", "Aldhanab", "Aldhibah", "Aldulfin",
            "Alfirk", "Algedi", "Algenib", "Algieba", "Algol", "Algorab", "Alhena", "Alioth",
            "Aljanah", "Alkaid", "Alkalurops", "Alkaphrah", "Alkarab", "Alkes", "Almaaz", "Almach",
            "Alnair", "Alnasl", "Alnilam", "Alnitak", "Alniyat", "Alphard", "Alphecca", "Alpheratz",
            "Arrakis", "Alrescha", "Alsafi", "Alsciaukat", "Alsephina", "Alshain", "Alshat",
            "Altair", "Altais", "Alterf", "Aludra", "Alula Australis", "Alula Borealis", "Alya",
            "Alzirr", "Ancha", "Angetenar", "Ankaa", "Anser", "Antares", "Arcturus",
            "Arkab Posterior", "Arkab Prior", "Arneb", "Ascella", "Asellus Australis",
            "Asellus Borealis", "Aspidiske", "Asterope", "Athebyne", "Atik", "Atlas", "Atria",
            "Avior", "Azelfafage", "Azha", "Barnard's Star", "Baten Kaitos", "Beemim", "Beid",
            "Bellatrix", "Betelgeuse", "Bharani", "Biham", "Botein", "Brachium", "Canopus",
            "Capella", "Caph", "Castor", "Castula", "Cebalrai", "Celaeno", "Cervantes", "Chalawan",
            "Chamukuy", "Chara", "Chertan", "Copernicus", "Cor Caroli", "Cujam", "Cursa", "Dabih",
            "Dalim", "Deneb", "Deneb Algedi", "Denebola", "Diadem", "Diphda", "Dschubba", "Dubhe",
            "Dziban", "Edasich", "Electra", "Elnath", "Eltanin", "Enif", "Errai", "Fafnir", "Fang",
            "Fomalhaut", "Fulu", "Furud", "Fuyue", "Gacrux", "Giausar", "Gienah", "Ginan",
            "Gomeisa", "Grumium", "Hadar", "Haedus", "Hamal", "Hassaleh", "Hatysa", "Helvetios",
            "Homam", "Iklil", "Intercrus", "Izar", "Jabbah", "Jishui", "Kaffaljidhma", "Kang",
            "Kaus Australis", "Kaus Borealis", "Kaus Media", "Keid", "Khambalia", "Kitalpha",
            "Kochab", "Kornephoros", "Kurhah", "Larawag", "Lesath", "Libertas", "Lich",
            "Lilii Borea", "Maasym", "Mahasim", "Maia", "Marfik", "Markab", "Markeb", "Marsic",
            "Matar", "Mebsuta", "Megrez", "Meissa", "Mekbuda", "Meleph", "Menkalinan", "Menkar",
            "Menkent", "Menkib", "Merak", "Merga", "Meridiana", "Merope", "Mesarthim",
            "Miaplacidus", "Mimosa", "Minchir", "Minelauva", "Mintaka", "Mira", "Mirach", "Miram",
            "Mirfak", "Mirzam", "Misam", "Mizar", "Mothallah", "Muliphein", "Muphrid", "Muscida",
            "Musica", "Naos", "Nashira", "Nekkar", "Nembus", "Nihal", "Nunki", "Nusakan", "Ogma",
            "Peacock", "Phact", "Phecda", "Pherkad", "Pipirima", "Pleione", "Polaris",
            "Polaris Australis", "Polis", "Pollux", "Porrima", "Praecipua", "Prima Hyadum",
            "Procyon", "Propus", "Proxima Centauri", "Ran", "Rasalas", "Rasalgethi", "Rasalhague",
            "Rastaban", "Regulus", "Revati", "Rigel", "Rigil Kentaurus", "Rotanev", "Ruchbah",
            "Rukbat", "Sabik", "Saclateni", "Sadachbia", "Sadalbari", "Sadalmelik", "Sadalsuud",
            "Sadr", "Saiph", "Salm", "Sargas", "Sarin", "Sceptrum", "Scheat", "Schedar",
            "Secunda Hyadum", "Segin", "Seginus", "Sham", "Shaula", "Sheliak", "Sheratan",
            "Sirius", "Situla", "Skat", "Spica", "Sualocin", "Subra", "Suhail", "Sulafat", "Syrma",
            "Tabit", "Taiyangshou", "Taiyi", "Talitha", "Tania Australis", "Tania Borealis",
            "Tarazed", "Taygeta", "Tegmine", "Tejat", "Terebellum", "Theemin", "Thuban", "Tiaki",
            "Tianguan", "Tianyi", "Titawin", "Tonatiuh", "Torcular", "Tureis", "Unukalhai",
            "Unurgunite", "Vega", "Veritate", "Vindemiatrix", "Wasat", "Wazn", "Wezen", "Wurren",
            "Xamidimura", "Xuange", "Yed Posterior", "Yed Prior", "Yildun", "Zaniah", "Zaurak",
            "Zavijava", "Zhang", "Zibal", "Zosma", "Zubenelgenubi", "Zubenelhakrabi",
            "Zubeneschamali"
        )

    companion object {
        val instance = AstronomyDataSource()
    }
}
