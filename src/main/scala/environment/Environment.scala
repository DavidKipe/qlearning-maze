package environment

import environment.state.State

trait Environment {

	def setFinalState(state: State): Unit

	def isFinal(state: State): Boolean

	def setStartingState(state: State): Unit

	def getStartingState: State

	def getRandomState: State

}
