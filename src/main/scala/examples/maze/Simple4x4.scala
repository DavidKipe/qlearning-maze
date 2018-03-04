package examples.maze

import environment.Environment
import environment.maze.{GridBuilder, MazeGridBuilder}

object Simple4x4 extends MazeDirector {

	override def construct(): Environment = {
		val builder: GridBuilder = new MazeGridBuilder(4, 4)

		builder
			.setWall(1, 0, 1, 1)
			.setWall(2, 1, 2, 2)

			.setPosReward(0, 2)
			.setPosReward(0, 3)
			.setPosReward(2, 1)

			.setNegReward(2, 3)
			.setNegReward(3, 1)

			.setGoalState(0, 0)
			.setStartingState(3, 3)

			.build()
	}

	override def showMaze(): Unit = ???
}
