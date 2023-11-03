package controllers

import accesData.entities.Venue
import accesData.repositories.VenueRepository
import com.google.inject.matcher.Matchers._
import controllers.requests.VenueRequest
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.test._
import play.api.test.Helpers._
import play.api.mvc._
import services.VenueService


class VenueControllerSpec extends PlaySpec with Injecting with GuiceOneAppPerTest {

  val mockRepository: VenueRepository = mock[VenueRepository]
  val venueService : VenueService = new VenueService(mockRepository)
  val venueController = new VenueController(stubControllerComponents(),venueService)

  "The venueController" should   {


    "render the Venue object added to the Database" in {
      // subject under test (sut)
      //Esto de aca ya me lo esta injectado las depedencias solo

      val jsonBody = Json.obj(
        "name" -> "VenueValida",
        "address" -> "Capital",
        "capacity" -> 100
      )

      val jsonBodyResponse = Json.obj(
        "id" -> 3,
        "name" -> "VenueValida",
        "address" -> "Capital",
        "capacity" -> 100
      )

      val venue = VenueRequest("VenueValida","Capital",100)
      mockRepository.add(venue) returns Venue(1,"VenueValida","Capital",100)

      val validRequest = FakeRequest(POST, "/venues").withHeaders("Content-Type" -> "application/json").withJsonBody(jsonBody)

      val result = route(app,validRequest).get

      status(result) mustBe OK
      contentAsJson(result) mustBe jsonBodyResponse
    }
  }

}
