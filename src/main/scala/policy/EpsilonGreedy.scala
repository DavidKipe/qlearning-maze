package policy
import environment.action.Action
import environment.state.State
import learning.QMatrix

import scala.util.Random

class EpsilonGreedy(val epsilon: Double) extends ExplorationPolicy {

	private val random = new Random()

	var lastActionIsRandom: Boolean = _

	override def nextAction(state: State, qMatrix: QMatrix): Action = {
		var bestActions: List[Action] = null
		lastActionIsRandom = false

		if (random.nextDouble < epsilon) {
			bestActions = state.getActions
			lastActionIsRandom = true
		}
		else
			bestActions = qMatrix.bestActions(state)

		val random_i: Int = random.nextInt(bestActions.size)
		bestActions(random_i)

		// TODO create exception if no action is present
	}

	def printHeadAction(): Unit = {
		if (lastActionIsRandom)
			print("*   ")
		else
			print("    ")
	}

}
