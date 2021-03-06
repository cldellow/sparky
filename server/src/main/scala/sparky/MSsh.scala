package sparky

import sys.process._
import java.io.File

class MSsh(command: String, hostname: String) {
  def runSsh(): Stream[String] = {
    val cmd = Seq("ssh", hostname, command)

    cmd.lineStream
  }

  def runSsh(args: String): Stream[String] = {
    val cmd = Seq("ssh", hostname, command, args)

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
  def newMSsh(script: String, host: String): MSsh = {
    new MSsh("mkdir -p ~/bin/sparky/", host).runSsh() foreach println
    doScp(script, host)
    new MSsh("chmod 755 ~/bin/sparky/"+ script, host).runSsh() foreach println
    new MSsh("~/bin/sparky/"+ script, host)
  }
  def runScriptOnMachine(script: String, host: String) = {
    val mssh = newMSsh(script, host)
    mssh.runSsh
  }
  def runScriptOnMachine(script: String, host: String, args: String) = {
    val mssh = newMSsh(script, host)
    mssh.runSsh(args)
  }
}
