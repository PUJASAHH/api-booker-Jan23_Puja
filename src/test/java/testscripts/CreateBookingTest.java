package testscripts;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import constants.Status_Code;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import pojo.request.createbooking.BookingDates;
import pojo.request.createbooking.CreateBookingRequest;

// given- all input details9URI, headers, path/query parameters/ payload, headerType)
// when- submit api
// then- validate the response
public class CreateBookingTest {
	
	String token;
	int bookingId;
	CreateBookingRequest payload;
	
	@BeforeMethod
	public void generateToken() {
		RestAssured.baseURI="https://restful-booker.herokuapp.com";
		Response res = RestAssured.given()
			.log().all()
			.headers("Content-Type","application/json")
			.body("{\r\n"
					+ "    \"username\" : \"admin\",\r\n"
					+ "    \"password\" : \"password123\"\r\n"
					+ "}")
			.when().post("/auth");
		System.out.println(res.statusCode());
		Assert.assertEquals(res.statusCode(),200);
		System.out.println(res.asPrettyString());
		

		token = res.jsonPath().getString("token");
		System.out.println(token);	
	}
	
	@Test
	public void createBookingTestWithPOJO() {		
		BookingDates bookingDates = new BookingDates();
		bookingDates.setCheckin("2023-04-03");
		bookingDates.setCheckout("2023-04-07");
		
		payload = new CreateBookingRequest();
		payload.setFirstname("Poooja");
		payload.setLastname("Sah");
		payload.setTotalprice(123);
		payload.setDepositpaid(true);
		payload.setAdditionalNeeds("breakfast");
		payload.setBookingdates(bookingDates);
		
		Response res = RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Accept", "application/json")
				.body(payload)
				.log().all()
				.when()
				.post("/booking");
		
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
		/* Assert.assertTrue(Integer.valueOf(res.jsonPath().getInt("bookingId")) instanceof Integer);
		its not a good option to use for schema validation, becaz if bookingId returns string 
		value den it will fail at getInt("bookingValue"), whereas we expect it to fail at instanceOf Integer */
		bookingId =res.jsonPath().getInt("bookingid");
		Assert.assertTrue(bookingId > 0);
		validateResponse(res, payload, "booking.");
		
	}	
	
	@Test(priority = 1)
	public void getAllBookingTest() {	
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.log().all()
				.when()
				.get("/booking");

		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		List<Integer> listOfBookingIds = res.jsonPath().getList("bookingid");
		System.out.println(listOfBookingIds.size());
		Assert.assertTrue(listOfBookingIds.contains(bookingId));
	}
	
	@Test(priority = 2)
	public void getBookingIdDeserializedTest() {	
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.when()
				.get("/booking/"+bookingId);

		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(responseBody.equals(payload));
	}
	
	@Test(priority = 3)
	public void updateBookingIdTest() {	
		payload.setFirstname("Krishna");
		Response res = RestAssured.given()
				.headers("Content-Type","application/json")
				.headers("Accept", "application/json")
				.headers("Cookie", "token="+token)
				.log().all()
				.body(payload)
				.when()
				.put("/booking/"+bookingId);

		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		
		CreateBookingRequest responseBody = res.as(CreateBookingRequest.class);
		Assert.assertTrue(responseBody.equals(payload));
	}
	
	@Test(priority = 2, enabled=false)
	public void getBookingIdTest() {	
		Response res = RestAssured.given()
				.headers("Accept", "application/json")
				.when()
				.get("/booking"+bookingId);

		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());
		validateResponse(res, payload, "");	
	}
	
	@Test(enabled=false)
	public void createBookingTest() {		
		Response res = RestAssured.given()
				.log().all()
				.headers("Content-Type","application/json")
				.headers("Accept", "application/json")
				.body("{\r\n"
						+ "    \"firstname\" : \"Jim\",\r\n"
						+ "    \"lastname\" : \"Brown\",\r\n"
						+ "    \"totalprice\" : 111,\r\n"
						+ "    \"depositpaid\" : true,\r\n"
						+ "    \"bookingdates\" : {\r\n"
						+ "        \"checkin\" : \"2018-01-01\",\r\n"
						+ "        \"checkout\" : \"2019-01-01\"\r\n"
						+ "    }")
				.when()
				.post("/booking");
		System.out.println(res.getStatusCode());
		System.out.println(res.getStatusLine());
		Assert.assertEquals(res.getStatusCode(), Status_Code.OK);
	}

	
	@Test(enabled=false)
	public void createBookingTestInPlaneMode() {
		String payload = "{\r\n"
				+ "    \"username\" : \"admin\",\r\n"
				+ "    \"password\" : \"password123\"\r\n"
				+ "}";
		RequestSpecification reqSpec = RestAssured.given();
		
		reqSpec.baseUri("https://restful-booker.herokuapp.com");
		reqSpec.headers("Content-Type","application/json");
		reqSpec.body(payload);
		Response res = reqSpec.post("/auth");
		
		Assert.assertEquals(res.statusCode(), 200);
		System.out.println(res.asPrettyString());			
	}
	
	private void validateResponse(Response res, CreateBookingRequest payload, String object) {
		System.out.println(res.jsonPath().getString(object+"firstname"));
		Assert.assertEquals(res.jsonPath().getString(object+"firstname"), payload.getFirstname());
		Assert.assertEquals(res.jsonPath().getString(object+"lastname"), payload.getLastname());
		Assert.assertEquals(res.jsonPath().getInt(object+"totalprice"), payload.getTotalprice());
		Assert.assertEquals(res.jsonPath().getBoolean(object+"depositpaid"), payload.isDepositpaid());
		Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkin"), payload.getBookingdates().getCheckin());
		Assert.assertEquals(res.jsonPath().getString(object+"bookingdates.checkout"), payload.getBookingdates().getCheckout());
		Assert.assertEquals(res.jsonPath().getString(object+"additionalneeds"), payload.getAdditionalNeeds());
	}
}
