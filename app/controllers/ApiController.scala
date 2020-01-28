package controllers

import com.softwaremill.sttp.{HttpURLConnectionBackend, _}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.Decoder
import javax.inject.Inject
import models.{ApiResult, Forecast, WeatherDesc, WeatherInfo}
import play.api.mvc.{Request, _}
import cats.syntax.either._


class ApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def apiCall: Action[AnyContent] =  Action { implicit request: Request[AnyContent] =>
    val apiKey = sys.env("WEATHER_API_KEY")
    val request = sttp.get(
      uri"https://api.openweathermap.org/data/2.5/forecast?lat=51.482056&lon=-0.280056&APPID=${apiKey}"
    )
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val responseBody: String = response.unsafeBody
    val decodedBody: Either[Error, ApiResult] = decode[ApiResult](responseBody)

    def testApiResults(apiResult: ApiResult): Array[Forecast] = {
      val testApiResult: ApiResult = decodedBody match {
        case Right(result: ApiResult) => result
      }
      apiResultWithMaps(testApiResult)
    }

    decodedBody match {
      case Right(result) => Ok(views.html.api(testApiResults(result)))
      case Left(error: Error) =>
        println(error)
        InternalServerError(views.html.error("Not working"))
    }
  }


  def apiResultWithMaps(apiResult: ApiResult): Array[Forecast] = {
    //   MAP RATHER THAN FOR LOOP
    apiResult.list.flatMap { jsonInstance =>
    val decodedWeather: Either[Error, WeatherInfo] = decode[WeatherInfo](jsonInstance.toString())
      val weatherInfo: WeatherInfo = decodedWeather match {
        case Right(result: WeatherInfo) => result
      }
      val weather: Array[Json] = weatherInfo.weather
      weather.map { info =>
      val decodedWeatherInfo: Either[Error, WeatherDesc] = decode[WeatherDesc](info.toString)
        val weatherDesc: WeatherDesc = decodedWeatherInfo match {
          case Right(desc: WeatherDesc) => desc
        }
        Forecast(dateAndTime = weatherInfo, description = weatherDesc)
      }
    }
  }
}

//  Go into JSON list (apiResult.list)
//  For every JSON instance in list =
//      -> Create WeatherInfo instance (dt_txt & weather[Json Array])
//        -> Go into WeatherInfo instance & create instance of WeatherDesc from weatherInfo (weatherDesc.description)
//          -> Save these instances to a seq of Forecasts (with dateAndTime & description)
//  Feed these to view to show to user!!!!!!


//  Original method of getting WeatherInfo & WeatherDesc
//  Does not create instances of case class Forecast

//  def apiResults(apiResult: ApiResult) = {
//
//    for (el: Json <- apiResult.list) {
//      var index: Int = 0
//      println("\n******************")
//      val decodedWeather: Either[Error, WeatherInfo] = decode[WeatherInfo](el.toString())
//      val weatherInfo: WeatherInfo = decodedWeather match {
//        case Right(result: WeatherInfo) =>  result
//      }
//
//      val weather: Array[Json] = weatherInfo.weather
//      for (item <- weather) {
//        val decodedWeatherInfo: Either[Error, WeatherDesc] = decode[WeatherDesc](item.toString)
//        val weatherDesc: WeatherDesc = decodedWeatherInfo match {
//          case Right(desc: WeatherDesc) => desc
//        }
//        println(s"weather desc = ${weatherDesc.description}")
//      }
//
//      print(s"weather date = ${weatherInfo.dt_txt}")
//      index += 1
//    }
//  }
