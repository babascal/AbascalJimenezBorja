
package cat.copernic.babascal.abascaljimenezborja

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import cat.copernic.babascal.abascaljimenezborja.databinding.MainFragmentBinding
import java.lang.Thread.sleep
import kotlin.random.Random

class MainFragment : Fragment() {
    private lateinit var binding: MainFragmentBinding
    private var controlPlayer = true
    private var gameOver = false
    private var contador = 0
    private lateinit var grid: Array<Array<CharSequence>>

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = MainFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.button1.setOnClickListener {
            onClick(binding.button1)
        }
        binding.button2.setOnClickListener {
            onClick(binding.button2)
        }
        binding.button3.setOnClickListener {
            onClick(binding.button3)
        }
        binding.button4.setOnClickListener {
            onClick(binding.button4)
        }
        binding.button5.setOnClickListener {
            onClick(binding.button5)
        }
        binding.button6.setOnClickListener {
            onClick(binding.button6)
        }
        binding.button7.setOnClickListener {
            onClick(binding.button7)
        }
        binding.button8.setOnClickListener {
            onClick(binding.button8)
        }
        binding.button9.setOnClickListener {
            onClick(binding.button9)
        }

    }


    fun onClick(button: Button) {
        if (!gameOver) {
            button.text = "X"
            //Se deshabilita el botón
            button.isClickable = false
            //Sumamos 1 al contador de tiradas
            contador++

            updateGrid()
            computerMove()
            updateGrid()

            if (checkVictory()) {
                //controlPlayer = true implica que el jugador 2 acaba de hacer click en el botón
                if (controlPlayer) {
                    binding.textView1.text = "El jugador 2 ha ganado!"
                } else {
                    binding.textView1.text = "El jugador 1 ha ganado!"
                }
                gameOver = true;

            } else if (contador > 9) {
                binding.textView1.text = "ES UN EMPATE!"
                gameOver = true
            }
        }
    }


    fun checkVictory(): Boolean {
        var check = false

        for (j in 0..2) {
            if (grid[j][0].isNotBlank() && grid[j][0] == grid[j][1] && grid[j][1] == grid[j][2]) {
                check = true
            } else if (grid[0][j].isNotBlank() && grid[0][j] == grid[1][j] && grid[1][j] == grid[2][j]) {
                check = true
            }
        }

        if ((grid[0][0].isNotBlank() && grid[0][0] == grid[1][1] && grid[1][1] == grid[2][2]) ||
            (grid[0][2].isNotBlank() && grid[0][2] == grid[1][1] && grid[1][1] == grid[2][0])) {
            check = true;
        }

        return check
    }

    fun computerMove () {
        var movesScore = arrayOf(arrayOf(3,2,3), arrayOf(2,4,2), arrayOf(3,2,3))
        for (j in 0..2) {
            for (i in 0..2) {
                if (grid[j][i].isNotBlank()) {
                    movesScore[j][i] = 0
                }
                if (moveBlocks(j,i) && movesScore[j][i] != 0) {
                    movesScore[j][i] = movesScore[j][i] + 50
                }

                if (moveWins(j, i, grid) && movesScore[j][i] != 0) {
                    movesScore[j][i] = movesScore[j][i] + 100
                }

            }
        }

        val move: Pair<Int,Int> = optimalMove(movesScore)

        if (move.first != -1) {
            if (move.first == 0) {
                when (move.second) {
                    0 -> binding.button1.text = "O"
                    1 -> binding.button2.text = "O"
                    2 -> binding.button3.text = "O"
                }
            }
            if (move.first == 1) {
                when (move.second) {
                    0 -> binding.button4.text = "O"
                    1 -> binding.button5.text = "O"
                    2 -> binding.button6.text = "O"
                }
            }
            if (move.first == 2) {
                when (move.second) {
                    0 -> binding.button7.text = "O"
                    1 -> binding.button8.text = "O"
                    2 -> binding.button9.text = "O"
                }
            }
        }

        contador++
    }

    fun optimalMove (moves : Array<Array<Int>>): Pair<Int,Int> {
        var indexA: Int = 0
        var indexB: Int = 0
        var score : Int = 0
        var check = false

        for (j in 0..2) {
            for (i in 0..2) {
                if (moves[j][i] != 0 && moves[j][i] > score) {
                    indexA = j
                    indexB = i
                    score = moves[j][i]
                    check = true
                }
            }

        }

        if (check) {
            return Pair(indexA,indexB)
        } else {
            return Pair(-1,-1)
        }
    }


    fun moveBlocks (indexA: Int, indexB : Int): Boolean {
        if (indexA == indexB) {
            if (indexA == 0) {
                if (grid[1][1] == grid[2][2] && grid[1][1] == "X") {
                    return true
                }
            }
            if (indexA == 1) {
                if (grid[0][0] == grid[2][2] && grid[0][0] == "X") {
                    return true
                }
            }
            if (indexA == 2) {
                if (grid[0][0] == grid[1][1] && grid[0][0] == "X") {
                    return true
                }
            }
        }

        if (indexA == 0) {
            if (grid[1][indexB] == grid[2][indexB] && grid[1][indexB] == "X") {
                return true
            }
        }
        if (indexA == 1) {
            if (grid[0][indexB] == grid[2][indexB] && grid[0][indexB] == "X") {
                return true
            }
        }
        if (indexA == 2) {
            if (grid[0][indexB] == grid[1][indexB] && grid[0][indexB] == "X") {
                return true
            }
        }

        if (indexB == 0) {
            if (grid[indexA][1] == grid[indexA][2] && grid[indexA][1] == "X") {
                return true
            }
        }
        if (indexB == 1) {
            if (grid[indexA][0] == grid[indexA][2] && grid[indexA][0] == "X") {
                return true
            }
        }
        if (indexB == 2) {
            if (grid[indexA][0] == grid[indexA][1] && grid[indexA][0] == "X") {
                return true
            }
        }

        return false
    }

    fun moveWins (indexA: Int, indexB : Int, board: Array<Array<CharSequence>>): Boolean {
        if (indexA == indexB) {
            if (indexA == 0) {
                if (board[1][1] == board[2][2] && board[1][1] == "O") {
                    return true
                }
            }
            if (indexA == 1) {
                if (board[0][0] == board[2][2] && board[0][0] == "O") {
                    return true
                }
            }
            if (indexA == 2) {
                if (board[0][0] == board[1][1] && board[0][0] == "O") {
                    return true
                }
            }
        }

        if (indexA == 0) {
            if (board[1][indexB] == board[2][indexB] && board[1][indexB] == "O") {
                return true
            }
        }
        if (indexA == 1) {
            if (board[0][indexB] == board[2][indexB] && board[0][indexB] == "O") {
                return true
            }
        }
        if (indexA == 2) {
            if (board[0][indexB] == board[1][indexB] && board[0][indexB] == "O") {
                return true
            }
        }

        if (indexB == 0) {
            if (board[indexA][1] == board[indexA][2] && board[indexA][1] == "O") {
                return true
            }
        }
        if (indexB == 1) {
            if (board[indexA][0] == board[indexA][2] && board[indexA][0] == "O") {
                return true
            }
        }
        if (indexB == 2) {
            if (board[indexA][0] == board[indexA][1] && board[indexA][0] == "O") {
                return true
            }
        }

        return false
    }

    /*fun onClick(button: Button) {
        var computerButton: Button

            button.text = "X"
            //Se deshabilita el botón
            button.isClickable = false
            //Sumamos 1 al contador de tiradas
            contador++

            var move = computerMove(grid)

            computerButton = buttonFromPair(move)
            computerButton.text = "O"
            computerButton.isClickable = false
            contador++


    }
    */


    fun updateGrid() {
        grid = arrayOf(
            arrayOf(binding.button1.text,binding.button2.text,binding.button3.text),
            arrayOf(binding.button4.text,binding.button5.text,binding.button6.text),
            arrayOf(binding.button7.text,binding.button8.text,binding.button9.text)
        )
    }


}