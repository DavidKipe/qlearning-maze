package environment.maze

import environment.action.{Action, BasicAction}
import environment.state.{BasicState, State}

import scala.collection.mutable.ListBuffer
import scala.util.Random

class MazeGridBuilder(val x: Int, val y: Int) {

	protected val grid: Array[Array[BasicState]] = Array.ofDim[BasicState](x, y)


	{
		var actionMap = Map[State, Action]() // cache for re-use the actions
	val actionGrid = Array.ofDim[ListBuffer[Action]](x, y) // store all the actions

		def getOrNone(i: Int, j: Int): Option[BasicState] = {
			try {
				Option(grid(i)(j))
			} catch {
				case _: ArrayIndexOutOfBoundsException => None
			}
		}

		def createDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = {
			val s1 = grid(s1_i)(s1_j)
			val s2 = grid(s2_i)(s2_j)
			var to_s1 = actionMap.get(s1)
			var to_s2 = actionMap.get(s2)

			if (to_s1 isEmpty) {
				to_s1 = Option(new BasicAction("to->" + s1, s1))
				actionMap += (s1 -> to_s1.get)
			}
			if (to_s2 isEmpty) {
				to_s2 = Option(new BasicAction("to->" + s2, s2))
				actionMap += (s2 -> to_s2.get)
			}

			actionGrid(s1_i)(s1_j) += to_s2.get
			actionGrid(s2_i)(s2_j) += to_s1.get
		}

		def deleteDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = {
			val s1 = grid(s1_i)(s1_j)
			val s2 = grid(s2_i)(s2_j)
			val to_s1 = actionMap.get(s1)
			val to_s2 = actionMap.get(s2)
			actionGrid(s1_i)(s1_j) -= to_s2.get
			actionGrid(s2_i)(s2_j) -= to_s1.get
		}

		for (i <- 0 until x; j <- 0 until y) { // create all the states in the grid
			grid(i)(j) = new BasicState("(" + i + "," + j + ")")
		}

		for (i <- 0 until x; j <- 0 until y) { // initialize all the action lists in the grid
			actionGrid(i)(j) = ListBuffer[Action]()
		}

		for (i <- 0 to x; j <- 0 to y) { // create all links
			val underneath = getOrNone(i, j + 1)
			val right = getOrNone(i + 1, j)

			if (underneath nonEmpty)
				createDoubleLink(i, j, i, j + 1)
			if (right nonEmpty)
				createDoubleLink(i + 1, j, i, j)
		}

		/*deleteDoubleLink(1, 0, 1, 2)
		deleteDoubleLink(2, 1, 2, 2)*/

		deleteDoubleLink(1, 1, 2, 1)
		deleteDoubleLink(2, 2, 3, 2)

		for (i <- 0 until x; j <- 0 until y) { // setting of the all actions
			grid(i)(j).setActions(actionGrid(i)(j).toList)
		}

		/* setting the rewards */

		grid(0)(0).setReward(100) // Goal

		val positive_reward = 20
		val nagative_reward = -2 * positive_reward

		// Note: positive reward must be less than (('discount factor' * 'goal reward') * (1 - 'discount factor'))
		// Negative reward must not be analysed in deep yet

		/*grid(0)(2).setReward(positive_reward)
		grid(0)(3).setReward(positive_reward)
		grid(2)(1).setReward(positive_reward)

		grid(2)(3).setReward(nagative_reward)
		grid(3)(1).setReward(nagative_reward)*/

		/*grid(0)(2).setReward(positive_reward)
		grid(0)(3).setReward(positive_reward)
		grid(2)(1).setReward(positive_reward)
		grid(1)(1).setReward(positive_reward)

		grid(2)(3).setReward(nagative_reward)
		grid(2)(2).setReward(nagative_reward)*/

		grid(1)(0).setReward(positive_reward)
		grid(1)(1).setReward(positive_reward)
		grid(1)(3).setReward(positive_reward)
		grid(2)(2).setReward(positive_reward)

		grid(1)(2).setReward(nagative_reward)
		grid(3)(0).setReward(nagative_reward)
	}

	def getInit: BasicState = grid(x - 1)(y - 1)

	def getRandomState: BasicState = {
		val random = new Random()
		val i = random.nextInt(x)
		val j = random.nextInt(y)

		grid(i)(j)
	}

}
