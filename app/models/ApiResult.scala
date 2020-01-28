package models

import io.circe.Json

case class ApiResult(list: Array[Json])
case class WeatherInfo(dt_txt: String, weather: Array[Json])
case class WeatherDesc(description: String)
case class Forecast(dateAndTime: WeatherInfo, description: WeatherDesc)


case class FullStuff(list: StuffInBrackets)
case class StuffInBrackets(w: WeatherStuff, dtStuff: Int)
case class WeatherStuff(id: Int, main: String, desc: String, icon: String)

