package sparky

import sys.process._

class MSsh(command: String, hostname: String) {
  def runSsh(): Stream[String] = {
    val cmd = Seq("ssh", hostname, command)

    cmd.lineStream
  }

}
object MSsh {
  def scpTo(file: String, hostname: String, remoteFile: String) {
    val cmd = Seq("scp", file,  hostname + ":"+remoteFile)
    cmd.lineStream foreach println
  }
  def runScriptOnMachine(script: String, host: String) = {
    new MSsh("mkdir -p ~/bin/sparky/", host).runSsh() foreach println
    MSsh.scpTo("../data/"+ script, host, "bin/sparky/" + script)
    new MSsh("chmod 755 ~/bin/sparky/"+ script, host).runSsh() foreach println
    val mssh = new MSsh("~/bin/sparky/"+ script, host)
    mssh.runSsh
  }
}
