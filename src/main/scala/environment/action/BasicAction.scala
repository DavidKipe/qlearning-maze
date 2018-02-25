package environment.action

import environment.state.State

class BasicAction(private val label: String, protected val toState: State) extends Action {

	require(label != null && label.nonEmpty, "The label of an Action can not be null or empty")
	require(toState != null, "An Action require a target State")

	override def act: State = toState

	override def toString: String = label


	def canEqual(other: Any): Boolean = other.isInstanceOf[BasicAction]

	override def equals(other: Any): Boolean = other match {
		case that: BasicAction =>
			(that canEqual this) &&
				label == that.label &&
				toState == that.toState
		case _ => false
	}

	override def hashCode(): Int = {
		val state = Seq(label, toState)
		state.map(_.hashCode()).foldLeft(0)((a, b) => 31 * a + b)
	}
}
