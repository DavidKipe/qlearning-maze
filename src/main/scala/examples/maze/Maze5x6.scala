package examples.maze

import environment.Environment
import environment.maze.{GridBuilder, MazeGridBuilder}

object Maze5x6 extends MazeDirector {

	override def construct(): Environment = {
		val builder: GridBuilder = new MazeGridBuilder(5, 6)

		builder
			.setWall(1, 0, 1, 1)
			.setWall(2, 1, 1, 1)
			.setWall(2, 2, 1, 2)
			.setWall(3, 2, 3, 3)
			.setWall(3, 4, 2, 4)
			.setWall(3, 5, 2, 5)

			.setPosReward(1, 2)
			.setPosReward(2, 3)
			.setPosReward(3, 3)
			.setPosReward(4, 2)

			.setNegReward(0, 3)
			.setNegReward(2, 0)
			.setNegReward(2, 5)
			.setNegReward(3, 1)
			.setNegReward(3, 3)
			.setNegReward(3, 4)
			.setNegReward(3, 5)

			.setGoalState(0, 0)
			.setStartingState(4, 5)

			.build()
	}

	override def showMaze(): Unit = ???
}
