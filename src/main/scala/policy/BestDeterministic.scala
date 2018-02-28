package policy
import environment.action.Action
import environment.state.State
import learning.QMatrix

class BestDeterministic extends ExplorationPolicy {

	override def nextAction(state: State, qMatrix: QMatrix): Action = qMatrix.bestAction(state)

}
