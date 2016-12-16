package sst.backend

import sst.shared.HelloWorld

object Main {

  def main(args: Array[String]): Unit = {
    System.out.println(HelloWorld("from Scala"))
    if (args.length > 0)
      Option(getClass.getClassLoader.getResource(args(0))) match {
        case Some(url) => System.out.println(s"Resource found: $url")
        case None => System.out.println("Resource not found")
      }
  }

}
