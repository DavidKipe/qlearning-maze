import environment.Environment
import environment.action.Action
import environment.maze.MazeGridBuilder
import environment.path.PathLabels
import environment.state.State
import examples.maze.Maze5x6
import exception.NoSuchPathFound
import learning.{QFunction, QMatrix}
import policy.{BestDeterministic, EpsilonGreedy}

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .9 // discount factor

	private val epsilon = .2

	private def explorationEpisode(qMatrix: QMatrix, qFunction: QFunction, maze: Environment, policy: EpsilonGreedy): Unit = {
		println("Start episode")

		var currState: State = maze.getRandomState
		var oldState: State = currState

		do {
			val selected_a: Action = policy.nextAction(currState, qMatrix)

			val q = qFunction.update(qMatrix, currState, selected_a) // updating the Q matrix

			oldState = currState
			currState = selected_a.act.newState

			policy.asInstanceOf[EpsilonGreedy].printHeadAction()

			println(oldState + " " + selected_a + " - q: " + q)
		} while (!maze.isGoal(currState))
		println("End episode\n")
	}

	def analyzeBestPath(qMatrix: QMatrix, maze: Environment): Unit = {
		val policy = new BestDeterministic()

		var currState: State = maze.getStartingState
		var oldState: State = currState

		var nStep = 0
		var qSum = 0.0

		print("Best path: ")
		do {
			print(currState + " -> ")

			val selected_a: Action = policy.nextAction(currState, qMatrix)

			oldState = currState
			currState = selected_a.act.newState

			qSum += qMatrix.get(oldState, currState)

			nStep += 1
		} while (!maze.isGoal(currState))
		println(currState)

		val (x, y) = maze.gridSize
		println("Minimum number of steps: " + (x + y - 2))
		println("Number of steps: " + nStep)
		println("Average of reward bonus: " + (qSum / nStep))
	}

	def analyzePath(qMatrix: QMatrix, path: PathLabels): Unit = {
		var qSum = 0.0

		println("Path: " + path)

		val pathIterator = path.iterator
		var fromLabel = pathIterator.next()

		for (label <- pathIterator) {
			val q = qMatrix.getByLabel(fromLabel, label)
			if (q == 0.0)
				throw new NoSuchPathFound((fromLabel, label), "No Q-value found for such action in the given Q-matrix")
			qSum += q

			fromLabel = label
		}

		val nStep = path.numberOfSteps
		println("Number of steps: " + nStep)
		println("Average of reward bonus: " + (qSum / nStep))
	}

	def main(args: Array[String]): Unit = {
		val mazeDir = Maze5x6

		val maze = mazeDir.construct(MazeGridBuilder.STRONG_REWARD)

		val qMatrix = new QMatrix()
		val qFunction = new QFunction(lRate, dFactor)
		val epsilonGreedy = new EpsilonGreedy(epsilon)

		for (i <- 1 to 1000)
			explorationEpisode(qMatrix, qFunction, maze, epsilonGreedy)

		mazeDir.showMaze()

		analyzeBestPath(qMatrix, maze)

		println()
		val path9 = new PathLabels(9) -> (4, 5) -> (4, 4) -> (4, 3) -> (4, 2) -> (3, 2) -> (2, 2) -> (2, 1) -> (2, 0) -> (1, 0) -> (0, 0)
		analyzePath(qMatrix, path9)
		println()
		val path11 = new PathLabels(11) -> (4, 5) -> (4, 4) -> (4, 3) -> (4, 2) -> (3, 2) -> (2, 2) -> (2, 3) -> (1, 3) -> (1, 2) -> (0, 2) -> (0, 1) -> (0, 0)
		analyzePath(qMatrix, path11)
	}


}
