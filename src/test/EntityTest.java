package test;

import static org.junit.Assert.assertNotEquals;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import org.junit.Test;

import app.Application;
import junit.framework.TestCase;
import managers.BeauticianManager;
import managers.ClientManager;
import managers.ManagerManager;
import managers.ReceptionistManager;
import managers.SalonManager;
import managers.TreatmentManager;
import managers.UserManager;
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

public class EntityTest extends TestCase{
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
		treatment = new Treatment(offer, b, c, LocalDateTime.of(2023, 6, 5, 10, 0), TreatmentStatus.ZAKAZAN, 1000);
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
	public void testSalary() {
		b.setSalary(0);
		b.updateSalary();
		assertTrue("Testing beautician salary", b.getSalary(false) > 0);
		
		r.setSalary(0);
		r.updateSalary();
		assertTrue("Testing beautician salary", r.getSalary(false) > 0);
	}
	
	@Test
	public void testCalulateMoneySpent() {
		assertEquals(0.0, c.calculateMoneySpent());
		assertEquals(0.0, c.calculateMoneySpentInMonth(2023, 6));
		
		try {
			TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, 4, 10, 0), r);
		} catch (TreatmentException e) {
			e.printStackTrace();
		}
		
		assertEquals(offer.getPrice(), c.calculateMoneySpent());
		assertEquals(offer.getPrice(), c.calculateMoneySpentInMonth(2023, 6));
	}
	
	@Test
	public void testBonusForMonth() {
		assertEquals(0.0, r.calculateBonusForMonth(2023, 6));
		try {
			for(int i=1;i<=10;i++)
				TreatmentManager.getInstance().scheduleTreatment(offer, b, c, LocalDateTime.of(2023, 6, i, 10, 0), r);
		} catch (TreatmentException e) {
			e.printStackTrace();
		}
		assertEquals((double)SalonManager.getInstance().getSalon().getReceptionistBonus(), r.calculateBonusForMonth(2023, 6));
	}
	@Test
	public void testTreatmentStatusChange() {
		s.addBalance(1000);
		treatment.cancelByClient();
		assertEquals(treatment.getPrice()*0.1, treatment.getItCostedClient());
		assertEquals(100000 + treatment.getPrice()*0.1, s.getBalance());
		treatment.complete();
		assertEquals(treatment.getPrice(), treatment.getItCostedClient());
		assertEquals(100000 + treatment.getPrice(), s.getBalance());
		treatment.cancelBySalon();
		assertEquals(0.0, treatment.getItCostedClient());
		assertEquals(100000.0, s.getBalance());
		treatment.didntShowUp();
		assertEquals(treatment.getPrice(), treatment.getItCostedClient());
		assertEquals(100000 + treatment.getPrice(), s.getBalance());
		treatment.setTreatmentStatus(TreatmentStatus.ZAKAZAN);
		treatment.update();
		assertEquals(TreatmentStatus.IZVRŠEN, treatment.getTreatmentStatus());
		assertEquals(treatment.getPrice(), treatment.getItCostedClient());
		assertEquals(100000 + treatment.getPrice(), s.getBalance());
	}
	@Test
	public void testRegister() {
		ClientManager.getInstance().register(c, false);
		r.setUsername(c.getUsername());
		assertNotNull(ReceptionistManager.getInstance().register(r));
		b.setUsername(c.getUsername());
		assertNotNull(BeauticianManager.getInstance().register(b));
		Client c2 = new Client("Test", "Test2", "123456789", "Adress", c.getUsername() + "addedChars", "123", Gender.male, false);
		assertNull(ClientManager.getInstance().register(c2, false));
	}
	@Test
	public void testLoginSuccess() {
		ClientManager.getInstance().register(c, false);
		assertTrue(UserManager.getInstance().login(c.getUsername(), c.getPassword()));
		assertEquals(c, (Client)Application.currentUser);
	}
	@Test
	public void testLoginFailWrongCreditials() {
		ClientManager.getInstance().register(c, false);
		assertFalse(UserManager.getInstance().login(c.getUsername() + "TEST", c.getPassword()));
		assertNotEquals(c, (Client)Application.currentUser);
		assertFalse(UserManager.getInstance().login(c.getUsername(), c.getPassword() + "TEST"));
		assertNotEquals(c, (Client)Application.currentUser);
	}
	@Test
	public void testLoginFailDeletedClient() {
		ClientManager.getInstance().register(c, false);
		ClientManager.getInstance().getClientByUsername(c.getUsername()).setDeleted(true);
		assertFalse(UserManager.getInstance().login(c.getUsername(), c.getPassword()));
		assertNotEquals(c, (Client)Application.currentUser);
	}
	@Test
	public void testLoginFailDeletedReceptionist() {
		ReceptionistManager.getInstance().register(r);
		ReceptionistManager.getInstance().getReceptionist(r.getID()).setDeleted(true);
		assertFalse(UserManager.getInstance().login(r.getUsername(), r.getPassword()));
		assertNotEquals(c, (Client)Application.currentUser);
	}
	@Test
	public void testLoginFailDeletedBeautician() {
		BeauticianManager.getInstance().register(b);
		BeauticianManager.getInstance().getBeautician(b.getID()).setDeleted(true);
		assertFalse(UserManager.getInstance().login(b.getUsername(), b.getPassword()));
		assertNotEquals(c, (Client)Application.currentUser);
	}
	@Test
	public void testLogout() {
		assertNull(Application.currentUser);
		ClientManager.getInstance().register(c, true);
		assertEquals(c, Application.currentUser);
		UserManager.getInstance().logout();
		assertNull(Application.currentUser);
	}
}
