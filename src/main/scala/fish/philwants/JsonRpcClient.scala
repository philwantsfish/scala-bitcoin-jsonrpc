package fish.philwants

import com.typesafe.scalalogging.StrictLogging
import fish.philwants.data._
import spray.json._
import scalaj.http.{Http, HttpRequest }
import DefaultJsonProtocol._

final case class JsonRpcClient(uri: String, username: String, password: String) extends StrictLogging {
  val id = "scala-jsonrpc"
  val baseRequest = Http(uri).auth(username, password).header("content-type", "text/plain")

  def request(command: String, params: String = ""): HttpRequest =
    baseRequest.postData(s"""{"jsonrpc": "1.0", "id":"$id", "method": "$command", "params": [$params] }""")

  def send[T](request: HttpRequest)(implicit t: RootJsonFormat[RpcResponse[T]]): RpcResponse[T] = {
    val body: String = try {
      request.asString.body
    } catch {
      case t: Throwable =>
        logger.error(s"Failed to get response.", t)
        throw t
    }

    try {
      body.parseJson.convertTo[RpcResponse[T]]
    } catch {
      case rpcErr: RpcResponseError =>
        logger.error(s"Bad rpc request, failure code:${rpcErr.code}, msg:${rpcErr.message}")
        throw rpcErr
      case t: Throwable =>
        logger.error(s"Failed to convert $body to an RpcResponse", t)
        throw t
    }
  }

  // Throwing parse exception
//  def backupwallet(path: String): Unit = send[JsObject](request("backupwallet", s""""$path"""")).result

  def getblock(hash: String): Block = send[JsObject](request("getblock", s""""$hash"""")).result.convertTo[Block]

  def getblockcount(): Int = send[Int](request("getblockcount")).result

  def getblockhash(height: Int): String = send[String](request("getblockhash", s"$height")).result

  def decoderawtransaction(rawtransaction: String): Transaction =
    send[JsObject](request("decoderawtransaction", s""""$rawtransaction"""")).result.convertTo[Transaction]

  def getconnectioncount(): Int = send[Int](request("getconnectioncount")).result

  // Throwing method not found error
//  def getgenerate(): Boolean = send[Boolean](request("getgenerate")).result

  // Throwing method not found error
//  def gethashespersec(): Int = send[Int](request("gethashespersec")).result

  def getdifficulty(): Double = send[Double](request("getdifficulty")).result

  def getinfo(): GetInfo = send[JsObject](request("getinfo")).result.convertTo[GetInfo]

  // TODO: This only works on wallet transactions. Will need to create a type for this return
  def gettransaction(hash: String): JsObject = send[JsObject](request("gettransaction", s""""$hash"""")).result

  def listaccounts(confirmations: Int = 1): Map[String, Double] =
    send[JsObject](request("listaccounts", s"$confirmations")).result.convertTo[Map[String, Double]]

  def listunspent(minconf: Int = 1, maxconf: Int = 999999): List[Unspent] = {
    send[JsObject](request("listunspent", s"$minconf, $maxconf")).result.convertTo[List[Unspent]]
  }

  // listreceivedbyaccount
  // listreceivedbyaddress
  // listtransactions

  def getmininginfo(): MiningInfo = send[JsObject](request("getmininginfo")).result.convertTo[MiningInfo]

  def getnewaddress(): String = send[String](request("getnewaddress")).result

  def getrawtransaction(txid: String, verbose: Boolean = false): String =
    send[String](request("getrawtransaction", s""""$txid", $verbose""")).result

  def sendrawtransaction(transactionHex: String): String =
    send[String](request("sendrawtransaction", s""""$transactionHex""")).result

  def getrawmempool(): Seq[String] = send[Seq[String]](request("getrawmempool")).result

  def validateaddress(address: String): ValidateAddress = send[JsObject](request("validateaddress", s""""$address"""")).result.convertTo[ValidateAddress]
}


