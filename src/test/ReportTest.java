package test;

import static org.junit.Assert.assertArrayEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import app.Application;
import junit.framework.TestCase;
import managers.BeauticianManager;
import managers.ClientManager;
import managers.ManagerManager;
import managers.ReceptionistManager;
import managers.SalonManager;
import managers.TreatmentManager;
import reports.Reports;
import salon.Salon;
import salon.Treatment;
import salon.TreatmentException;
import salon.TreatmentOffer;
import salon.TreatmentStatus;
import salon.TreatmentType;
import users.Beautician;
import users.Client;
import users.Gender;
import users.Manager;
import users.Receptionist;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportTest extends TestCase{
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
		ClientManager.getInstance().addClient(c);
		BeauticianManager.getInstance().addBeautician(b);
		try { treatment = TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), r);
		} catch (TreatmentException e) {}
		treatment.update();
		SalonManager.getInstance().changeLoyaltyMinimum(3000);
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
		Application.currentUser = null;
    }
	
	@Test
	public void testReportIncomeEarned() {
		setUp();
		assertEquals(offer.getPrice(), Reports.incomeEarnedByBetween(b, LocalDateTime.of(2010, 1, 1, 0, 0), LocalDateTime.of(2023, 9, 1, 0, 0)));
	}
	@Test
	public void testReportTreatmentDone() {
		assertEquals(1, Reports.treatmentDoneByBetween(b, LocalDateTime.of(2010, 1, 1, 0, 0), LocalDateTime.of(2023, 9, 1, 0, 0)));
	}
	@Test
	public void testIncomeForMonth() {
		assertEquals(offer.getPrice(), Reports.getIncomeForMonth(2023,6));
	}
	@Test
	public void testExpensesForMonth() {
		assertTrue(Reports.getExpensesForMonth(2023,6) > 0);
	}
	@Test
	public void testAllWithStatusBetween() {
		assertEquals(1, Reports.treatmentWithStatusBetween(TreatmentStatus.IZVRŠEN, LocalDateTime.of(2010, 1, 1, 0, 0), LocalDateTime.of(2023, 9, 1, 0, 0)));
	}
	@Test
	public void testAnalyzeTreatmentOffer() {
		assertArrayEquals(new int[]{0, (int) offer.getPrice()}, Reports.analyzeTreatmentOffer(offer, LocalDateTime.of(2010, 1, 1, 0, 0), LocalDateTime.of(2023, 9, 1, 0, 0)));
		treatment.setStartTime(LocalDateTime.MAX.minusYears(1));
		treatment.setTreatmentStatus(TreatmentStatus.ZAKAZAN);
		assertArrayEquals(new int[]{1, (int) offer.getPrice()}, Reports.analyzeTreatmentOffer(offer, LocalDateTime.of(2010, 1, 1, 0, 0), LocalDateTime.MAX));
	}
	@Test
	public void testClientsThatSatisfyLoyaltyMinimum() {
		assertEquals(new ArrayList<Client>(), Reports.clientsThatSatisfyLoyaltyMinimum());
	}
	
}
