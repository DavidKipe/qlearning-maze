package learning

import environment.action.Action
import environment.state.State

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class QMatrix {

	private val qValues = mutable.Map[(State, Action), Double]()

	def put(state: State, action: Action)(q: Double): Unit = qValues += (state, action) -> q

	def get(state: State, action: Action): Double = qValues.getOrElse((state, action), .0)

	def getMax(state: State): Double = {
		var max_q = Double.NegativeInfinity

		for (a <- state.getActions) {
			val q = get(state, a)
			if (q > max_q)
				max_q = q
		}

		max_q
	}

	def bestActions(state: State): List[Action] = {
		val bestActions = ListBuffer[Action]()
		var max_q = Double.NegativeInfinity

		for (a <- state.getActions) {
			val q = get(state, a)
			if (q == max_q) bestActions += a
			if (q > max_q) {
				bestActions.clear()
				bestActions += a
				max_q = q
			}
		}

		bestActions.toList
	}

	def bestAction(state: State): Action = {
		var bestAction: Action = null
		var max_q = Double.NegativeInfinity

		for (a <- state.getActions) {
			val q = get(state, a)
			if (q > max_q) {
				bestAction = a
				max_q = q
			}
		}

		bestAction
	}

}
