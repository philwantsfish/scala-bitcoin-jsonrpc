# Scala Bitcoin Json RPC

A client library to communicate with [bitcoind](https://en.bitcoin.it/wiki/Bitcoind) via JSON-RPC in Scala. The library provides the ability to query the blockchain, the network, and manage a local wallet.

Currently supports the following APIs:
- getblock
- getblockcount
- getblockhash
- decoderawtransaction
- getconnectioncount
- getdifficulty
- getinfo
- listaccounts
- getmininginfo
- getnewaddress
- getrawtransaction
- getrawmempool
- validateaddress

## Usage

Configure a client to communicate with a local or remote bitcoind server:

```scala
val client = JsonRpcClient("http://<ip>:8332", "username", "password")
// For convenience Bring APIs into scope
import client._
```

This library exposes APIs equivalent to the standard [bitcoin-cli API](https://en.bitcoin.it/wiki/Original_Bitcoin_client/API_Calls_list). For example `getinfo()` will run the bitcoin-cli command `getinfo`:

```scala
println(getinfo())
```

prints:

```text
{
  "testnet": false,
  "paytxfee": 0,
  "difficulty": 922724699725,
  "blocks": 485671,
  "timeoffset": -2,
  "errors": "",
  "balance": 0,
  "keypoololdest": 1504659203,
  "version": 140200,
  "protocolversion": 70015,
  "relayfee": 0,
  "proxy": "",
  "walletversion": 130000,
  "connections": 8,
  "keypoolsize": 100
}
```

The JSON-RPC response is converted to a Scala type. `JsObject` is converted to a `case class`, `JsArray` to a `Seq`, simple types kept as simple types, etc.


## Example 1

This example shows how to retrieve a transaction and print each output.

```scala
val client = JsonRpcClient(uri, username, password)
import client._

// A specific transaction id, this one was copied from Example 3-4 in Mastering Bitcoin
val txid = "0627052b6f28912f2703066a912ea577f2ce4da4caa5a5fbd8a57286c345c2f2"

// First, retrieve the raw transaction in hex
val raw_tx = getrawtransaction(txid)

// Decode the transaction hex into a transaction class
val decoded_tx = decoderawtransaction(raw_tx)

// Print each output in the transaction
decoded_tx.vout.foreach { t =>
  println(s"${t.scriptPubKey.addresses.mkString(",")} : ${t.value}")
}  
```

Which outputs

```text
1GdK9UzpHBzqzX2A9JFP3Di4weBwqgmoQA : 0.015
1Cdid9KFAaatwczBwBttQcwXYCpvK8h7FK : 0.0845
```

## Example 2

This example shows how to retrieve a block and sum the output transaction values

```scala
val client = JsonRpcClient(uri, username, password)
import client._


// A block height, coped from Example 3-5 in Mastering Bitcoin
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
```
which prints:

```text
Total value in block 10322.07722534
```
