package controllers

import accesData.database.VenuesTable
import accesData.entities.Venue
import accesData.repositories.VenueRepository
import controllers.requests.VenueRequest
import org.mockito._
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.mockito.MockitoSugar.mock
import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.Play.materializer
import play.api.libs.json.Json
import play.api.test.Helpers._
import play.api.test._
import services.VenueService
import slick.lifted.TableQuery
import slick.jdbc.PostgresProfile.api._
import slick.jdbc.PostgresProfile.api._


class VenueControllerSpec extends PlaySpec  with GuiceOneAppPerTest with MockitoSugar {

  val mockRepository: VenueRepository = mock[VenueRepository]
  val venueService : VenueService = new VenueService(mockRepository)
  val venueController = new VenueController(stubControllerComponents(),venueService)
  private val venuesTable = TableQuery[VenuesTable]


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
        "id" -> 6,
        "name" -> "VenueValida",
        "address" -> "Capital",
        "capacity" -> 100
      )

      val venueRequest = VenueRequest("VenueValida","Capital",100)
      val venue = Venue(Int.MinValue,venueRequest.name,venueRequest.address,venueRequest.capacity)

      when(mockRepository.add(venueRequest)) thenReturn ((venuesTable returning (venuesTable)) += venue)

      val validRequest = FakeRequest(POST, "/venues").withHeaders("Content-Type" -> "application/json").withJsonBody(jsonBody)

      val result = route(app,validRequest).get

      status(result) mustBe OK
      contentAsJson(result) mustBe jsonBodyResponse
    }
  }

}
