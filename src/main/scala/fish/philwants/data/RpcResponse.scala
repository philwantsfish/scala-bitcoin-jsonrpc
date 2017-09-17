package fish.philwants.data

import spray.json._

/**
  * The Json RPC protocol responds with a object with an id and a result. The result type depends on the request
  *
  * @param result
  * @param id
  * @tparam T The type of the json rpc result
  */
final case class RpcResponse[T](result: T, id: String, error: Option[RpcResponseError])

final case class RpcResponseError(code: Int, message: String) extends RuntimeException
object RpcResponseError extends DefaultJsonProtocol {
  implicit val rpcResponseErrorFormat = jsonFormat2(RpcResponseError.apply)
}

object RpcResponse extends DefaultJsonProtocol {
  implicit def rpcResponseFormat[T: JsonFormat] = jsonFormat3(RpcResponse.apply[T])
}
