package sparky

import sys.process._
import java.io.File

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
  def getTs(script: String) = {
    new File("../data/" + script).lastModified
  }
  def doScp(script: String, host: String) {
    MSsh.scpTo("../data/"+ script, host, "bin/sparky/" + script)
  }
  def runScriptOnMachine(script: String, host: String) = {
    new MSsh("mkdir -p ~/bin/sparky/", host).runSsh() foreach println
    doScp(script, host)
    new MSsh("chmod 755 ~/bin/sparky/"+ script, host).runSsh() foreach println
    val mssh = new MSsh("~/bin/sparky/"+ script, host)
    mssh.runSsh
  }
}
