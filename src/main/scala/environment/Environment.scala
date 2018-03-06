package environment

import environment.state.State

trait Environment {

	def gridSize: (Int, Int)

	def isGoal(state: State): Boolean

	def getStartingState: State

	def getRandomState: State

}
