package fish.philwants.data

import spray.json.DefaultJsonProtocol
import spray.json._

final case class GetInfo(
                        version: Int,
                        protocolversion: Int,
                        walletversion: Int,
                        balance: Int,
                        blocks: Int,
                        timeoffset: Int,
                        connections: Int,
                        proxy: String,
                        difficulty: Long,
                        testnet: Boolean,
                        keypoololdest: Long,
                        keypoolsize: Int,
                        paytxfee: Long,
                        relayfee: Long,
                        errors: String
                        ) {
  override def toString() = {
    GetInfo.getInfoFormat.write(this).prettyPrint
  }
}

object GetInfo extends DefaultJsonProtocol {
  implicit val getInfoFormat = jsonFormat15(GetInfo.apply)
}
