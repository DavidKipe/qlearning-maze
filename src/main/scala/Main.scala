import environment.Environment
import environment.action.Action
import environment.state.State
import examples.maze.Maze5x6
import learning.{QFunction, QMatrix}
import policy.{BestDeterministic, EpsilonGreedy, ExplorationPolicy}

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .8 // discount factor

	private val epsilon = .2

	private def episode(qMatrix: QMatrix, qFunction: QFunction, maze: Environment, policy: ExplorationPolicy): Unit = {
		/* TODO create ad hoc methods for exploration and best path
		*  for best path visualize the q values without update q-matrix
		*  and perform the average reward of the path
		*  Furthermore, adding a comparison with other path(s)
		* */
		var greedy: Boolean = false

		policy match {
			case _: EpsilonGreedy => greedy = true
			case _: BestDeterministic => greedy = false
			case _ => // TODO error: policy did not manage yet
		}

		println("Start episode")

		var oldState: State = null
		var currState: State = if (greedy) maze.getRandomState else maze.getStartingState

		do {
			val selected_a: Action = policy.nextAction(currState, qMatrix)

			var q = .0
			//if (greedy)
				q = qFunction.update(qMatrix, currState, selected_a) // updating the Q matrix

			oldState = currState
			currState = selected_a.act.newState

			if (greedy)
				policy.asInstanceOf[EpsilonGreedy].printHeadAction()

			print(oldState + " " + selected_a)
			/*if (greedy)*/ print(" - q: " + q)
			println()
		} while (!maze.isGoal(currState))
		println("End episode\n")
	}

	def main(args: Array[String]): Unit = {
		//val maze = Simple4x4.construct()
		val maze = Maze5x6.construct()

		val qMatrix = new QMatrix()
		val qFunction = new QFunction(lRate, dFactor)
		val epsilonGreedy = new EpsilonGreedy(epsilon)

		for (i <- 1 to 1000)
			episode(qMatrix, qFunction, maze, epsilonGreedy)

		println("Best path:")
		episode(qMatrix, qFunction, maze, new BestDeterministic())
	}

}
