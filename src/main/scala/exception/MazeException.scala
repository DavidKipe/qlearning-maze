package exception

class MazeException(protected val message: String, protected val cause: Throwable = None.orNull) extends RuntimeException(message, cause) {

}
