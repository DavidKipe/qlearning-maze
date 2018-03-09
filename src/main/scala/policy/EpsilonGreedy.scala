package policy
import environment.action.Action
import environment.state.State
import exception.NoActionFound
import learning.QMatrix

import scala.util.Random

class EpsilonGreedy(val epsilon: Double) extends ExplorationPolicy {

	require(epsilon > 0.0 && epsilon < 1.0, "Epsilon value must be in interval (0.0, 1.0)")

	private val random = new Random()

	private var _isLastActionRandom: Boolean = _

	def isLastActionRandom: Boolean = _isLastActionRandom

	override def nextAction(state: State, qMatrix: QMatrix): Action = {
		var bestActions: Seq[Action] = Seq.empty
		_isLastActionRandom = false

		if (random.nextDouble < epsilon) {
			bestActions = state.getActions
			_isLastActionRandom = true
		}
		else
			bestActions = qMatrix.bestActions(state)

		if (bestActions == null || bestActions.isEmpty)
			throw new NoActionFound(state, "Failed to found the next action")

		val random_i: Int = random.nextInt(bestActions.size)
		bestActions(random_i)
	}

	def printHeadAction(): Unit = {
		if (_isLastActionRandom)
			print("*   ")
		else
			print("    ")
	}

}
