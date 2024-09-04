package test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import junit.framework.TestCase;
import managers.BeauticianManager;
import managers.ClientManager;
import managers.ManagerManager;
import managers.ReceptionistManager;
import managers.SalonManager;
import managers.TreatmentManager;
import salon.Salon;
import salon.Treatment;
import salon.TreatmentException;
import salon.TreatmentOffer;
import salon.TreatmentType;
import users.Beautician;
import users.Client;
import users.Gender;
import users.Manager;
import users.Receptionist;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ScheduleTest extends TestCase{
	private Treatment treatment;
	private TreatmentOffer offer;
	private TreatmentType type;
	private Beautician b;
	private Client c;
	private Manager m;
	private Receptionist r;
	private Salon s;
	
	@Override
	protected void setUp() { // setup some basic entities that many tests use
		s = new Salon("testSalon", LocalTime.of(8, 0), LocalTime.of(20, 0), 100000);
		SalonManager.getInstance().setSalon(s);
		s.setPayments(1000, 1000, 100, 100);
		type = new TreatmentType("testType");
		offer = new TreatmentOffer("testOffer", type, LocalTime.of(1, 15), 1000.12);
		c = new Client("Test", "Test2", "123456789", "Adress", "TestTestC0", "123", Gender.male, false);
		m = new Manager("Test", "Test2", "123456789", "Adress", "TestTestM0", "123", Gender.male);
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(type);
		b = new Beautician("Test", "Test2", "123456789", "Adress", "TestTestB0", "123", Gender.male, (short) 1, 2, 0, types);
		r = new Receptionist("Test", "Test2", "123456789", "Adress", "TestTestR0", "123", Gender.male, (short) 1, 2, 0, 3);
//		treatment = new Treatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), TreatmentStatus.ZAKAZAN, 1000);
	} 
	@Override
    public void tearDown() { // we should clear data after every test case
		TreatmentManager.getInstance().getAllTreatments().clear();
		TreatmentManager.getInstance().getAllTreatmentOffers().clear();
		TreatmentManager.getInstance().getAllTreatmentTypes().clear();
		ClientManager.getInstance().getAllClients().clear();
		BeauticianManager.getInstance().getAllBeauticians().clear();
		ReceptionistManager.getInstance().getAllReceptionists().clear();
		ManagerManager.getInstance().getAllManagers().clear();
		SalonManager.getInstance().setSalon(null);
    }
	
	@Test
	public void testScheduleNoBeauticianNooneFree() {
		BeauticianManager.getInstance().addBeautician(b);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			fail("Exception didn't occurr.");
		} catch (TreatmentException e) {}
	}
	@Test
	public void testScheduleNoBeauticianNooneQualified() {
		BeauticianManager.getInstance().addBeautician(b);
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(new TreatmentType("wrongType"));
		Beautician b2 = new Beautician("Test", "Test2", "123456789", "Adress", "TestTestB0", "123", Gender.male, (short) 1, 2, 0, types);
		BeauticianManager.getInstance().addBeautician(b2);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			fail("Exception didn't occurr.");
		} catch (TreatmentException e) {}
	}
	@Test
	public void testScheduleNoBeauticianSuccess() {
		BeauticianManager.getInstance().addBeautician(b);
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(type);
		Beautician b2 = new Beautician("Test", "Test2", "123456789", "Adress", "TestTestB1", "123", Gender.male, (short) 1, 2, 0, types);
		BeauticianManager.getInstance().addBeautician(b2);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
		} catch (TreatmentException e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}

	@Test
	public void testScheduleNotQualified() {
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(new TreatmentType("wrongType"));
		Beautician b2 = new Beautician("Test", "Test2", "123456789", "Adress", "TestTestB1", "123", Gender.male, (short) 1, 2, 0, types);
		BeauticianManager.getInstance().addBeautician(b2);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, b2, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			fail("Exception didn't occurr.");
		} catch (TreatmentException e) {}
	}
	@Test
	public void testScheduleNotFree() {
		BeauticianManager.getInstance().addBeautician(b);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			fail("Exception didn't occurr.");
		} catch (TreatmentException e) {}
	}
	@Test
	public void testScheduleSuccess() {
		BeauticianManager.getInstance().addBeautician(b);
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(type);
		Beautician b2 = new Beautician("Test", "Test2", "123456789", "Adress", "TestTestB1", "123", Gender.male, (short) 1, 2, 0, types);
		BeauticianManager.getInstance().addBeautician(b2);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, b2, c, LocalDateTime.of(2023, 6, 5, 12, 0), r);
		} catch (TreatmentException e) {
			fail("Exception occurred: " + e.getMessage());
		}
	}
	@Test
	public void testScheduleOverlappingTime() {
		BeauticianManager.getInstance().addBeautician(b);
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 30), r);
			fail("Exception didn't occurr.");
		} catch (TreatmentException e) {}
	}
	@Test
	public void testTreatmentPriceAfterChange() {
		BeauticianManager.getInstance().addBeautician(b);
		double original;
		Treatment t = null;
		try {
			t = TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
		} catch (TreatmentException e) {
			fail("Exception occurred: " + e.getMessage());
		}
		original = t.getPrice();
		offer.setPrice(2000000);
		assertEquals(original, t.getPrice());
	}
}
