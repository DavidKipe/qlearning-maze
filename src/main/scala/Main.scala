import environment.action.Action
import environment.maze.MazeGridBuilder
import environment.state.State
import learning.{QFunction, QMatrix}
import policy.{BestDeterministic, EpsilonGreedy, ExplorationPolicy}

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .8 // discount factor

	private val epsilon = .2

	private def episode(qMatrix: QMatrix, qFunction: QFunction, policy: ExplorationPolicy, init: State): Unit = {
		println("Start episode")

		var oldState: State = null
		var currState: State = init

		do {
			val selected_a: Action = policy.nextAction(currState, qMatrix)

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
		val epsilonGreedy = new EpsilonGreedy(epsilon)

		val init = mg.getInit

		for (i <- 1 to 1000)
			episode(qMatrix, qFunction, epsilonGreedy, mg.getRandomState)

		println("Best path:")
		episode(qMatrix, qFunction, new BestDeterministic, init) // start from initial point // TODO optimize this for best path, create ad hoc method (not an episode, we don't need to update the qmatrix)
	}

}
