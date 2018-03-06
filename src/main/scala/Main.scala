import environment.Environment
import environment.action.Action
import environment.maze.MazeGridBuilder
import environment.state.State
import examples.maze.Maze5x6
import learning.{QFunction, QMatrix}
import policy.{BestDeterministic, EpsilonGreedy, ExplorationPolicy}

import scala.collection.mutable.ListBuffer

object Main {

	private val lRate = .8 // learning rate

	private val dFactor = .9 // discount factor

	private val epsilon = .2

	private def episode(qMatrix: QMatrix, qFunction: QFunction, maze: Environment, policy: ExplorationPolicy): Unit = {
		var greedy: Boolean = false

		policy match {
			case _: EpsilonGreedy => greedy = true
			case _: BestDeterministic => greedy = false
			case _ => // TODO error: policy did not manage yet
		}

		println("Start episode")

		var oldState: State = null
		var currState: State = if (greedy) maze.getRandomState else maze.getStartingState

		do {
			val selected_a: Action = policy.nextAction(currState, qMatrix)

			var q = .0
			//if (greedy)
			q = qFunction.update(qMatrix, currState, selected_a) // updating the Q matrix

			oldState = currState
			currState = selected_a.act.newState

			if (greedy)
				policy.asInstanceOf[EpsilonGreedy].printHeadAction()

			print(oldState + " " + selected_a)
			/*if (greedy)*/ print(" - q: " + q)
			println()
		} while (!maze.isGoal(currState))
		println("End episode\n")
	}

	def analyzeBestPath(qMatrix: QMatrix, maze: Environment): Unit = {
		val policy = new BestDeterministic()

		var oldState: State = null
		var currState: State = maze.getStartingState

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

	def analyzePath(qMatrix: QMatrix, maze: Environment, path: PathLabels): Unit = {
		var nStep = 0
		var qSum = 0.0

		println("Path: " + path)

		val pathIterator = path.iterator

		var fromLabel = pathIterator.next()

		for (label <- pathIterator) {
			qSum += qMatrix.getByLabel(fromLabel, label)
			nStep += 1
			fromLabel = label
		}

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
			episode(qMatrix, qFunction, maze, epsilonGreedy)

		mazeDir.showMaze()

		analyzeBestPath(qMatrix, maze)

		println()
		val path9 = new PathLabels() -> (4, 5) -> (4, 4) -> (4, 3) -> (4, 2) -> (3, 2) -> (2, 2) -> (2, 1) -> (2, 0) -> (1, 0) -> (0, 0)
		analyzePath(qMatrix, maze, path9)
		println()
		val path11 = new PathLabels() -> (4, 5) -> (4, 4) -> (4, 3) -> (4, 2) -> (3, 2) -> (2, 2) -> (2, 3) -> (1, 3) -> (1, 2) -> (0, 2) -> (0, 1) -> (0, 0)
		analyzePath(qMatrix, maze, path11)
	}

	private class PathLabels extends Iterable[String] {

		private val path = ListBuffer[(Int, Int)]()

		def ->(i: Int, j: Int): PathLabels = {
			path += ((i, j))
			this
		}

		def numberOfSteps: Int = path.size

		private def createLabel(i: Int, j: Int): String = "(" + i + "," + j + ")"

		override def iterator: Iterator[String] = path.map(pair => createLabel(pair._1, pair._2)).iterator

		override def toString: String = {
			val strPath: StringBuilder = new StringBuilder()
			var i = 0
			val steps = numberOfSteps
			for (label <- this) {
				i += 1
				strPath ++= label
				if (i < steps)
					strPath ++= " -> "
			}
			strPath.toString
		}
	}

}
