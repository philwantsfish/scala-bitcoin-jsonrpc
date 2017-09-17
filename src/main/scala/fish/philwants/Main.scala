package fish.philwants


object Main {
  val uri = "http://192.168.0.11:8332"
  val username = sys.env("RPC_USERNAME")
  val password = sys.env("RPC_PASSWORD")


  def example_1(): Unit = {
    // This is from Mastering Bitcoin, Chapter 3, Example 3-4
    val client = JsonRpcClient(uri, username, password)
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

  def example_2(): Unit = {
    // This is from Mastering Bitcoin, Chapter 3, Example 3-5
    // This code snippet takes a block height and sums the transaction outputs
    val client = JsonRpcClient(uri, username, password)
    import client._


    // The block height where Alice's transaction was recorded
    val blockHeight = 277316

    // Get the block hash with height 277316
    val blockHash = getblockhash(blockHeight)

    // Retrieve the block by its hash
    val block = getblock(blockHash)

    // Element tx contains all transactions ids in the block
    val transactionIds = block.tx

    // Iterate through each transaction id
    val blockValue = transactionIds.map { txid =>
      // Get the transaction from its id
      val raw_tx = getrawtransaction(txid)
      val transaction = decoderawtransaction(raw_tx)
      // Iterate over each transaction summing the values
      transaction.vout.map { t =>
        t.value
      }.sum
    }.sum

    println(s"Total value in block $blockValue")
  }

  def example_3(): Unit = {
    val client = JsonRpcClient(uri, username, password)
    import client._

    println(validateaddress("17PTtGEgxkc78RzpyFi4ZuyUdPDmvGcHu8"))
  }

  def main(args: Array[String]): Unit = {
    example_3()
  }
}
