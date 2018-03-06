package learning

import environment.Transition
import environment.action.Action
import environment.state.{BasicState, State}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class QMatrix {

	private val qValues = mutable.Map[(State, State), Double]()

	def put(state: State, newState: State, q: Double): Unit = qValues += (state, newState) -> q

	def put(state: State, transition: Transition, q: Double): Unit = put(state, transition.newState, q)

	def put(state: State, action: Action, q: Double): Unit = put(state, action.act, q)

	def get(state: State, newState: State): Double = qValues.getOrElse((state, newState), .0)

	def get(state: State, transition: Transition): Double = get(state, transition.newState)

	def get(state: State, action: Action): Double = get(state, action.act)

	def getByLabel(fromLabel: String, toLabel: String): Double = get(new BasicState(fromLabel), new BasicState(toLabel))

	def getMax(state: State): Double = {
		var max_q = Double.NegativeInfinity

		for (a <- state.getActions) {
			val q = get(state, a)
			if (q > max_q)
				max_q = q
		}

		max_q
	}

	def bestActions(state: State): Seq[Action] = {
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

		bestActions
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
