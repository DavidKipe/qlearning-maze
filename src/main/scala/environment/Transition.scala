package environment

import environment.state.State

case class Transition(newState: State, reward: Int)
