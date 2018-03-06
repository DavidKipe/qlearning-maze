package examples.maze

import environment.Environment

trait MazeDirector {

	def construct(rewardBonus: Int): Environment

	def showMaze(): Unit

}
