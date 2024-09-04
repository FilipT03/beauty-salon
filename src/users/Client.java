package users;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import salon.Treatment;

public class Client extends User{
	public static final String HEADER = "ID, Name, Surname, Phone, Adress, Username, Password, Gender, Loyalty Card, Deleted";
	private boolean loyaltyCard;
	private ArrayList<Treatment> treatments;

	public Client() {
		super();
	}

	public Client(String name,   String surname,  String phone, String adress, 
			String username, String password, Gender gender, boolean loyaltyCard) {
		super();
		
		this.name        = name;
		this.surname     = surname;
		this.phone       = phone;
		this.adress      = adress;
		this.username    = username;
		this.password    = password;
		this.gender      = gender;
		this.loyaltyCard = loyaltyCard;
		this.treatments  = new ArrayList<>();
	}

	public static Client parse(String csvData) {
		String[] values = csvData.split(" *, *");
		Client client = new Client(values[1], values[2], values[3], values[4], values[5], values[6], 
				values[7].equals("M") ? Gender.male : Gender.female, Boolean.parseBoolean(values[8]));
		client.setDeleted(Boolean.getBoolean(values[9]));
		if (!values[0].equals(""))
			client.setID(UUID.fromString(values[0]));
		return client;
	}
	public static String parse(Client client) {
		return String.join(", ", client.getID().toString(), client.name, client.surname, client.phone, client.adress, 
				client.username, client.password, client.gender.toString(), Boolean.toString(client.loyaltyCard), Boolean.toString(client.deleted));
	}
	
	public static String isValid(Client client) {
		if (client.name.equals("") || client.name.equals(null))
			return "Ime ne sme biti prazno!";
		if (client.surname.equals("") || client.surname.equals(null))
			return "Prezime ne sme biti prazno!";
		if (client.phone.equals("") || client.phone.equals(null))
			return "Telefon ne sme biti prazan!";
		if (client.phone.length() < 9)
			return "Telefon ne sme biti kraći od 9 karaktera";
		if (client.password.equals("") || client.password.equals(null))
			return "Šifra ne sme biti prazna!";
		if (client.username.equals("") || client.username.equals(null))
			return "Korisničko ime ne sme biti prazno!";
		if (client.gender == null)
			return "Pol ne sme biti prazan!";
		return null;
	}
	public double calculateMoneySpent() {
		double result = 0;
		for(Treatment treatment : treatments) {
			treatment.update();
			result += treatment.getItCostedClient();
		}
		return result;
	}
	public double calculateMoneySpentInMonth(int year, int month) {
		double result = 0;

		LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0, 0);
		LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);
		
		for(Treatment treatment : treatments) {
			if (treatment.getStartTime().compareTo(monthStart) > 0 && treatment.getEndTime().compareTo(monthEnd) < 0) {
				treatment.update();
				result += treatment.getItCostedClient();
			}
		}
		
		return result;
	}
	@Override
	public String toString() {
		return name + " " + surname;
	}
	
	// Getters and setters
	public boolean hasLoyaltyCard() {
		return loyaltyCard;
	}
	public void setLoyaltyCard(boolean loyaltyCard) {
		this.loyaltyCard = loyaltyCard;
	}
	public ArrayList<Treatment> getTreatments() {
		return treatments;
	}
	public void setTreatments(ArrayList<Treatment> treatments) {
		this.treatments = treatments;
	}
	public void addTreatment(Treatment treatment) {
		this.treatments.add(treatment);
	}
	public Treatment getTreatment(int index) {
		return treatments.get(index);
	}

}
