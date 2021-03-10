package shaundoku

import Board
import BoardRenderer
import CellColors
import Color
import InputBar
import TwoDGridBoard
import processing.core.PApplet

// TODO: Are non-primitive constants possible?
private val colorCellBackground = Color(242f, 236f, 201f)
private val colorCellDarkBackground = Color(212f, 206f, 181f)
private val colorCandidateText = Color(0f, 0f, 0f)
private val colorCandidateTextDuplicate = Color(200f, 0f, 0f)

class Shaundoku(private val clues: String) : PApplet() {

    private val screenSize = 600
    private val colorClear = 255

    // input bar will have all valid candidates plus
    // empty candidate, so cells need to be small enough
    // for them to all fit in the screen width
    private val boardSize = 9
    private val cellSize = screenSize / (boardSize + 1f)
    private val boardBoundingBoxSize = cellSize * boardSize
    private val leftMargin = (screenSize - boardBoundingBoxSize) / 2
    private val boardStart = Pair(leftMargin, leftMargin)

    private val cellColors = CellColors(
        colorCellBackground,
        colorCellDarkBackground,
        colorCandidateText,
        colorCandidateTextDuplicate,
    )

    private val inputBar = InputBar(this, cellSize, cellColors)
    private val board: Board = TwoDGridBoard(boardSize, inputBar)
    private val boardRenderer = BoardRenderer(this, cellSize, boardSize, cellColors)

    override fun settings() {

        // let's give enough room for a cell's worth of blank space
        // before and after the input bar
        this.size(screenSize, screenSize + (cellSize.toInt() * 3))
    }

    override fun setup() {
        board.setClues(this.clues)
    }

    override fun draw() {

        this.background(colorClear)

        this.translate(boardStart.first, boardStart.second)
        boardRenderer.draw(board)

        // we allow for a cell's width of empty space and then
        // draw the input bar
        this.translate(-leftMargin, boardStart.second + boardBoundingBoxSize + cellSize)
        inputBar.draw()
    }

    override fun keyPressed() {
        when (keyCode) {
            UP -> board.moveUp()
            RIGHT -> board.moveRight()
            DOWN -> board.moveDown()
            LEFT -> board.moveLeft()
        }
        when (key) {
            ENTER -> board.select()
            BACKSPACE -> board.back()
        }
    }

    companion object Factory {
        fun run(clues: String) {
            val shaundoku = Shaundoku(clues)
            shaundoku.setSize(500, 500)
            shaundoku.runSketch()
        }
    }
}

fun main() {

    // hardcoded till a better mechanism is found
    val input = "535070000600195000098000060800060003400803001700020006060000280000419005000080079"

    Shaundoku.run(input)
}