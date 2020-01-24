package controllers

import com.softwaremill.sttp.{HttpURLConnectionBackend, _}
import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import javax.inject.Inject
import models.{ApiResult, WeatherInfo}
import play.api.mvc.{Request, _}

class ApiController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def apiCall: Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    val apiKey = sys.env("WEATHER_API_KEY")
    val request = sttp.get(
      uri"https://api.openweathermap.org/data/2.5/forecast?lat=51.482056&lon=-0.280056&APPID=${apiKey}"
    )
    implicit val backend = HttpURLConnectionBackend()
    val response = request.send()
    val responseBody = response.unsafeBody
    val decodedBody: Either[Error, ApiResult] = decode[ApiResult](responseBody)
    decodedBody match {
      case Right (result) => Ok(views.html.api(result))
      case Left (error) =>
        println(error)
        InternalServerError(views.html.error("Not working"))
    }
  }

  def apiResults(apiResult: ApiResult)= {
    def makeWeather(apiResult: ApiResult) = {
      for (result <- apiResult) {
        new WeatherInfo()
      }
    }

  }
}



//    NEED TO WORK OUT HOW TO MAKE VIEW FOR API RESULTS
//    THEN NEED TO INTEGRATE API CALL WITH USER DAY QUERY W/ THE DIARY SERVICE
