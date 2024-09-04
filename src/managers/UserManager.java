package managers;

import java.util.ArrayList;
import java.util.UUID;

import app.Application;
import users.Beautician;
import users.Client;
import users.Manager;
import users.Receptionist;
import users.User;

public class UserManager {
	private ClientManager clientManager;
	private BeauticianManager beauticianManager;
	private ReceptionistManager receptionistManager;
	private ManagerManager managerManager;
	
	private static UserManager instance;
	private UserManager() {
		clientManager = ClientManager.getInstance();
		beauticianManager = BeauticianManager.getInstance();
		receptionistManager = ReceptionistManager.getInstance();
		managerManager = ManagerManager.getInstance();
	}
	public static UserManager getInstance() {
		if (instance == null)
			instance = new UserManager();
		return instance;
	}
	
	public void addUser(User user){
		if     (user instanceof Client)
			clientManager.addClient((Client) user);
		else if(user instanceof Beautician)
			beauticianManager.addBeautician((Beautician) user);
		else if(user instanceof Receptionist)
			receptionistManager.addReceptionist((Receptionist) user);
		else if(user instanceof Manager)
			managerManager.addManager((Manager) user);
		else
			System.err.println("User class " + user.getClass().getName() + " not handled!");
	}
	
	public void updateUser(User user){
		if     (user instanceof Client)
			clientManager.updateClient((Client) user);
		else if(user instanceof Beautician)
			beauticianManager.updateBeautician((Beautician) user);
		else if(user instanceof Receptionist)
			receptionistManager.updateReceptionist((Receptionist) user);
		else if(user instanceof Manager)
			managerManager.updateManager((Manager) user);
		else
			System.err.println("User class " + user.getClass().getName() + " not handled!");
	}
	
	public boolean removeUser(User user){
		if     (user instanceof Client)
			return clientManager.removeClient(user.getID());
		else if(user instanceof Beautician)
			return beauticianManager.removeBeautician(user.getID());
		else if(user instanceof Receptionist)
			return receptionistManager.removeReceptionist(user.getID());
		else if(user instanceof Manager)
			return managerManager.removeManager(user.getID());
		else {
			System.err.println("User class " + user.getClass().getName() + " not handled!");
			return false;
		}
	}
	public boolean removeUser(UUID id){
		return clientManager.removeClient(id) || beauticianManager.removeBeautician(id) ||
			   receptionistManager.removeReceptionist(id) || managerManager.removeManager(id);
	}
	public User getUser(UUID id) {
		if     (clientManager.contains(id))
			return clientManager.getClient(id);
		else if(beauticianManager.contains(id))
			return beauticianManager.getBeautician(id);
		else if(receptionistManager.contains(id))
			return receptionistManager.getReceptionist(id);
		else if(managerManager.contains(id))
			return managerManager.getManager(id);
		else
			return null;
	}
	public ArrayList<User> getAllUsers(){
		ArrayList<User> users = new ArrayList<User>();
		for(Client client : clientManager.getAllClients().values())
			users.add(client);
		for(Beautician beautician : beauticianManager.getAllBeauticians().values())
			users.add(beautician);
		for(Receptionist receptionist : receptionistManager.getAllReceptionists().values())
			users.add(receptionist);
		for(Manager manager : managerManager.getAllManagers().values())
			users.add(manager);
		return users;
	}
	
	public void loadAllUsers() {
		clientManager.loadClients();
		beauticianManager.loadBeauticians();
		receptionistManager.loadReceptionists();
		managerManager.loadManagers();
	}
	public void saveAllUsers() {
		clientManager.saveClients();
		beauticianManager.saveBeauticians();
		receptionistManager.saveReceptionists();
		managerManager.saveManagers();
	}
	
	public boolean login(String username, String password) {
		ArrayList<User> allUsers = getAllUsers();
		for(User user : allUsers) {
			if (user.isDeleted())
				continue;
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				Application.currentUser = user;
				return true;
			}
		}
		return false;
	}
	public void logout() {
		Application.currentUser = null;
	}
}
