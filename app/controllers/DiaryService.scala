package controllers

import javax.inject.Inject
import play.api.mvc.{BaseController, ControllerComponents}


class DiaryService @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def dayCheck(day: String): Boolean = {
    val days: List[String] = List("monday","tuesday","wednesday","thursday","friday","saturday", "sunday")
    days.contains(day.toLowerCase())
  }

  def yearCheck(year: String): Boolean = {
    year.length == 4
  }

  def monthCheck(month: String): Boolean = {
    val months: List[String] = List("january","february","march","april","may","june", "july","august","september","october","november", "december")
    months.contains(month.toLowerCase())
  }

  def yearAndMonthCheck(month: String, year: String): String = {
    var resultList: String = ""
    if (monthCheck(month) == false) {
      return resultList.concat(s"$month & $year")
    } else if (yearCheck(year) == false) {
      return resultList.concat(s"$year ")
    } else if (monthCheck(month) == false && yearCheck(year) == false) {
      return resultList.concat(s"$month")
    }
    resultList
  }

}

