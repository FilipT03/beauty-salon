package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import app.Application;
import users.Client;
import users.User;

public class ClientManager {
	public final String CLIENTS_FILE_PATH = "./data/clients.csv";
	
	private HashMap<UUID, Client> clients = new HashMap<>();
	
	private static ClientManager instance;
	private ClientManager() {}
	public static ClientManager getInstance() {
		if (instance == null)
			instance = new ClientManager();
		return instance;
	}
	
	public boolean addClient(Client client){
		if(clients.containsKey(client.getID()))
			return false;
		clients.put(client.getID(), client);
		return true;
	}
	
	public void updateClient(Client client){
		clients.replace(client.getID(), client);
	}
	
	public boolean removeClient(UUID id){
		if (clients.containsKey(id)) 
			clients.get(id).setDeleted(true);
		else
			return false;
		return true;
	}
	public boolean forceRemoveClient(UUID id){
		if (clients.containsKey(id)) 
			clients.remove(id);
		else
			return false;
		return true;
	}
	public boolean removeClient(Client client){
		return removeClient(client.getID());
	}
	
	public Client getClient(UUID id) {
		if (clients.containsKey(id))  
			return clients.get(id);
		else                             
			return null;
	}
	// returns the first if there are multiple
	public Client getClientByUsername(String username) {
		for(Client client : clients.values())
			if (client.getUsername().equals(username))
				return client;
		return null;
	}
	public HashMap<UUID, Client> getAllClients(){
		return clients;
	}
	public boolean contains(UUID id) {
		return clients.containsKey(id);
	}
	public void loadClients() {
		loadClients(CLIENTS_FILE_PATH);
	}
	public void loadClients(String path){
		HashMap<UUID, Client> newClients = new HashMap<>();
		Client newClient;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newClient = Client.parse(line);
				newClients.put(newClient.getID(), newClient);
			}
			br.close();
			
			clients.clear();
			clients = newClients;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveClients() {
		saveClients(CLIENTS_FILE_PATH);
	}
	public void saveClients(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Client.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(Client client : clients.values()) {
				bw.write(Client.parse(client));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public String register(Client client, boolean login) {
		String error = Client.isValid(client);
		if (error != null)
			return error;
		
		ArrayList<User> allUsers = UserManager.getInstance().getAllUsers();
		for(User user : allUsers) {
			if (user.getUsername().equals(client.getUsername()))
				return "Korisničko ime je zauzeto!";
		}
		
		if (login)
			Application.currentUser = client;
		
		return addClient(client) ? null : "Desila se greška.";
	}
	public ArrayList<Client> getAllActiveClients() {
		ArrayList<Client> result = new ArrayList<>();
		for (Client client : clients.values())
			if (!client.isDeleted())
				result.add(client);
		return result;
	}
}
