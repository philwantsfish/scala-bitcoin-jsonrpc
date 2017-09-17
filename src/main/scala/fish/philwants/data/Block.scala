package fish.philwants.data

import spray.json.DefaultJsonProtocol

final case class Block(
                      hash: String,
                      confirmations: Int,
                      size: Int,
                      height: Int,
                      version: Int,
                      merkleroot: String,
                      tx: Seq[String],
                      time: Long,
                      mediantime: Long,
                      nonce: Long,
                      bits: String,
                      difficulty: Long,
                      chainwork: String,
                      previousblockhash: String,
                      nextblockhash: String
                      ) {

  override def toString(): String = {
    s"""Block:
       |  hash:              $hash
       |  confirmations:     $confirmations
       |  size:              $size
       |  height             $height
       |  version:           $version
       |  merkleroot:        $merkleroot
       |  time:              $time
       |  mediantime:        $mediantime
       |  nonce:             $nonce
       |  bits:              $bits
       |  difficulty:        $difficulty
       |  chainwork:         $chainwork
       |  previousblockhash: $previousblockhash
       |  nextblockhash:     $nextblockhash
       |  tx: [
       |    ${tx.mkString(",\n    ")}
       |  ]
     """.stripMargin

  }
}

object Block extends DefaultJsonProtocol {
  implicit val blockFormat = jsonFormat15(Block.apply)
}
