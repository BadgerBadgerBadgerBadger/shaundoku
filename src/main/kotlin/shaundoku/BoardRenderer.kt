import processing.core.PApplet

typealias Color = Triple<Float, Float, Float>

class BoardRenderer(
    private val drawer: PApplet,
    private val cellSize: Float,
    private val boardSize: Int,
    private val cellColors: CellColors
) {

    fun draw(board: Board) {

        // trial and error figures 2/5ths is a nice text size
        // relative to the cell size
        drawer.textSize(cellSize * (2f/5f))
        drawer.strokeWeight(0.5f)

        for ((index, cell) in board.getCells().withIndex()) {

            val rowIndex = index / boardSize
            val cellIndex = index % boardSize

            val xPos = cellIndex * cellSize
            val yPos = rowIndex * cellSize

            val strokeWeight = if (cell.isSelected) {
                4f
            } else {
                1f
            }

            val backgroundColor = if (cell.isSelectedInput) {
                cellColors.darkBackground
            } else {
                cellColors.background
            }

            // draw the cell walls and background
            drawer.fill(
                backgroundColor.first,
                backgroundColor.second,
                backgroundColor.third
            )
            drawer.strokeWeight(strokeWeight)
            drawer.rect(xPos, yPos, cellSize, cellSize)

            // we don't display empty cell contents
            if (cell.isEmpty) {
                continue
            }

            val textColor = if (cell.isDuplicate) {
                cellColors.duplicate
            } else {
                cellColors.text
            }

            // positioning values figured via trial & error
            drawer.fill(textColor.first, textColor.second, textColor.third)
            drawer.text(cell.Candidate, xPos + cellSize / 3, yPos + (cellSize / 1.5).toInt())
        }

        // draw the thicker lines that delineate blocks in the puzzle
        drawer.strokeWeight(2f)
        drawer.line(cellSize * 3, 1f, cellSize * 3, (cellSize * 9) - 1)
        drawer.line(cellSize * 6, 1f, cellSize * 6, (cellSize * 9) - 1)
        drawer.line(0f, cellSize * 3, cellSize * 9, cellSize * 3)
        drawer.line(0f, cellSize * 6, cellSize * 9, cellSize * 6)
    }
}

data class CellColors(
    val background: Color,
    val darkBackground: Color,
    val text: Color,
    val duplicate: Color,
)