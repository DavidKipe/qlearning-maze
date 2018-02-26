import environment.action.Action
import environment.maze.MazeGridBuilder
import environment.state.State
import learning.{QFunction, QMatrix}

import scala.util.Random

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .8 // discount factor

	private var epsilon = .2

	private def episode(qMatrix: QMatrix, qFunction: QFunction, init: State): Unit = {
		println("Start episode")

		var oldState: State = null
		var currState: State = init

		val random = new Random()

		do {
			val possible_actions = currState.getActions
			var bestActions: List[Action] = null

			if (random.nextDouble < epsilon) { // epsilon greedy
				bestActions = possible_actions
				print(" * ")
			}
			else
				bestActions = qMatrix.bestActions(currState)

			val random_i: Int = random.nextInt(bestActions.size)
			val selected_a: Action = bestActions(random_i)

			val q = qFunction.update(qMatrix, currState, selected_a) // updating the Q matrix

			oldState = currState
			currState = selected_a.act.newState

			println(oldState + " " + selected_a + " - q: " + q)
		} while (!(currState.toString == "(0,0)")) // TODO make a better condition
		println("End episode\n")
	}

	def main(args: Array[String]): Unit = {
		val mg = new MazeGridBuilder(4, 4)

		val qMatrix = new QMatrix()
		val qFunction = new QFunction(lRate, dFactor)

		val init = mg.getInit

		for (i <- 1 to 1000)
			episode(qMatrix, qFunction, mg.getRandomState)

		println("Best path:")
		epsilon = 0.0 // no more exploration
		episode(qMatrix, qFunction, init) // start from initial point // TODO optimize this for best path, create ad hoc method
	}

}
