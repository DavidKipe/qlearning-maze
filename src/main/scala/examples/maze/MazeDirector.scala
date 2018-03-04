package examples.maze

import environment.Environment

trait MazeDirector {

	def construct(): Environment

	def showMaze(): Unit

}
