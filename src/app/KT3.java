package app;

import java.time.LocalDateTime;

import managers.BeauticianManager;
import managers.ClientManager;
import managers.ReceptionistManager;
import managers.SalonManager;
import managers.TreatmentManager;
import managers.UserManager;
import reports.Reports;
import salon.Treatment;
import salon.TreatmentException;
import salon.TreatmentOffer;
import users.Beautician;
import users.Client;
import users.Receptionist;
import util.Printing;

public class KT3 {

	public static void main(String[] args) {
		UserManager userManager = UserManager.getInstance();
		BeauticianManager beauticianManager = BeauticianManager.getInstance();
		ClientManager clientManager = ClientManager.getInstance();
		TreatmentManager treatmentManager = TreatmentManager.getInstance();
		SalonManager salonManager = SalonManager.getInstance();
		
		//Main.setData();
		Application.loadAllData();
		
		userManager.login("MilicaM", "123452");
		
//		Ovde podatke tražim po imenu, dok će se u gui-u čuvati lista objekata, i ti objekti će biti korišćeni za kreiranje tretmana.
//		 treatmentManager.getAllTreatmentOffers();
//		Korisnik sada može da bira kozmetičara i termin. Izbor jednog će ograničiti izbor drugog.
//		 beauticianManager.getAllQualifiedBeauticians(offer.getTreatmentType());
//       treatmentManager.isFree(startTime, offer, beautician);
		TreatmentOffer offer = treatmentManager.getTreatmentOfferByName("Francuski manikir");
		Beautician beautician = beauticianManager.getBeauticianByUsername("JadrankaJ");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, (Client)Application.currentUser, LocalDateTime.of(2023, 6, 10, 18, 0), null);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage()); //u gui-u se može izvući poruka i ispisati u vidu greške
			e.printStackTrace();
			return;
		}
		
		userManager.logout();
		// Recepcioner Pera se loguje u sistem kako bi zakazao termin
		userManager.login("PeraP", "123457");
		
		offer = treatmentManager.getTreatmentOfferByName("Spa pedikir");
		beautician = beauticianManager.getBeauticianByUsername("ZikaZ");
		Client client = clientManager.getClientByUsername("MilicaM");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, client, LocalDateTime.of(2023, 6, 11, 9, 0), (Receptionist)Application.currentUser);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		System.out.println(Application.currentUser.getUsername());
		userManager.logout();
		userManager.login("MM", "12");
		System.out.println(Application.currentUser.getUsername());
		
		
		offer = treatmentManager.getTreatmentOfferByName("Sportska masaža");
		beautician = beauticianManager.getBeauticianByUsername("SimaS");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, (Client)Application.currentUser, LocalDateTime.of(2023, 6, 12, 8, 0), null);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		userManager.logout();
		userManager.login("PeraP", "123457");
		
		offer = treatmentManager.getTreatmentOfferByName("Relaks masaža");
		beautician = beauticianManager.getBeauticianByUsername("ZikaZ");
		client = clientManager.getClientByUsername("MM");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, client, LocalDateTime.of(2023, 6, 13, 19, 0), (Receptionist)Application.currentUser);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		userManager.logout();
		userManager.login("MM", "12");

		System.out.println("# Zakazivanje zauzetog termina: ");
		offer = treatmentManager.getTreatmentOfferByName("Francuski manikir");
		beautician = beauticianManager.getBeauticianByUsername("JadrankaJ");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, (Client)Application.currentUser, LocalDateTime.of(2023, 6, 10, 18, 0), null);
		} catch (TreatmentException e) {
			System.out.println(e.getMessage()); // ispisujemo da je termin zauzet
		}
		System.out.println();

		beautician = beauticianManager.getBeauticianByUsername("ZikaZ");
		System.out.println("# Uvid kozmetičara 2: ");
		Printing.printTreatments(beautician.getTreatments());
		
		userManager.logout();
		userManager.login("NikolaN", "123456");
		
		salonManager.changeLoyaltyMinimum(3500);
		System.out.println("LOY " + clientManager.getClientByUsername("MM").hasLoyaltyCard());
		
		client = clientManager.getClientByUsername("MilicaM");
		client.getTreatment(0).complete();
		System.out.println("# 1. treatman 1. klijenta: ");
		Printing.printTreatment(client.getTreatment(0));
		System.out.println("Potrošila novca: " + client.calculateMoneySpent());
		System.out.println("Bilans salona: " + salonManager.getSalon().getBalance());
		System.out.println();

		System.out.println("# 2. treatman 1. klijenta: ");
		client.getTreatment(1).cancelByClient();
		Printing.printTreatment(client.getTreatment(1));
		System.out.println("Potrošila novca: " + client.calculateMoneySpent());
		System.out.println("Bilans salona: " + salonManager.getSalon().getBalance());
		System.out.println();
		
		client = clientManager.getClientByUsername("MM");
		client.getTreatment(0).cancelBySalon();
		System.out.println("# 1. treatman 2. klijenta: ");
		Printing.printTreatment(client.getTreatment(0));
		System.out.println("Potrošio novca: " + client.calculateMoneySpent());
		System.out.println("Bilans salona: " + salonManager.getSalon().getBalance());
		System.out.println();
		
		client.getTreatment(1).didntShowUp();;
		System.out.println("# 2. treatman 2. klijenta: ");
		Printing.printTreatment(client.getTreatment(1));
		System.out.println("Potrošio novca: " + client.calculateMoneySpent());
		System.out.println("Bilans salona: " + salonManager.getSalon().getBalance());
		System.out.println();

		userManager.logout();
		userManager.login("MilicaM", "123452");

		offer = treatmentManager.getTreatmentOfferByName("Gel lak");
		beautician = beauticianManager.getBeauticianByUsername("SimaS");
		try {
			treatmentManager.scheduleTreatment(offer, beautician, (Client)Application.currentUser, LocalDateTime.of(2023, 6, 14, 9, 0), null);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}

		userManager.logout();
		userManager.login("MM", "12");

		offer = treatmentManager.getTreatmentOfferByName("Spa manikir");
		try {
			treatmentManager.scheduleTreatment(offer, (Client)Application.currentUser, LocalDateTime.of(2023, 6, 14, 9, 0), null);
		} catch (TreatmentException e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
			return;
		}
		
		client = clientManager.getClientByUsername("MM");
		client.getTreatment(2).complete();;
		System.out.println("# 3. treatman 2. klijenta: ");
		Printing.printTreatment(client.getTreatment(2));
		System.out.println("Potrošio novca: " + client.calculateMoneySpent());
		System.out.println("Bilans salona: " + salonManager.getSalon().getBalance());
		System.out.println();

		System.out.println("# Svi tretmani Mike Mikića: ");
		Printing.printTreatments(client.getTreatments());
		
		System.out.println("Prihodi za 6. mesec 2023: " + Reports.getIncomeForMonth(2023, 6));
//		Ovde samo uzimam izveštaj rashoda za dati mesec, dok bi se isplaćivanje plate vršilo 
//		pomoću salonManager.paySalariesForLastMonth() i salonManager.payBonusesForLastMonth()
		System.out.println("Rashodi za 6. mesec 2023: " + Reports.getExpensesForMonth(2023, 6));
		
		System.out.println("# Izvršićemo puno tretmana da bi Zika dobio bonus");
		offer = treatmentManager.getTreatmentOfferByName("Francuski manikir");
		beautician = beauticianManager.getBeauticianByUsername("ZikaZ");
		for(int i=1;i<=10;i++) {
			try {
				Treatment t = treatmentManager.scheduleTreatment(offer, beautician, (Client)Application.currentUser, LocalDateTime.of(2023, 6, i, 18, 0), null);
				t.complete();
			} catch (TreatmentException e) {
				System.err.println(e.getMessage());
				e.printStackTrace();
				return;
			}
		}
		System.out.println("Rashodi za 6. mesec 2023: " + Reports.getExpensesForMonth(2023, 6));
		
		for(Beautician beautician2 : beauticianManager.getAllBeauticians().values())
			System.out.println("Kozmetičar " + beautician2.getUsername() + " u 6. mesecu ima bonus: " + beautician2.calculateBonusForMonth(2023, 6));
			
		for(Receptionist receptionist2 : ReceptionistManager.getInstance().getAllReceptionists().values())
			System.out.println("Recepcioner " + receptionist2.getUsername() + " u 6. mesecu ima bonus: " + receptionist2.calculateBonusForMonth(2023, 6));
	
		//Application.saveAllData();
	}

}
