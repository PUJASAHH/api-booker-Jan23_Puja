package pojo.request.createbooking;

import java.util.Objects;

public class CreateBookingRequest {

	public String firstname;
	public String lastname;
	public String additionalNeeds;
	public int totalprice;
	public boolean depositpaid;
	public BookingDates bookingdates;

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getAdditionalNeeds() {
		return additionalNeeds;
	}

	public void setAdditionalNeeds(String additionalNeeds) {
		this.additionalNeeds = additionalNeeds;
	}

	public int getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}

	public boolean isDepositpaid() {
		return depositpaid;
	}

	public void setDepositpaid(boolean depositpaid) {
		this.depositpaid = depositpaid;
	}

	public BookingDates getBookingdates() {
		return bookingdates;
	}

	public void setBookingdates(BookingDates bookingdates) {
		this.bookingdates = bookingdates;
	}

	@Override
	public int hashCode() {
		return firstname.length();
	}

	@Override
	public boolean equals(Object obj) {
		
		CreateBookingRequest other = (CreateBookingRequest) obj;
		return Objects.equals(additionalNeeds, other.additionalNeeds)
				&& Objects.equals(bookingdates, other.bookingdates) && depositpaid == other.depositpaid
				&& Objects.equals(firstname, other.firstname) && Objects.equals(lastname, other.lastname)
				&& totalprice == other.totalprice;
	}

	
}
