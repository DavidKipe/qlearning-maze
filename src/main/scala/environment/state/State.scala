package environment.state

import environment.action.Action

trait State {

  def setActions(actions: Action*): Unit

  def getActions: List[Action]

  def getReward: Int

}
