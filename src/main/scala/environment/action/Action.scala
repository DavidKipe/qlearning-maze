package environment.action

import environment.state.State

trait Action {

  def act: State

}
