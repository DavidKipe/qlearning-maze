package exception

import environment.state.State

class NoActionFound(protected val state: State, protected val extraInfo: String = "", override protected val cause: Throwable = None.orNull)
	extends MazeException(NoActionFound.getMessage(state, extraInfo), cause) {

}

object NoActionFound {

	protected def getMessage(state: State, extraInfo: String): String = {
		val msg = new StringBuilder("No action found for the state '").append(state).append("'")
		if (extraInfo.nonEmpty)
			msg.append(" - Extra info: ").append(extraInfo)
		msg.toString()
	}
}
