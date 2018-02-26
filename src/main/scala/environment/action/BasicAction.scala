package environment.action

import environment.Transition
import environment.state.State

class BasicAction(private val label: String, protected val toState: State, protected var reward: Int) extends Action {

	require(label != null && label.nonEmpty, "The label of an Action can not be null or empty")
	require(toState != null, "An Action require a target State")

	def this(label: String, toState: State) = this(label, toState, 0)


	override def setReward(reward: Int): Unit = this.reward = reward

	override def act: Transition = Transition(toState, reward)

	override def toString: String = label


	def canEqual(other: Any): Boolean = other.isInstanceOf[BasicAction]

	override def equals(other: Any): Boolean = other match {
		case that: BasicAction =>
			(that canEqual this) &&
				label == that.label
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(label)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}
}
