import environment.action.{Action, BasicAction}
import environment.state.{BasicState, State}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.util.Random

object Main {

	private val qValues = mutable.Map[(State, Action), Double]() // experience (Q matrix) // TODO create an "ad hoc" class for storing this

	private var epsilon = 0.2

	private def episode(init: State): Unit = {
		println("Start episode")

		var old_state: State = null
		var curr_state = init

		val random = new Random

		do {
			val possible_actions = curr_state.getActions
			var best_actions: ListBuffer[Action] = null

			if (random.nextDouble < epsilon) { // epsilon greedy
				best_actions = possible_actions.to[ListBuffer]
				print(" * ")
			}
			else {
				best_actions = ListBuffer[Action]()
				var max_q = Double.NegativeInfinity

				for (a <- possible_actions) {
					val q = qValues.getOrElse((curr_state, a), 0.0)
					if (q == max_q) best_actions += a
					if (q > max_q) {
						best_actions.clear()
						best_actions += a
						max_q = q
					}
				}
			}

			val random_i: Int = random.nextInt(best_actions.size)
			val selected_a: Action = best_actions(random_i)

			old_state = curr_state
			curr_state = selected_a.act

			var max_next_q = Double.NegativeInfinity

			for (a <- curr_state.getActions) {
				val q = qValues.getOrElse((curr_state, a), 0.0)
				if (q > max_next_q) max_next_q = q
			}

			val qValue: Double = curr_state.getReward + 0.9 * max_next_q // TODO change formula adding learning rate (wikipedia)

			qValues += ((old_state, selected_a) -> qValue) // updating the Q matrix

			println(old_state + " - a: " + selected_a + " - q: " + qValue)
		} while (!(curr_state.toString == "final")) // TODO make a better condition
		println("End episode\n")
	}

	def main(args: Array[String]): Unit = {
		val init = new BasicState("init")
		val s1 = new BasicState("s1", 10)
		val s2 = new BasicState("s2", -20)
		val s3 = new BasicState("s3")
		val s4 = new BasicState("s4", -80)
		val end = new BasicState("final", 100)

		val to_init = new BasicAction("to_init", init)
		val to_s1 = new BasicAction("to_s1", s1)
		val to_s2 = new BasicAction("to_s2", s2)
		val to_s3 = new BasicAction("to_s3", s3)
		val to_s4 = new BasicAction("to_s4", s4)
		val to_final = new BasicAction("to_final", end)

		init.setActions(to_s1, to_s2)
		s1.setActions(to_init, to_s2, to_s3)
		s2.setActions(to_init, to_s1, to_s4, to_final)
		s3.setActions(to_s4, to_s1)
		s4.setActions(to_s3, to_s2, to_final)
		end.setActions(to_s2, to_s4)

		for (i <- 1 to 500)
			episode(init)

		println("Best path:")
		epsilon = 0.0
		episode(init)
	}

}
