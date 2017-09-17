package fish.philwants.data

import spray.json.DefaultJsonProtocol

final case class ValidateAddress(
                                isvalid: Boolean,
                                address: String,
                                scriptPubKey: String,
                                ismine: Boolean,
                                iswatchonly: Boolean,
                                isscript: Boolean,
                                pubkey: String,
                                iscompressed: Boolean,
                                account: String,
                                timestamp: Long,
                                hdkeypath: String,
                                hdmasterkeyid: String
                                ) {
  override def toString() = {
    s"""isvalid: $isvalid
       |address: $address
       |scriptPubKey: $scriptPubKey
       |ismine: $ismine
       |iswatchonly: $iswatchonly
       |isscript: $isscript
       |pubkey: $pubkey
       |iscompressed: $iscompressed
       |account: $account
       |timestamp: $timestamp
       |hdkeypath: $hdkeypath
       |hdmasterkeyid: $hdmasterkeyid""".stripMargin
  }
}

object ValidateAddress extends DefaultJsonProtocol {
  implicit val validateAddressFormat = jsonFormat12(ValidateAddress.apply)
}
