package users;

import java.time.LocalDate;
import java.util.UUID;

import managers.SalonManager;

public class Receptionist extends User{
	public static final String HEADER = "ID, Name, Surname, Phone, Adress, Username, Password, Gender, Qualification level, Work experience, Salary, Treatments Scheduled, Deleted, Date of Deletion";
	
	private short qualificationLevel;
	private float workExperience;
	private int salary;
	private int treatmentsScheduled;
	private LocalDate dateOfDeletion;

	public Receptionist (){
		super();
	}

	public Receptionist(String name,              String surname,       String phone, String adress, 
			            String username,          String password,      Gender gender, 
			            short qualificationLevel, float workExperience, int salary, int treatmentsScheduled) {
		super();
		
		this.name               = name;
		this.surname            = surname;
		this.phone              = phone;
		this.adress             = adress;
		this.username           = username;
		this.password           = password;
		this.gender             = gender;
		this.qualificationLevel = qualificationLevel;
		this.workExperience     = workExperience;
		if(salary > 0) 
			this.salary         = salary;
		else
			updateSalary();
		this.treatmentsScheduled = treatmentsScheduled;
	}
	
	public void updateSalary() {
		this.salary = SalonManager.getInstance().calculateReceptionistSalary(qualificationLevel, workExperience);
	}

	public static Receptionist parse(String csvData) {
		String[] values = csvData.split(" *, *");
		Receptionist receptionist = new Receptionist(values[1], values[2], values[3], values[4], values[5], values[6], values[7].equals("M") ? Gender.male : Gender.female,
								Short.parseShort(values[8]), Float.parseFloat(values[9]), Integer.parseInt(values[10]), Integer.parseInt(values[11]));
		receptionist.setDeleted(Boolean.getBoolean(values[12]));
		if (!values[13].equals("Not deleted"))
			receptionist.setDateOfDeletion(LocalDate.parse(values[13]));
		if (!values[0].equals(""))
			receptionist.setID(UUID.fromString(values[0]));
		return receptionist;
	}

	public static String parse(Receptionist receptionist) {
		return String.join(", ", receptionist.getID().toString(), receptionist.name, receptionist.surname, receptionist.phone, receptionist.adress, 
								 receptionist.username, receptionist.password, receptionist.gender.toString(), Short.toString(receptionist.qualificationLevel), 
								 Float.toString(receptionist.workExperience), Integer.toString(receptionist.salary), 
								 Integer.toString(receptionist.treatmentsScheduled), Boolean.toString(receptionist.deleted), 
								 receptionist.dateOfDeletion == null ? "Not deleted" : receptionist.dateOfDeletion.toString());
	}
	public static String isValid(Receptionist receptionist) {
		if (receptionist.name.equals("") || receptionist.name.equals(null))
			return "Ime ne sme biti prazno!";
		if (receptionist.surname.equals("") || receptionist.surname.equals(null))
			return "Prezime ne sme biti prazno!";
		if (receptionist.username.equals("") || receptionist.username.equals(null))
			return "Korisničko ime ne sme biti prazno!";
		if (receptionist.phone.equals("") || receptionist.phone.equals(null))
			return "Telefon ne sme biti prazan!";
		if (receptionist.phone.length() < 9)
			return "Telefon ne sme biti kraći od 9 karaktera";
		if (receptionist.password.equals("") || receptionist.password.equals(null))
			return "Šifra ne sme biti prazna!";
		if (receptionist.gender == null)
			return "Pol ne sme biti prazan!";
		if (receptionist.qualificationLevel < 0)
			return "Nivo st. spreme ne sme biti prazan!";
		if (receptionist.workExperience < 0)
			return "Staž ne sme biti prazan!";
		return null;
	}
	
	public double calculateBonusForMonth(int year, int month) {
		double bonus = 0;

		LocalDate monthStartDate = LocalDate.of(year, month, 1);

		if(dateOfDeletion == null || dateOfDeletion.compareTo(monthStartDate) > 0)
			if (treatmentsScheduled >= 10) 
				bonus += SalonManager.getInstance().getSalon().getReceptionistBonus();
		
		return bonus;
	}

	
	// Getter and setters
	public short getQualificationLevel() {
		return qualificationLevel;
	}

	public void setQualificationLevel(short qualificationLevel) {
		this.qualificationLevel = qualificationLevel;
		updateSalary();
	}

	public float getWorkExperience() {
		return workExperience;
	}

	public void setWorkExperience(float workExperience) {
		this.workExperience = workExperience;
		updateSalary();
	}
	public int getSalary(boolean update) {
		if (update)
			updateSalary();
		return salary;
	}
	public void setSalary(int salary) {
		this.salary = salary;
	}
	public int getTreatmentsScheduled() {
		return treatmentsScheduled;
	}
	public void setTreatmentsScheduled(int treatmentsScheduled) {
		this.treatmentsScheduled = treatmentsScheduled;
	}
	public void increaseTreatmentsScheduled() {
		this.treatmentsScheduled++;
	}
	public LocalDate getDateOfDeletion() {
		return dateOfDeletion;
	}
	public void setDateOfDeletion(LocalDate dateOfDeletion) {
		this.dateOfDeletion = dateOfDeletion;
	}
}
