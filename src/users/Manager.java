package users;

import java.util.UUID;

public class Manager extends User{
	public static final String HEADER = "ID, Name, Surname, Phone, Adress, Username, Password, Gende, Deleted";
	
	public Manager() {
		super();
	}
	public Manager(String name,   String surname,  String phone, 
		    String adress, String username, String password, Gender gender) {
		super();
		
		this.name     = name;
		this.surname  = surname;
		this.phone    = phone;
		this.adress   = adress;
		this.username = username;
		this.password = password;
		this.gender   = gender;
	}
	
	public static Manager parse(String csvData) {
		String[] values = csvData.split(" *, *");
		Manager manager = new Manager(values[1], values[2], values[3], values[4], values[5], values[6], values[7].equals("M") ? Gender.male : Gender.female);
		manager.setDeleted(Boolean.getBoolean(values[8]));
		if (!values[0].equals(""))
			manager.setID(UUID.fromString(values[0]));
		return manager;
	}

	public static String parse(Manager manager) {
		return String.join(", ", manager.getID().toString(), manager.name, manager.surname, manager.phone, manager.adress, 
				manager.username, manager.password, manager.gender.toString(), Boolean.toString(manager.deleted));
	}
}
