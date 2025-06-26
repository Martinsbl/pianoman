package net.testiprod.pianoman.app.music

enum class NoteLetter {
    C, D, E, F, G, A, B
}

enum class Accidental(val symbol: String, val offset: Int) {
    DOUBLE_FLAT("𝄫", -2),
    FLAT("♭", -1),
    NATURAL("", 0),
    SHARP("#", 1),
    DOUBLE_SHARP("𝄪", 2)
}

data class Note(
    val letter: NoteLetter,
    val octave: Int,
    val accidental: Accidental = Accidental.NATURAL
) {

    override fun toString(): String {
        return "${letter.name}${accidental.symbol}$octave"
    }
}

fun Note.toMidiNoteNumber(): Int {
    val baseNoteToMidi = mapOf(
        NoteLetter.C to 0,
        NoteLetter.D to 2,
        NoteLetter.E to 4,
        NoteLetter.F to 5,
        NoteLetter.G to 7,
        NoteLetter.A to 9,
        NoteLetter.B to 11
    )

    val baseMidi = baseNoteToMidi[letter] ?: 0
    val midiNumber = (octave + 1) * 12 + baseMidi + accidental.offset
    return midiNumber
}


fun Int.midiNumberToNote(preferSharps: Boolean = true): Note {
    val midiNumber = this
    require(midiNumber in 0..127) { "MIDI number must be between 0 and 127" }

    val noteIndex = midiNumber % 12
    val octave = (midiNumber / 12) - 1 // Adjusting for MIDI octave numbering

    val noteMap = if (preferSharps) {
        mapOf(
            0 to Pair(NoteLetter.C, Accidental.NATURAL),
            1 to Pair(NoteLetter.C, Accidental.SHARP),
            2 to Pair(NoteLetter.D, Accidental.NATURAL),
            3 to Pair(NoteLetter.D, Accidental.SHARP),
            4 to Pair(NoteLetter.E, Accidental.NATURAL),
            5 to Pair(NoteLetter.F, Accidental.NATURAL),
            6 to Pair(NoteLetter.F, Accidental.SHARP),
            7 to Pair(NoteLetter.G, Accidental.NATURAL),
            8 to Pair(NoteLetter.G, Accidental.SHARP),
            9 to Pair(NoteLetter.A, Accidental.NATURAL),
            10 to Pair(NoteLetter.A, Accidental.SHARP),
            11 to Pair(NoteLetter.B, Accidental.NATURAL)
        )
    } else {
        mapOf(
            0 to Pair(NoteLetter.C, Accidental.NATURAL),
            1 to Pair(NoteLetter.D, Accidental.FLAT),
            2 to Pair(NoteLetter.D, Accidental.NATURAL),
            3 to Pair(NoteLetter.E, Accidental.FLAT),
            4 to Pair(NoteLetter.E, Accidental.NATURAL),
            5 to Pair(NoteLetter.F, Accidental.NATURAL),
            6 to Pair(NoteLetter.G, Accidental.FLAT),
            7 to Pair(NoteLetter.G, Accidental.NATURAL),
            8 to Pair(NoteLetter.A, Accidental.FLAT),
            9 to Pair(NoteLetter.A, Accidental.NATURAL),
            10 to Pair(NoteLetter.B, Accidental.FLAT),
            11 to Pair(NoteLetter.B, Accidental.NATURAL)
        )
    }

    val (letter, accidental) = noteMap[noteIndex]
        ?: throw IllegalArgumentException("Invalid note index calculated from MIDI number")

    return Note(
        letter = letter,
        accidental = accidental,
        octave = octave
    )
}

fun String.toNote(): Note {
    val noteString = this
    val cleanedString = noteString.trim()

    val noteRegex = Regex("^([A-Ga-g])([#b♯♭𝄪𝄫]{0,2})(-?\\d+)$")

    val matchResult = noteRegex.matchEntire(cleanedString)
        ?: throw IllegalArgumentException("Invalid note format: '$noteString'")

    val letterStr = matchResult.groupValues[1].uppercase()
    val accidentalStr = matchResult.groupValues[2]
    val octaveStr = matchResult.groupValues[3]

    val noteLetter = NoteLetter.valueOf(letterStr)
    val accidental = when (accidentalStr) {
        "", "♮" -> Accidental.NATURAL
        "#", "♯" -> Accidental.SHARP
        "##", "♯♯", "𝄪" -> Accidental.DOUBLE_SHARP
        "b", "♭" -> Accidental.FLAT
        "bb", "♭♭", "𝄫" -> Accidental.DOUBLE_FLAT
        else -> throw IllegalArgumentException("Invalid accidental: '$accidentalStr'")
    }

    val octave = octaveStr.toInt()

    return Note(
        letter = noteLetter,
        accidental = accidental,
        octave = octave
    )
}