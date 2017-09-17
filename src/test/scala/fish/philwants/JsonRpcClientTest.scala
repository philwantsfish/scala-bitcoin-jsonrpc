package fish.philwants

import org.scalatest.{FlatSpec, Matchers}
import scala.util.Try

class JsonRpcClientTest extends FlatSpec with Matchers {
  val uri = Try(sys.env("RPC_URI")).getOrElse(throw new RuntimeException("Must supply environment variable RPC_URI that points to a bitcoind service"))
  val user = Try(sys.env("RPC_USERNAME")).getOrElse(throw new RuntimeException("Must supply environment variable RPC_USERNAME for the bitcoind service"))
  val password = Try(sys.env("RPC_PASSWORD")).getOrElse(throw new RuntimeException("Must supply environment variable RPC_PASSWORD for the bitcoind service"))

  val client = JsonRpcClient(uri, user, password)
  import client._

  // Test data to use for tests
  val blockHeight = 277316
  val blockHash = "0000000000000001b6b9a13b095e96db41c4a928b97ef2d944a9b31b2cc7bdc4"
  val coinbasetxid = "d5ada064c6417ca25c4308bd158c34b77e1c0eca2a73cda16c737e7424afba2f"
  val txid = "b268b45c59b39d759614757718b9918caf0ba9d97c56f3b91956ff877c503fbe"


  "JsonRpcClient" should "support getinfo" in {
    getinfo().errors shouldBe 'empty
  }

  it should "support getblockhash" in {
    getblockhash(blockHeight) shouldBe blockHash
  }

  it should "support getblock" in {
    val block = getblock(blockHash)
    block.hash shouldBe blockHash
    block.nextblockhash shouldBe "000000000000000010236c269dd6ed714dd5db39d36b33959079d78dfd431ba7"
  }

  it should "support getrawtransaction" in {
    getrawtransaction(txid) shouldBe "010000000189b2f42166c9b79fa5ec6214f842aefec3f46392fd7b62610376106eed3155b1000000008a4730440220394e03eb5b73e8813d14f16780f696fac5f4d34bd0414fade8c8422cf6f293be02204dea334228bbc1fccd9e52e99b7dc749020fe55425b9156e94e3e3e628d58eba014104329f73fce53c4b70065d60b914c6b3511fc4201cfc25a84240a5e10b92bc96f5f9d2ab2c30b0c07ac16c76f92eae4fa3b1648dba168b76b06f228b8fee1aea33ffffffff0100bca065010000001976a9143c7cd91e41818d9a77b743082a042fc6ee9fdf3e88ac00000000"
  }

  it should "support decoderawtransaction" in {
    val t = decoderawtransaction(getrawtransaction(txid))
    t.size shouldBe 223
    t.vout.size shouldBe 1
    t.vin.size shouldBe 1
  }
}
