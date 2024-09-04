package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import salon.TreatmentOffer;
import salon.TreatmentType;
import users.Beautician;
import users.User;

public class BeauticianManager {
	public final String BEAUTICIANS_FILE_PATH = "./data/beauticians.csv";
	
	private HashMap<UUID, Beautician> beauticians = new HashMap<>();
	
	private static BeauticianManager instance;
	private BeauticianManager() {}
	public static BeauticianManager getInstance() {
		if (instance == null)
			instance = new BeauticianManager();
		return instance;
	}
	
	public boolean addBeautician(Beautician beautician){
		if(beauticians.containsKey(beautician.getID()))
			return false;
		beauticians.put(beautician.getID(), beautician);
		return true;
	}
	
	public void updateBeautician(Beautician beautician){
		beauticians.replace(beautician.getID(), beautician);
	}
	
	public boolean removeBeautician(UUID id){
		if (beauticians.containsKey(id)) 
			beauticians.get(id).setDeleted(true);
		else
			return false;
		return true;
	}
	public boolean forceRemoveBeautician(UUID id){
		if (beauticians.containsKey(id)) 
			beauticians.remove(id);
		else
			return false;
		return true;
	}
	public boolean removeBeautician(Beautician beautician){
		return removeBeautician(beautician.getID());
	}
	
	public Beautician getBeautician(UUID id) {
		if (beauticians.containsKey(id))  
			return beauticians.get(id);
		else                             
			return null;
	}
	// returns the first if there are multiple
	public Beautician getBeauticianByUsername(String username) {
		for(Beautician beautician : beauticians.values())
			if (beautician.getUsername().equals(username))
				return beautician;
		return null;
	}
	public HashMap<UUID, Beautician> getAllBeauticians(){
		return beauticians;
	}
	public ArrayList<Beautician> getAllActiveBeauticians() {
		ArrayList<Beautician> list = new ArrayList<>();
		for (Beautician b : beauticians.values())
			if (!b.isDeleted())
				list.add(b);
		return list;
	}
	public boolean contains(UUID id) {
		return beauticians.containsKey(id);
	}	
	public void loadBeauticians(){
		loadBeauticians(BEAUTICIANS_FILE_PATH);
	}
	public void loadBeauticians(String path){
		HashMap<UUID, Beautician> newBeauticians = new HashMap<>();
		Beautician newBeautician;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newBeautician = Beautician.parse(line);
				newBeauticians.put(newBeautician.getID(), newBeautician);
			}
			br.close();
			
			beauticians.clear();
			beauticians = newBeauticians;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveBeauticians(){
		saveBeauticians(BEAUTICIANS_FILE_PATH);
	}
	public void saveBeauticians(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Beautician.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(Beautician beautician : beauticians.values()) {
				bw.write(Beautician.parse(beautician));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}

	public String register(Beautician beautician) {
		String error = Beautician.isValid(beautician);
		if (error != null)
			return error;
		
		ArrayList<User> allUsers = UserManager.getInstance().getAllUsers();
		for(User user : allUsers) {
			if (user.getUsername().equals(beautician.getUsername()))
				return "Korisničko ime je zauzeto!";
		}
		
		return addBeautician(beautician) ? null : "Desila se greška";
	}
	public ArrayList<Beautician> getAllQualifiedBeauticians(TreatmentType treatmentType) {
		ArrayList<Beautician> result = new ArrayList<>();
		for(Beautician beautician : beauticians.values())
			if(!beautician.isDeleted() && beautician.getSpecializations().contains(treatmentType))
				result.add(beautician);
		return result;
	}
	public Beautician getOneQualifiedAndFreeBeautician(TreatmentOffer treatmentOffer, LocalDateTime startTime) {
		TreatmentType type = treatmentOffer.getTreatmentType();
		for (Beautician beautician : beauticians.values())
			if(!beautician.isDeleted() && beautician.getSpecializations().contains(type) 
									   && TreatmentManager.getInstance().isFree(startTime, treatmentOffer, beautician))
				return beautician;
		return null;
	}
	public double calculateAllSalaries(int year, int month) {
		double expense = 0;
		
		LocalDate targetedDate = LocalDate.of(year, month, 1);
		
		for (Beautician beautician : beauticians.values())
			if (beautician.getDateOfDeletion() == null || beautician.getDateOfDeletion().compareTo(targetedDate) > 0)
				expense += beautician.getSalary(true);
		
		return expense;
	}
	public double calculateAllBonuses(int year, int month) {
		double expense = 0;
		for (Beautician beautician : beauticians.values())
			expense += beautician.calculateBonusForMonth(year, month);
		return expense;
	}
	public ArrayList<LocalTime> getFreeTimes(LocalDate date, TreatmentOffer offer, Beautician beautician) {
		int day = date.getDayOfWeek().getValue() - 1;
		LocalTime openTime = SalonManager.getInstance().getSalon().getOpenTime(day);
		int lowestHour;
		if (openTime.getMinute() > 0)
			lowestHour = openTime.getHour() + 1;
		else
			lowestHour = openTime.getHour();
		LocalTime closeTime = SalonManager.getInstance().getSalon().getCloseTime(day);
		int highestHour = closeTime.getHour();
		
		ArrayList<LocalTime> result = new ArrayList<>();
		for (int i=lowestHour; i<=highestHour; i++)
			if (beautician == null)
				result.add(LocalTime.of(i, 0));
			else if(TreatmentManager.getInstance().isFree(LocalDateTime.of(date, LocalTime.of(i, 0)), offer, beautician))
				result.add(LocalTime.of(i, 0));
		
		return result;
	}
}
