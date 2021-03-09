typealias Coordinates = Pair<Int, Int>
typealias SeenCoordinates = MutableMap<Char, MutableList<Coordinates>>
typealias SeenMap = MutableMap<Int, SeenCoordinates>
typealias Duplicates = MutableSet<Coordinates>

class TwoDGridBoard(private val size: Int, private val inputBar: InputBar): Board {

    private val cells: Array<CharArray>
    private var seen = Seen()
    private val duplicates = mutableSetOf<Coordinates>()
    private val selectedCell: MutablePair<Int, Int>
    private var inInputBar: Boolean

    init {
        val row = CharArray(size) { '0' }
        cells = Array(size) { row.clone() }

        selectedCell = MutablePair(0, 0)
        inInputBar = false
    }

    override fun setClues(clues: String) {
        for ((index, value) in clues.withIndex()) {

            val row: Int = index / size
            val cell: Int = index % size

            setCell(row, cell, value)
        }

        updateValidity()
    }

    override fun getCells(): List<Cell> {

        val returnCells = mutableListOf<Cell>()

        for ((rowIndex, row) in cells.withIndex()) {

            for ((cellIndex, candidate) in row.withIndex()) {

                val coordinates = Pair(rowIndex, cellIndex)

                val isDuplicate = duplicates.contains(coordinates)
                val isEmpty = candidate == '0'
                val isSelected = coordinates.first == selectedCell.first &&
                                coordinates.second == selectedCell.second
                val isSelectedInput = isSelected && inInputBar

                val cell = Cell(candidate, isDuplicate, isEmpty, isSelected, isSelectedInput)

                returnCells.add(cell)
            }
        }

        return returnCells
    }

    override fun moveUp() {

        if (inInputBar) {
            return
        }

        selectedCell.first--

        if (selectedCell.first < 0) {
            selectedCell.first = 0
        }
    }

    override fun moveRight() {

        if (inInputBar) {
            inputBar.moveRight()
            return
        }

        selectedCell.second++

        if (selectedCell.second > (size - 1)) {
            selectedCell.second = size - 1
        }
    }

    override fun moveDown() {

        if (inInputBar) {
            return
        }

        selectedCell.first++

        if (selectedCell.first > (size - 1)) {
            selectedCell.first = size - 1
        }
    }

    override fun moveLeft() {

        if (inInputBar) {
            inputBar.moveLeft()
            return
        }

        selectedCell.second--

        if (selectedCell.second < 0) {
            selectedCell.second = 0
        }
    }

    override fun select() {

        if (inInputBar) {

            val selectedCandidate = inputBar.select()

            if (selectedCandidate != cells[selectedCell.first][selectedCell.second]) {
                setCell(selectedCell.first, selectedCell.second, selectedCandidate)
                updateValidity()
            }
            return
        }

        inInputBar = true
        inputBar.isActive = true
    }

    override fun back() {
        if (inInputBar) {
            inInputBar = false
            inputBar.isActive = false
            return
        }
    }

    private fun updateValidity() {

        seen.clear()

        for ((rowIndex, row) in cells.withIndex()) {

            for ((cellIndex, candidate) in row.withIndex()) {

                val coordinates = Coordinates(rowIndex, cellIndex)

                seen.rowSeen(rowIndex, candidate, coordinates)
                seen.columnSeen(cellIndex, candidate, coordinates)

                val boxIndex = (cellIndex / 3) + ((rowIndex / 3) * 3)
                seen.boxSeen(boxIndex, candidate, coordinates)
            }
        }

        duplicates.clear()
        seen.duplicates(duplicates)
    }

    private fun setCell(row: Int, cell: Int, value: Char) {
        cells[row][cell] = value
    }
}

class Seen {

    private val rows: SeenMap = mutableMapOf()
    private val columns: SeenMap = mutableMapOf()
    private val blocks: SeenMap = mutableMapOf()

    fun clear() {
        rows.clear()
        columns.clear()
        blocks.clear()
    }

    fun duplicates(duplicates: Duplicates) {

        for (group in arrayOf(rows, columns, blocks)) {
            for ((_, seenCoordinates) in group) {

                for ((_, coordinates) in seenCoordinates) {

                    if (coordinates.size > 1) {
                        duplicates.addAll(coordinates.toSet())
                    }
                }
            }
        }
    }

    fun rowSeen(rowIndex: Int, candidate: Char, coordinates: Coordinates): Boolean {
        return groupSeen(rows, rowIndex, candidate, coordinates)
    }

    fun columnSeen(cellIndex: Int, candidate: Char, coordinates: Coordinates): Boolean {
        return groupSeen(columns, cellIndex, candidate, coordinates)
    }

    fun boxSeen(boxIndex: Int, candidate: Char, coordinates: Coordinates): Boolean {
        return groupSeen(blocks, boxIndex, candidate, coordinates)
    }

    private fun groupSeen(group: SeenMap, index: Int, candidate: Char, coordinates: Coordinates): Boolean {

        if (candidate == '0') {
            return true
        }

        // if we haven't seen this index before, let's allocate
        // storage for it
        if (group[index] == null) {
            group[index] = mutableMapOf()
        }
        val row = group[index]!!

        // if we've seen this candidate before, we'll add the
        // newly seen coordinates
        if (row[candidate] != null) {
            row[candidate]?.add(coordinates)

            return true
        }

        // we're seeing this candidate for the first time
        // let's add it to the list of coordinates
        row[candidate] = mutableListOf(coordinates)

        return false
    }
}

data class MutablePair<T, U>(var first: T, var second: U)
