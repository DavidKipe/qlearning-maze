package environment.action

import environment.Transition

trait Action {

	private[environment] def setReward(reward: Int): Unit

	def act: Transition

}
