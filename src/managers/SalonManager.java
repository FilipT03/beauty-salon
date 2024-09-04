package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.UUID;

import salon.Salon;
import users.Client;

public class SalonManager {
	public final String SALON_FILE_PATH = "./data/salon.csv";
	private Salon salon;
	
	private static SalonManager instance;
	private SalonManager() {}
	public static SalonManager getInstance() {
		if (instance == null)
			instance = new SalonManager();
		return instance;
	}
	public Salon getSalon() {
		return salon;
	}
	public void setSalon(Salon salon) {
		this.salon = salon;
	}
	public void changeLoyaltyMinimum(int value) {
		salon.setLoyaltyMinimum(value);
		updateLoyaltyCards();
	}
	public void updateLoyaltyCards() {
		int minimum = salon.getLoyaltyMinimum();
		HashMap<UUID, Client> allClients = ClientManager.getInstance().getAllClients();
		for(Client client: allClients.values()) 
			client.setLoyaltyCard(client.calculateMoneySpent() >= minimum);
	}
	public void addBalance(double value) {
		salon.addBalance(value);
	}
	public void paySalariesForLastMonth() {
		int month = LocalDate.now().getMonthValue() - 1;
		int year = LocalDate.now().getYear();
		double expense = BeauticianManager.getInstance().calculateAllSalaries(year, month);
		addBalance(-expense);
		expense = ReceptionistManager.getInstance().calculateAllSalaries(year, month);
		addBalance(-expense);
	}
	public void payBonusesForLastMonth() {
		int month = LocalDate.now().getMonthValue() - 1;
		int year = LocalDate.now().getYear();
		double expense = BeauticianManager.getInstance().calculateAllBonuses(year, month);
		addBalance(-expense);
		expense = ReceptionistManager.getInstance().calculateAllBonuses(year, month);
		addBalance(-expense);
	}
	public int calculateBeauticianSalary(short qualificationLevel, float workExperience) {
		return calculateSalary(qualificationLevel, workExperience, salon.getBeauticianSalaryBase());
	}
	public int calculateReceptionistSalary(short qualificationLevel, float workExperience) {
		return calculateSalary(qualificationLevel, workExperience, salon.getReceptionistSalaryBase());
	}
	private int calculateSalary(short qualificationLevel, float workExperience, int baseSalary) {
		double experienceMultiplier;
		if (workExperience <= 1)
			experienceMultiplier = 0.8;
		else if (workExperience <= 3)
			experienceMultiplier = 1;
		else if (workExperience <= 5)
			experienceMultiplier = 1.2;
		else if (workExperience <= 10)
			experienceMultiplier = 1.4;
		else
			experienceMultiplier = 1.6;
		
		double qualificationMultiplier;
		if (qualificationLevel <= 3)
			qualificationMultiplier = 0.8;
		else if (qualificationLevel == 4)
			qualificationMultiplier = 0.9;
		else if (qualificationLevel == 5)
			qualificationMultiplier = 1;
		else if (qualificationLevel == 6)
			qualificationMultiplier = 1.2;
		else if (qualificationLevel == 7)
			qualificationMultiplier = 1.5;
		else
			qualificationMultiplier = 1.8;
		
		return (int) Math.round(experienceMultiplier * qualificationMultiplier * baseSalary);
	}
	public void loadSalon(){
		loadSalon(SALON_FILE_PATH);
	}
	public void loadSalon(String path){
		Salon newSalon;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			line = br.readLine();
			newSalon = Salon.parse(line);
			br.close();

			salon = newSalon;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveSalon(){
		saveSalon(SALON_FILE_PATH);
	}
	public void saveSalon(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Salon.HEADER); // Write the header
			bw.write(System.lineSeparator());
			bw.write(Salon.parse(salon));
			bw.write(System.lineSeparator());
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
}
