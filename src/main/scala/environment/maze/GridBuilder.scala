package environment.maze

import environment.Environment

trait GridBuilder {

	def setGoalState(i: Int, j: Int): GridBuilder

	def setStartingState(i: Int, j: Int): GridBuilder

	def setPosReward(i: Int, j: Int): GridBuilder

	def setNegReward(i: Int, j: Int): GridBuilder

	def setWall(i1: Int, j1: Int, i2: Int, j2: Int): GridBuilder

	def build(): Environment

}
