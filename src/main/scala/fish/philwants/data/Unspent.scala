package fish.philwants.data

import spray.json.DefaultJsonProtocol

final case class Unspent(
                        txid: String,
                        vout: Int,
                        address: String,
                        scriptPubKey: String,
                        amount: BigDecimal,
                        confirmations: Int,
                        spendable: Boolean,
                        solvable: Boolean,
                        safe: Boolean
                        ) {

  override def toString() = {
    s"""txid: $txid
       |vout: $vout
       |address: $address
       |scriptPubKey: $scriptPubKey
       |amount: $amount
       |confirmations: $confirmations
       |spendable: $spendable
       |solvable: $solvable
       |safe: $safe""".stripMargin
  }
}

object Unspent extends DefaultJsonProtocol {
  implicit val unspentFormat = jsonFormat9(Unspent.apply)
}
