package utilities

import environment.Environment
import environment.action.Action
import environment.state.State
import learning.{QFunction, QMatrix}
import policy.EpsilonGreedy

object Exploration {

		def episode(qMatrix: QMatrix, qFunction: QFunction, maze: Environment, policy: EpsilonGreedy): Unit = {
		println("Start episode")

		var currState: State = maze.getRandomState
		var oldState: State = currState

		do {
			val selected_a: Action = policy.nextAction(currState, qMatrix) // get the next action

			val q = qFunction.value(qMatrix, currState, selected_a) // get the new q-value

			qMatrix.put(currState, selected_a, q) // update the q-matrix

			oldState = currState
			currState = selected_a.act.newState // go to the new state

			policy.printHeadAction()

			println(oldState + " " + selected_a + " - q: " + q)
		} while (!maze.isGoal(currState))
		println("End episode\n")
	}

}
