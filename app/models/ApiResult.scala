package models

import java.text.SimpleDateFormat
import io.circe.Json

case class ApiResult(list: Array[Json])
case class WeatherDesc(description: String)
case class Forecast(dateAndTime: WeatherInfo, desc: WeatherDesc)
case class WeatherInfo(dt_txt: String, weather: Array[Json]) {
  def getFormattedDate: String = {
    val originalDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    val newDateFormat = new SimpleDateFormat("EEEE, d MMM - h a")
    val stringToDate = originalDateFormat.parse(dt_txt)
    newDateFormat.format(stringToDate)
  }
}
