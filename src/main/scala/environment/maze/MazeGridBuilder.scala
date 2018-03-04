package environment.maze

import environment.Environment
import environment.action.{Action, BasicAction}
import environment.state.{BasicState, State}

import scala.collection.mutable.ListBuffer

class MazeGridBuilder(val x: Int, val y: Int) extends GridBuilder {

	protected val grid: Array[Array[State]] = Array.ofDim[State](x, y) // the grid of the states

	// setting the constants for the rewards
	val goalReward: Int = 100
	val positiveReward: Int = 30 // TODO create method to set the path rewards bonus (example: WEAK and STRONG rewards)
	val negativeReward: Int = -80 //-2 * positiveReward

	protected val actionCache: Array[Array[Option[Action]]] = Array.ofDim[Option[Action]](x, y) // cache for re-using the actions // in position (i,j) there is the Action to go to State (i,j)
	protected val actionGrid: Array[Array[ListBuffer[Action]]] = Array.ofDim[ListBuffer[Action]](x, y) // store all the actions in the right grid position

	protected var startingState: State = _
	protected var goalState: State = _

	/* Constructor */
	private def createDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = { // create a link in both directions for two states in the grid
		var to_s1 = actionCache(s1_i)(s1_j)
		var to_s2 = actionCache(s2_i)(s2_j)

		if (to_s1.isEmpty) {
			val s1 = grid(s1_i)(s1_j)
			to_s1 = Option(new BasicAction("to->" + s1, s1))
			actionCache(s1_i)(s1_j) = to_s1
		}
		if (to_s2.isEmpty) {
			val s2 = grid(s2_i)(s2_j)
			to_s2 = Option(new BasicAction("to->" + s2, s2))
			actionCache(s2_i)(s2_j) = to_s2
		}

		actionGrid(s1_i)(s1_j) += to_s2.get
		actionGrid(s2_i)(s2_j) += to_s1.get
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
		if (j < y - 1)
			createDoubleLink(i, j, i, j + 1)
		if (i < x - 1)
			createDoubleLink(i + 1, j, i, j)
	}
	/*  */

	/* Aux funxtions */
	private def setReward(i: Int, j: Int, reward: Int): Unit = actionCache(i)(j).get.setReward(reward)

	private def deleteDoubleLink(s1_i: Int, s1_j: Int, s2_i: Int, s2_j: Int): Unit = {
		val to_s1 = actionCache(s1_i)(s1_j)
		val to_s2 = actionCache(s2_i)(s2_j)
		actionGrid(s1_i)(s1_j) -= to_s2.get
		actionGrid(s2_i)(s2_j) -= to_s1.get
	}

	/*  */

	/* GridBuilder trait */
	override def setGoalState(i: Int, j: Int): GridBuilder = {
		goalState = grid(i)(j)
		setReward(i, j, goalReward)
		this
	}

	override def setStartingState(i: Int, j: Int): GridBuilder = {
		startingState = grid(i)(j)
		this
	}

	override def setPosReward(i: Int, j: Int): GridBuilder = {
		setReward(i, j, positiveReward)
		this
	}

	override def setNegReward(i: Int, j: Int): GridBuilder = {
		setReward(i, j, negativeReward)
		this
	}

	override def setWall(i1: Int, j1: Int, i2: Int, j2: Int): GridBuilder = {
		deleteDoubleLink(i1, j1, i2, j2)
		this
	}

	override def build(): Environment = {
		for (i <- 0 until x; j <- 0 until y) { // setting all actions into the states
			grid(i)(j).setActions(actionGrid(i)(j).toList)
		}

		new Maze(grid, startingState, goalState)
	}
	/*  */

}
