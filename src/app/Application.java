package app;

import managers.SalonManager;
import managers.TreatmentManager;
import managers.UserManager;
import salon.Treatment;
import users.User;

public class Application {
	public static User currentUser;
	
	public static void loadAllData() {
		TreatmentManager.getInstance().loadTreatmentTypes();
		TreatmentManager.getInstance().loadTreatmentOffers();
		
		UserManager.getInstance().loadAllUsers();
		
		TreatmentManager.getInstance().loadTreatments();
		
		for(Treatment treatment : TreatmentManager.getInstance().getAllTreatments().values()) {
			treatment.getBeautician().addTreatment(treatment);
			treatment.getClient().addTreatment(treatment);
		}
		
		SalonManager.getInstance().loadSalon();
	}
	public static void saveAllData() {
		TreatmentManager.getInstance().saveTreatmentTypes();
		TreatmentManager.getInstance().saveTreatmentOffers();
		TreatmentManager.getInstance().saveTreatments();
		
		UserManager.getInstance().saveAllUsers();
		
		SalonManager.getInstance().saveSalon();
	}
}
