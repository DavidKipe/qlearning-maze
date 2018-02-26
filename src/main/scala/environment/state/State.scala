package environment.state

import environment.action.Action

trait State {

	private[environment] def setActions(actions: List[Action]): Unit

	def getActions: List[Action]

}
