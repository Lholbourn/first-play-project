package controllers

import com.softwaremill.sttp.{HttpURLConnectionBackend, _}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import io.circe.Decoder
import javax.inject.Inject
import models.{ApiResult, WeatherDesc, WeatherInfo}
import play.api.mvc.{Request, _}
import cats.syntax.either._






class ApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def apiCall: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val apiKey = sys.env("WEATHER_API_KEY")
    val request = sttp.get(
      uri"https://api.openweathermap.org/data/2.5/forecast?lat=51.482056&lon=-0.280056&APPID=${apiKey}"
    )
    implicit val backend: SttpBackend[Id, Nothing] = HttpURLConnectionBackend()
    val response = request.send()
    val responseBody: String = response.unsafeBody
    val decodedBody: Either[Error, ApiResult] = decode[ApiResult](responseBody)



    val result: Result = decodedBody match {
      case Right(result: ApiResult) => Ok(views.html.api(result))
      case Left(error: Error) =>
        println(error)
        InternalServerError(views.html.error("Not working"))
    }

    val testApiResult: ApiResult = decodedBody match {
      case Right(result: ApiResult) => result
    }
    val whatWeGot: Unit = apiResults(testApiResult)

    result
  }



  def apiResults(apiResult: ApiResult) = {

    for (el: Json <- apiResult.list) {
      var index: Int = 0

      println("\n******************")

      val decodedWeather: Either[Error, WeatherInfo] = decode[WeatherInfo](el.toString())
      val weatherInfo: WeatherInfo = decodedWeather match {
        case Right(result: WeatherInfo) =>  result

      }

      val weather: Array[Json] = weatherInfo.weather
      for (item <- weather) {
        val decodedWeatherInfo: Either[Error, WeatherDesc] = decode[WeatherDesc](item.toString)
        val weatherDesc: WeatherDesc = decodedWeatherInfo match {
          case Right(desc: WeatherDesc) => desc
        }
        println(s"weather desc = ${weatherDesc.description}")
      }

      print(s"weather date = ${weatherInfo.dt_txt}")
      index += 1
    }

  }

//  case class Forecast(dateAndTime: String, descritoin: String)
//  def apiResultWithMaps(apiResult: ApiResult): Seq[Forecast] = {
  // MAP RATHER THAN FOR LOOP
  //
  //  }




}




//      val cursor: HCursor = el.hcursor
//      val dt: Decoder.Result[Int] = cursor.downField("dt").as[Int]
//      val weather: Decoder.Result[String] = cursor.downField("weather").downN(2).as[String]
