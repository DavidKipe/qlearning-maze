package learning

import environment.action.Action
import environment.state.State

class QFunction(val learningRate: Double, val discountFactor: Double) {

	def update(qMatrix: QMatrix)(state: State, action: Action): Double = {
		val newState = action.act

		val oldValue = qMatrix.get(state, action)
		val learnedValue = newState.getReward + (discountFactor * qMatrix.getMax(newState))

		val qValue = ((1.0 - learningRate) * oldValue) + (learningRate * learnedValue)

		qMatrix.put(state, action)(qValue)
		qValue
	}
}
