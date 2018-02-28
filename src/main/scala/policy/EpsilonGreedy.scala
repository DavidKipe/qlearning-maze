package policy
import environment.action.Action
import environment.state.State
import learning.QMatrix

import scala.util.Random

class EpsilonGreedy(val epsilon: Double) extends ExplorationPolicy {

	private val random = new Random()

	override def nextAction(state: State, qMatrix: QMatrix): Action = {
		var bestActions: List[Action] = null

		if (random.nextDouble < epsilon) {
			bestActions = state.getActions
			print(" * ")
		}
		else
			bestActions = qMatrix.bestActions(state)

		val random_i: Int = random.nextInt(bestActions.size)
		bestActions(random_i)
	}

}
