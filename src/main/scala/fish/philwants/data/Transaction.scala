package fish.philwants.data

import spray.json._
import scala.util.Try

final case class ScriptSignature(asm: String, hex: String)

object ScriptSignature extends DefaultJsonProtocol {
  implicit val scriptSignatureFormat = jsonFormat2(ScriptSignature.apply)
}

final case class PublicKey(hex: String, asm: String, `type`: String, reqSigs: Int, addresses: Seq[String])

object PublicKey extends DefaultJsonProtocol {
  implicit val publicKeyFormat = jsonFormat5(PublicKey.apply)
}

final case class TransactionOutput(value: Double, n: Int, scriptPubKey: PublicKey)

object TransactionOutput extends DefaultJsonProtocol {
  implicit val transactionOutputFormat = jsonFormat3(TransactionOutput.apply)
}

final case class TransactionInput(txid: String, vout: Int, scriptSig: ScriptSignature, sequence: Long)

object TransactionInput extends DefaultJsonProtocol {
  implicit val transactionInputFormat = jsonFormat4(TransactionInput.apply)
}

/**
  * The first transaction of each block is a coinbase transaction. These are special because they have no inputs, only
  * a single output claiming the mining reward. This class is a helper class used only when creating `Transaction`s from
  * json
  */
final case class CoinbaseTransaction(coinbase: String, sequence: Long)

object CoinbaseTransaction extends DefaultJsonProtocol {
  implicit val coinbaseTransactionFormat = jsonFormat2(CoinbaseTransaction.apply)

  def unapply(jsv: JsValue): Option[CoinbaseTransaction] = jsv match {
    case JsArray(elements) =>
      if (elements.size > 1) None
      else Try(elements.head.asJsObject.convertTo[CoinbaseTransaction]).toOption
    case _ =>
      None
  }
}

object Transaction extends DefaultJsonProtocol {
  // A custom Json formater is required to handle coinbase transactions
  implicit val transactionFormat = new RootJsonFormat[Transaction] {
    override def write(t: Transaction): JsValue = JsObject(
        "size" -> JsNumber(t.size),
        "txid" -> JsString(t.hash),
        "vout" -> t.vout.toJson,
        "vin" -> t.vin.toJson,
        "version" -> JsNumber(t.version),
        "hash" -> JsString(t.hash),
        "vsize" -> JsNumber(t.vsize),
        "locktime" -> JsNumber(t.locktime)
      )

    override def read(json: JsValue): Transaction = {
      json.asJsObject.getFields("size", "txid", "vout", "vin", "version", "hash", "vsize", "locktime") match {
        // Coinbase transactions dont have any inputs, but json is included in the rpc response.
        case Seq(JsNumber(s), JsString(txid), voutObj: JsArray, CoinbaseTransaction(vin), JsNumber(v), JsString(h), JsNumber(vsize), JsNumber(locktime)) =>
          Transaction(s.toInt, txid, voutObj.convertTo[Seq[TransactionOutput]], Seq(), v.toInt, h, vsize.toInt, locktime.toInt)
        case Seq(JsNumber(s), JsString(txid), voutObj: JsArray, vinObj: JsArray, JsNumber(v), JsString(h), JsNumber(vsize), JsNumber(locktime)) =>
          Transaction(s.toInt, txid, voutObj.convertTo[Seq[TransactionOutput]], vinObj.convertTo[Seq[TransactionInput]], v.toInt, h, vsize.toInt, locktime.toInt)
        case _ =>
          throw DeserializationException("Expected a Transaction")
      }
    }
  }
}

final case class Transaction(
                            size: Int,
                            txid: String,
                            vout: Seq[TransactionOutput],
                            vin: Seq[TransactionInput],
                            version: Int,
                            hash: String,
                            vsize: Int,
                            locktime: Int
                            ) {

  override def toString(): String = {
    s"""Transaction:
       |  size:     $size
       |  txid:     $txid
       |  version:  $version
       |  hash:     $hash
       |  vsize;    $vsize
       |  locktime: $locktime
       |  vin:      $vin
       |  vout      $vout
     """.stripMargin
  }
}

