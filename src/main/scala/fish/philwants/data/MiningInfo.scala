package fish.philwants.data

import spray.json.DefaultJsonProtocol

final case class MiningInfo(
                           blocks: Int,
                           currentblocksize: Int,
                           currentblockweight: Int,
                           currentblocktx: Int,
                           difficulty: Double,
                           errors: String,
                           networkhashps: Double,
                           pooledtx: Int,
                           chain: String
                           ) {
  override def toString() = {
      s"blocks: $blocks\n" +
      s"currentblocksize: $currentblocksize\n" +
      s"currentblockweight: $currentblockweight\n" +
      s"currentblocktx: $currentblocktx\n" +
      s"difficulty: $difficulty\n" +
      s"errors: $errors\n" +
      s"networkhashps: $networkhashps\n" +
      s"pooledtx: $pooledtx"
  }
}

object MiningInfo extends DefaultJsonProtocol {
  implicit val miningInfoFormat = jsonFormat9(MiningInfo.apply)
}

