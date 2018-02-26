package learning

import environment.Transition
import environment.action.Action
import environment.state.State

class QFunction(val learningRate: Double, val discountFactor: Double) {

	def update(qMatrix: QMatrix, state: State, transition: Transition): Double = {
		val newState = transition.newState

		val oldValue = qMatrix.get(state, newState)
		val learnedValue = transition.reward + (discountFactor * qMatrix.getMax(newState))

		val qValue = ((1.0 - learningRate) * oldValue) + (learningRate * learnedValue)

		qMatrix.put(state, newState, qValue)
		qValue
	}

	def update(qMatrix: QMatrix, state: State, action: Action): Double = update(qMatrix, state, action.act)
}
