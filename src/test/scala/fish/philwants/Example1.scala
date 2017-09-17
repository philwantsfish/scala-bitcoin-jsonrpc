package fish.philwants

object Example1 extends App {
  // This is from Mastering Bitcoin, Chapter 3, Example 3-4
  val client = JsonRpcClient(sys.env("RPC_URI"), sys.env("RPC_USERNAME"), sys.env("RPC_PASSWORD"))
  import client._

  // Alice's transaction ID
  val txid = "0627052b6f28912f2703066a912ea577f2ce4da4caa5a5fbd8a57286c345c2f2"

  // First, retrieve the raw transaction in hex
  val raw_tx = getrawtransaction(txid)

  // Decode the transaction hex into a JSON object
  val decoded_tx = decoderawtransaction(raw_tx)

  // Retrieve each of the outputs from the transaction
  decoded_tx.vout.foreach { t =>
    println(s"${t.scriptPubKey.addresses.mkString(",")} : ${t.value}")
  }
}
