package environment.maze

import environment.action.{Action, BasicAction}
import environment.state.BasicState

import scala.collection.mutable.ListBuffer
import scala.util.Random

class MazeGridBuilder(val x: Int, val y: Int) {

	protected val grid: Array[Array[BasicState]] = Array.ofDim[BasicState](x, y)


	{
		val actionCache = Array.ofDim[Option[Action]](x, y) // cache for re-use the actions
		val actionGrid = Array.ofDim[ListBuffer[Action]](x, y) // store all the actions in the right grid position

		def getOrNone(i: Int, j: Int): Option[BasicState] = {
			try {
				Option(grid(i)(j))
			} catch {
				case _: ArrayIndexOutOfBoundsException => None
			}
		}

		def createDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = {
			var to_s1 = actionCache(s1_i)(s1_j)
			var to_s2 = actionCache(s2_i)(s2_j)

			if (to_s1 isEmpty) {
				val s1 = grid(s1_i)(s1_j)
				to_s1 = Option(new BasicAction("to->" + s1, s1))
				actionCache(s1_i)(s1_j) = to_s1
			}
			if (to_s2 isEmpty) {
				val s2 = grid(s2_i)(s2_j)
				to_s2 = Option(new BasicAction("to->" + s2, s2))
				actionCache(s2_i)(s2_j) = to_s2
			}

			actionGrid(s1_i)(s1_j) += to_s2.get
			actionGrid(s2_i)(s2_j) += to_s1.get
		}

		def deleteDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = {
			val to_s1 = actionCache(s1_i)(s1_j)
			val to_s2 = actionCache(s2_i)(s2_j)
			actionGrid(s1_i)(s1_j) -= to_s2.get
			actionGrid(s2_i)(s2_j) -= to_s1.get
		}

		for (i <- 0 until x; j <- 0 until y) { // create all the (empty) states in the grid
			grid(i)(j) = new BasicState("(" + i + "," + j + ")")
		}

		for (i <- 0 until x; j <- 0 until y) { // initialize the cache of the actions
			actionCache(i)(j) = None
		}

		for (i <- 0 until x; j <- 0 until y) { // initialize all the action lists in the grid
			actionGrid(i)(j) = ListBuffer[Action]()
		}

		for (i <- 0 until x; j <- 0 until y) { // create all links (transitions) in the grid
			val underneath = getOrNone(i, j + 1)
			val right = getOrNone(i + 1, j)

			if (underneath nonEmpty)
				createDoubleLink(i, j, i, j + 1)
			if (right nonEmpty)
				createDoubleLink(i + 1, j, i, j)
		}

		// create walls
		deleteDoubleLink(1, 0, 1, 2)
		deleteDoubleLink(2, 1, 2, 2)

//		deleteDoubleLink(1, 1, 2, 1)
//		deleteDoubleLink(2, 2, 3, 2)

		// setting the rewards
		val positive_reward = 20
		val negative_reward = -2 * positive_reward

		actionCache(0)(0).get.setReward(100) // Goal

		actionCache(0)(2).get.setReward(positive_reward)
		actionCache(0)(3).get.setReward(positive_reward)
		actionCache(2)(1).get.setReward(positive_reward)

		actionCache(2)(3).get.setReward(negative_reward)
		actionCache(3)(1).get.setReward(negative_reward)

		for (i <- 0 until x; j <- 0 until y) { // setting all actions into the states
			grid(i)(j).setActions(actionGrid(i)(j).toList)
		}

		// Not true anymore with the new algorithm improvement
		// Note: positive reward must be less than (('discount factor' * 'goal reward') * (1 - 'discount factor'))

		// Negative reward must not be analysed in deep yet
	}

	def getInit: BasicState = grid(x - 1)(y - 1)

	def getRandomState: BasicState = {
		val random = new Random()
		val i = random.nextInt(x)
		val j = random.nextInt(y)

		grid(i)(j)
	}

}
