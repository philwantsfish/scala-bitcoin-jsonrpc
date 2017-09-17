package fish.philwants


object Main {
  val uri = "http://192.168.0.11:8332"
  val username = sys.env("RPC_USERNAME")
  val password = sys.env("RPC_PASSWORD")

  def example(): Unit = {
    val client = JsonRpcClient(uri, username, password)
    import client._

//    println(validateaddress("17PTtGEgxkc78RzpyFi4ZuyUdPDmvGcHu8"))
    println(getinfo())
  }

  def main(args: Array[String]): Unit = {
    example()
  }
}
