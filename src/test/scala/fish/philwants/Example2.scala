package fish.philwants

/**
  * Created by okeefephil on 2017-09-17.
  */
object Example2 extends App {
  // This is from Mastering Bitcoin, Chapter 3, Example 3-5
  // This code snippet takes a block height and sums the transaction outputs
  val client = JsonRpcClient(sys.env("RPC_URI"), sys.env("RPC_USERNAME"), sys.env("RPC_PASSWORD"))
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
