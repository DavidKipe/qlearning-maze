package environment.state

import environment.action.Action

trait State {

	private[environment] def setActions(actions: List[Action]): Unit

	private[environment] def setReward(reward: Int): Unit

	def getActions: List[Action]

	def getReward: Int

}
