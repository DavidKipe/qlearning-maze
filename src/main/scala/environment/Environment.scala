package environment

import environment.state.State

trait Environment {

	def isGoal(state: State): Boolean

	def getStartingState: State

	def getRandomState: State

}
