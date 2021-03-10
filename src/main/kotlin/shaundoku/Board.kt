interface Board {
    fun setClues(clues: String)
    fun getCells(): List<Cell>
    fun moveUp()
    fun moveRight()
    fun moveDown()
    fun moveLeft()
    fun select()
    fun back()
}

data class Cell(
    val Candidate: Char,
    val isDuplicate: Boolean,
    val isEmpty: Boolean,
    val isSelected: Boolean,
    val isSelectedInput: Boolean,
)
