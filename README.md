# Scala Bitcoin Json RPC

A [Bitcoin client](https://en.bitcoin.it/wiki/Original_Bitcoin_client/API_calls_list) for Scala. Each API name and parameter list matches the original Bitcoin API.

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

Configure a client to communicate with a [bitcoind](https://en.bitcoin.it/wiki/Bitcoind) server:

```scala
val client = JsonRpcClient("http://<ip>:8332", "username", "password")
// For convenience Bring APIs into scope
import client._
```

The API is very similar to the bitcoin-cli. For example `getinfo()` will run the bitcoin-cli command `getinfo`:

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

## Example 1

Example 3-4 in [Mastering Bitcoin](https://www.amazon.ca/Mastering-Bitcoin-Unlocking-Digital-Cryptocurrencies/dp/1449374042) showed the reader how to retrieve a transaction and iterate the outputs in python. Here is the same snippet, but in Scala:

```scala
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
```

Which outputs

```text
1GdK9UzpHBzqzX2A9JFP3Di4weBwqgmoQA : 0.015
1Cdid9KFAaatwczBwBttQcwXYCpvK8h7FK : 0.0845
```

## Example 2

Example 3-5 in [Mastering Bitcoin](https://www.amazon.ca/Mastering-Bitcoin-Unlocking-Digital-Cryptocurrencies/dp/1449374042) showed the reader how to retrieve a block and sum the transaction outputs in python. Here it the same snippet, but in Scala:

```scala
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
```
which prints:

```text
Total value in block 10322.07722534
```
