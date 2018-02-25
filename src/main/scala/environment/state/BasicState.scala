package environment.state

import environment.action.Action


class BasicState(private val label: String, private var actions: List[Action], private var reward: Int) extends State {

	require(label != null && label.nonEmpty, "The label of a State can not be null or empty")

	def this(label: String, reward: Int) {
		this(label, List[Action](), reward)
	}

	def this(label: String) {
		this(label, List[Action](), 0)
	}

	override private[environment] def setActions(actions: List[Action]): Unit = {
		this.actions = actions
	}

	override def getActions: List[Action] = actions

	override private[environment] def setReward(reward: Int): Unit = this.reward = reward

	override def getReward: Int = reward

	override def toString: String = label


	def canEqual(other: Any): Boolean = other.isInstanceOf[BasicState]

	override def equals(other: Any): Boolean = other match {
		case that: BasicState =>
			(that canEqual this) &&
				label == that.label &&
				reward == that.reward
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(label, reward)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}
}
