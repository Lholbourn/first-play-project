package controllers


import javax.inject.{Inject, _}
import play.api.mvc._

@Singleton
class DiaryController @Inject()(val controllerComponents: ControllerComponents, val diaryService: DiaryService) extends BaseController {




  def day(d: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (diaryService.dayCheck(d) == true) {
      Ok(views.html.day(d))
    } else {
      NotAcceptable(views.html.error(d))
    }
  }

  def month(m: String, y: String): Action[AnyContent] = Action { implicit request: Request[AnyContent] =>
    if (diaryService.yearCheck(y) == true && diaryService.monthCheck(m) == true) {
      Ok(views.html.month(m, y))
    } else {
      NotAcceptable(views.html.error(diaryService.yearAndMonthCheck(m, y)))
    }
  }

}

