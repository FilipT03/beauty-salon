package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

import users.Manager;

public class ManagerManager {
	public final String MANAGERS_FILE_PATH = "./data/managers.csv";
	
	private HashMap<UUID, Manager> managers = new HashMap<>();
	
	private static ManagerManager instance;
	private ManagerManager() {};
	public static ManagerManager getInstance() {
		if (instance == null)
			instance = new ManagerManager();
		return instance;
	}
	
	public boolean addManager(Manager manager){
		if(managers.containsKey(manager.getID()))
			return false;
		managers.put(manager.getID(), manager);
		return true;
	}
	
	public void updateManager(Manager manager){
		managers.replace(manager.getID(), manager);
	}
	
	public boolean removeManager(UUID id){
		if (managers.containsKey(id)) 
			managers.get(id).setDeleted(true);
		else
			return false;
		return true;
	}
	public boolean forceRemoveManager(UUID id){
		if (managers.containsKey(id)) 
			managers.remove(id);
		else
			return false;
		return true;
	}
	public boolean removeManager(Manager manager){
		return removeManager(manager.getID());
	}
	
	public Manager getManager(UUID id) {
		if (managers.containsKey(id))  
			return managers.get(id);
		else                             
			return null;
	}
	public HashMap<UUID, Manager> getAllManagers(){
		return managers;
	}
	public boolean contains(UUID id) {
		return managers.containsKey(id);
	}
	public void loadManagers(){
		loadManagers(MANAGERS_FILE_PATH);
	}
	public void loadManagers(String path){
		HashMap<UUID, Manager> newManagers = new HashMap<>();
		Manager newManager;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newManager = Manager.parse(line);
				newManagers.put(newManager.getID(), newManager);
			}
			br.close();
			
			managers.clear();
			managers = newManagers;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveManagers(){
		saveManagers(MANAGERS_FILE_PATH);
	}
	public void saveManagers(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Manager.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(Manager manager : managers.values()) {
				bw.write(Manager.parse(manager));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
}
