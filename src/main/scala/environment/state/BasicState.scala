package environment.state

import environment.action.Action


class BasicState(val label: String, var actions: List[Action], var reward: Int) extends State {

	def this(label: String, reward: Int) {
		this(label, List[Action](), reward)
	}

	def this(label: String) {
		this(label, List[Action](), 0)
	}

	override def setActions(actions: Action*): Unit = {
		this.actions = actions.toList
	}

	override def getActions: List[Action] = actions

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
