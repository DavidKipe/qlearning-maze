package policy

import environment.action.Action
import environment.state.State
import learning.QMatrix

trait ExplorationPolicy {

	def nextAction(state: State, qMatrix: QMatrix): Action

}
