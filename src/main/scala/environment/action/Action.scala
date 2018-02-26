package environment.action

import environment.Transition

trait Action {

	def setReward(reward: Int): Unit

	def act: Transition

}
