import environment.maze.MazeGridBuilder
import environment.path.PathLabels
import examples.maze.Maze5x6
import learning.{QFunction, QMatrix}
import policy.EpsilonGreedy
import utilities.{Analyze, Exploration}

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .8 // discount factor

	private val epsilon = .2


	def main(args: Array[String]): Unit = {
		var c: Char = '?'
		while (c != 'w' && c != 's') {
			print("Choose the reward bonus [w = weak, s = strong]: ")
			c = scala.io.StdIn.readChar()
		}
		val rewardType = if (c == 'w') MazeGridBuilder.WEAK_REWARD else MazeGridBuilder.STRONG_REWARD

		val mazeDir = Maze5x6

		val maze = mazeDir.construct(rewardType)

		val qMatrix = new QMatrix()
		val qFunction = new QFunction(lRate, dFactor)
		val epsilonGreedy = new EpsilonGreedy(epsilon)

		for (i <- 1 to 5000)
			Exploration.episode(qMatrix, qFunction, maze, epsilonGreedy)

		mazeDir.showMaze()

		Analyze.bestPath(qMatrix, maze)

		println()
		val path9_1 = new PathLabels(9) -> (4,5) -> (4,4) -> (4,3) -> (4,2) -> (3,2) -> (2,2) -> (2,1) -> (2,0) -> (1,0) -> (0,0)
		Analyze.path(qMatrix, path9_1)
		println()
		val path9_2 = new PathLabels(9) -> (4,5) -> (4,4) -> (4,3) -> (3,3) -> (2,3) -> (1,3) -> (1,2) -> (1,1) -> (0,1) -> (0,0)
		Analyze.path(qMatrix, path9_2)
		println()
		val path11 = new PathLabels(11) -> (4,5) -> (4,4) -> (4,3) -> (4,2) -> (3,2) -> (2,2) -> (2,3) -> (1,3) -> (1,2) -> (0,2) -> (0,1) -> (0,0)
		Analyze.path(qMatrix, path11)
	}

}
