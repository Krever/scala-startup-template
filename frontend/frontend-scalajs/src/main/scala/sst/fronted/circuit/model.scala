package sst.fronted.circuit

import diode.data.Pot
import sst.shared.{Note, Notebook}

case class AuthToken(jwtString: String)

object MessageType extends Enumeration {
  type MessageType = Value
  val Success, Error = Value
}

case class Message(text: String, `type`: MessageType.MessageType)

case class RootModel(notebooks: Pot[Notebooks],
                     notes: Pot[Notes],
                     session: Option[AuthToken],
                     message: Option[Message])

case class Notebooks(items: Seq[Notebook]) {
  def updated(newItem: Notebook): Notebooks = {
    items.indexWhere(_.id == newItem.id) match {
      case -1 =>
        Notebooks(items :+ newItem)
      case idx =>
        Notebooks(items.updated(idx, newItem))
    }
  }

  def remove(item: Notebook) = Notebooks(items.filterNot(_ == item))
}

case class Notes(items: Seq[Note]) {
  def updated(newItem: Note): Notes = {
    items.indexWhere(_.id == newItem.id) match {
      case -1 =>
        Notes(items :+ newItem)
      case idx =>
        Notes(items.updated(idx, newItem))
    }
  }

  def remove(item: Note) = Notes(items.filterNot(_ == item))
}
