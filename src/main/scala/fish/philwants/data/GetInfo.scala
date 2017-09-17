package fish.philwants.data

import spray.json.DefaultJsonProtocol

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
                        )

object GetInfo extends DefaultJsonProtocol {
  implicit val getInfoFormat = jsonFormat15(GetInfo.apply)
}
