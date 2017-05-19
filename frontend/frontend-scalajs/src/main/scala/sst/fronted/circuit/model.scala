package sst.fronted.circuit

import diode.data.Pot
import sst.shared.{Note, Notebook}

case class RootModel(notebooks: Pot[Notebooks], notes: Pot[Notes])

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