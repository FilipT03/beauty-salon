package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import users.Receptionist;
import users.User;

public class ReceptionistManager {
	public final String RECEPTIONISTS_FILE_PATH = "./data/receptionists.csv";
	
	private HashMap<UUID, Receptionist> receptionists = new HashMap<>();
	
	private static ReceptionistManager instance;
	private ReceptionistManager() {}
	public static ReceptionistManager getInstance() {
		if (instance == null)
			instance = new ReceptionistManager();
		return instance;
	}
	
	public boolean addReceptionist(Receptionist receptionist){
		if(receptionists.containsKey(receptionist.getID()))
			return false;
		receptionists.put(receptionist.getID(), receptionist);
		return true;
	}
	
	public void updateReceptionist(Receptionist receptionist){
		receptionists.replace(receptionist.getID(), receptionist);
	}
	
	public boolean removeReceptionist(UUID id){
		if (receptionists.containsKey(id)) 
			receptionists.get(id).setDeleted(true);
		else
			return false;
		return true;
	}
	public boolean forceRemoveReceptionist(UUID id){
		if (receptionists.containsKey(id)) 
			receptionists.remove(id);
		else
			return false;
		return true;
	}
	public boolean removeReceptionist(Receptionist receptionist){
		return removeReceptionist(receptionist.getID());
	}
	
	public Receptionist getReceptionist(UUID id) {
		if (receptionists.containsKey(id))  
			return receptionists.get(id);
		else                             
			return null;
	}
	public HashMap<UUID, Receptionist> getAllReceptionists(){
		return receptionists;
	}
	public boolean contains(UUID id) {
		return receptionists.containsKey(id);
	}
	public void loadReceptionists(){
		loadReceptionists(RECEPTIONISTS_FILE_PATH);
	}
	public void loadReceptionists(String path){
		HashMap<UUID, Receptionist> newReceptionists = new HashMap<>();
		Receptionist newReceptionist;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newReceptionist = Receptionist.parse(line);
				newReceptionists.put(newReceptionist.getID(), newReceptionist);
			}
			br.close();
			
			receptionists.clear();
			receptionists = newReceptionists;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveReceptionists(){
		saveReceptionists(RECEPTIONISTS_FILE_PATH);
	}
	public void saveReceptionists(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Receptionist.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(Receptionist receptionist : receptionists.values()) {
				bw.write(Receptionist.parse(receptionist));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	
	public String register(Receptionist receptionist) {
		String error = Receptionist.isValid(receptionist);
		if (error != null)
			return error;
		
		ArrayList<User> allUsers = UserManager.getInstance().getAllUsers();
		for(User user : allUsers) {
			if (user.getUsername().equals(receptionist.getUsername()))
				return "Korisničko ime je zauzeto!";
		}
		
		return addReceptionist(receptionist) ? null : "Desila se greška";
	}
	public double calculateAllSalaries(int year, int month) {
		double expense = 0;
		
		LocalDate targetedDate = LocalDate.of(year, month, 1);
		
		for (Receptionist receptionist: receptionists.values())
			if (receptionist.getDateOfDeletion() == null || receptionist.getDateOfDeletion().compareTo(targetedDate) > 0)
				expense += receptionist.getSalary(true);
		
		return expense;
	}
	public double calculateAllBonuses(int year, int month) {
		double expense = 0;
		
		for (Receptionist receptionist : receptionists.values())
			expense += receptionist.calculateBonusForMonth(year, month);
		return expense;
	}
}
