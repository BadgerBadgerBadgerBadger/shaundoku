import processing.core.PApplet

enum class Candidate(val candidate: Char) {
    ZERO('0'),
    ONE('1'),
    TWO('2'),
    THREE('3'),
    FOUR('4'),
    FIVE('5'),
    SIX('6'),
    SEVEN('7'),
    EIGHT('8'),
    NINE('9'),
}

class InputBar {

    private val drawer: PApplet
    private val cellSize: Float
    private val cellColors: CellColors
    var isActive = false

    constructor(drawer: PApplet, cellSize: Float, cellColors: CellColors) {
        this.drawer = drawer
        this.cellSize = cellSize
        this.cellColors = cellColors
    }

    val candidates = arrayOf(
        Candidate.ZERO,
        Candidate.ONE,
        Candidate.TWO,
        Candidate.THREE,
        Candidate.FOUR,
        Candidate.FIVE,
        Candidate.SIX,
        Candidate.SEVEN,
        Candidate.EIGHT,
        Candidate.NINE,
    )

    var current = 0

    fun draw() {

        for ((index, candidate) in candidates.withIndex()) {

            val xPos = index * cellSize
            val yPos = 0f
            val strokeWeight = if (isActive && index == current) {
                4f
            } else {
                1f
            }

            drawer.fill(
                cellColors.background.first,
                cellColors.background.second,
                cellColors.background.third,
            )
            drawer.strokeWeight(strokeWeight)
            drawer.square(xPos, yPos, cellSize)

            if (candidate == Candidate.ZERO) {
                continue
            }

            drawer.fill(
                cellColors.text.first,
                cellColors.text.second,
                cellColors.text.third,
            )
            drawer.text(candidate.candidate, xPos + cellSize / 3, yPos + (cellSize / 1.5).toInt())
        }
    }

    fun moveLeft() {
        current--

        if (current < 0) {
            current = 0
        }
    }

    fun moveRight() {

        current++

        if (current >= candidates.size) {
            current = candidates.size - 1
        }
    }

    fun select(): Char {
        return candidates[current].candidate
    }
}