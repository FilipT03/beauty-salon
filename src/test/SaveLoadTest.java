package test;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

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
import salon.TreatmentOffer;
import salon.TreatmentStatus;
import salon.TreatmentType;
import users.Beautician;
import users.Client;
import users.Gender;
import users.Manager;
import users.Receptionist;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SaveLoadTest extends TestCase{
	private Treatment treatment;
	private TreatmentOffer offer;
	private TreatmentType type;
	private Beautician b;
	private Client c;
	private Manager m;
	private Receptionist r;
	private Salon s;
	@Override
	protected void setUp() {  // setup some basic entities that many tests use
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
    public void tearDown() {  // we should clear data after every test case
		TreatmentManager.getInstance().getAllTreatments().clear();
		TreatmentManager.getInstance().getAllTreatmentOffers().clear();
		TreatmentManager.getInstance().getAllTreatmentTypes().clear();
		ClientManager.getInstance().getAllClients().clear();
		BeauticianManager.getInstance().getAllBeauticians().clear();
		ReceptionistManager.getInstance().getAllReceptionists().clear();
		ManagerManager.getInstance().getAllManagers().clear();
		SalonManager.getInstance().setSalon(null);
		new File("./tmp.csv").delete();
    }
	
	@Test
	public void testClientsFile() {
		ClientManager.getInstance().addClient(c);
		UUID id = c.getID();
		ClientManager.getInstance().saveClients("./tmp.csv");
		ClientManager.getInstance().getAllClients().clear();
		ClientManager.getInstance().loadClients("./tmp.csv");
		assertThat(ClientManager.getInstance().getClient(id)).isEqualToComparingFieldByField(c);
	}
	@Test
	public void testBeauticiansFile() {
		BeauticianManager.getInstance().addBeautician(b);
		UUID id = b.getID();
		BeauticianManager.getInstance().saveBeauticians("./tmp.csv");
		BeauticianManager.getInstance().getAllBeauticians().clear();
		BeauticianManager.getInstance().loadBeauticians("./tmp.csv");
		ArrayList<TreatmentType> types = new ArrayList<TreatmentType>();
		types.add(type);
		BeauticianManager.getInstance().getBeautician(id).setSpecializations(types);
		assertThat(BeauticianManager.getInstance().getBeautician(id)).isEqualToComparingFieldByField(b);
	}
	@Test
	public void testReceptionistsFile() {
		ReceptionistManager.getInstance().addReceptionist(r);
		UUID id = r.getID();
		ReceptionistManager.getInstance().saveReceptionists("./tmp.csv");
		ReceptionistManager.getInstance().getAllReceptionists().clear();
		ReceptionistManager.getInstance().loadReceptionists("./tmp.csv");
		assertThat(ReceptionistManager.getInstance().getReceptionist(id)).isEqualToComparingFieldByField(r);
	}
	@Test
	public void testManagersFile() {
		ManagerManager.getInstance().addManager(m);
		UUID id = m.getID();
		ManagerManager.getInstance().saveManagers("./tmp.csv");
		ManagerManager.getInstance().getAllManagers().clear();
		ManagerManager.getInstance().loadManagers("./tmp.csv");
		assertThat(ManagerManager.getInstance().getManager(id)).isEqualToComparingFieldByField(m);
	}
	@Test
	public void testTypesFile() {
		TreatmentManager.getInstance().addTreatmentType(type);
		String id = type.name;
		TreatmentManager.getInstance().saveTreatmentTypes("./tmp.csv");
		TreatmentManager.getInstance().getAllTreatmentTypes().clear();
		TreatmentManager.getInstance().loadTreatmentTypes("./tmp.csv");
		assertThat(TreatmentManager.getInstance().getTreatmentType(id)).isEqualToComparingFieldByField(type);
	}
	@Test
	public void testOffersFile() {
		TreatmentManager.getInstance().addTreatmentType(type);
		TreatmentManager.getInstance().addTreatmentOffer(offer);
		UUID id = offer.getID();
		TreatmentManager.getInstance().saveTreatmentOffers("./tmp.csv");
		TreatmentManager.getInstance().getAllTreatmentOffers().clear();
		TreatmentManager.getInstance().loadTreatmentOffers("./tmp.csv");
		assertThat(TreatmentManager.getInstance().getTreatmentOffer(id)).isEqualToComparingFieldByField(offer);
	}
	@Test
	public void testTreatmentsFile() {
		ClientManager.getInstance().addClient(c);
		BeauticianManager.getInstance().addBeautician(b);
		TreatmentManager.getInstance().addTreatmentType(type);
		TreatmentManager.getInstance().addTreatmentOffer(offer);
		TreatmentManager.getInstance().addTreatment(treatment);
		treatment.update();
		UUID id = treatment.getID();
		TreatmentManager.getInstance().saveTreatments("./tmp.csv");
		TreatmentManager.getInstance().getAllTreatments().clear();
		TreatmentManager.getInstance().loadTreatments("./tmp.csv");
		assertThat(TreatmentManager.getInstance().getTreatment(id)).isEqualToComparingFieldByField(treatment);
	}
}
