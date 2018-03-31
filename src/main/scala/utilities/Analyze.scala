package utilities

import environment.Environment
import environment.action.Action
import environment.path.PathLabels
import environment.state.State
import exception.NoSuchPathFound
import learning.QMatrix
import policy.BestDeterministic

object Analyze {

	def bestPath(qMatrix: QMatrix, maze: Environment): Unit = {
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

	def path(qMatrix: QMatrix, path: PathLabels): Unit = {
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

}
