package users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import managers.SalonManager;
import managers.TreatmentManager;
import salon.Treatment;
import salon.TreatmentType;

public class Beautician extends User{
	public static final String HEADER = "ID, Name, Surname, Phone, Adress, Username, Password, Gender, Qualification level, Work experience, Salary, Deleted, Date of Deletion, Specializations";
	
	private short qualificationLevel;
	private float workExperience;
	private int salary;
	private ArrayList<TreatmentType> specializations;
	private ArrayList<Treatment> treatments;
	private LocalDate dateOfDeletion;
	
	public Beautician (){
		super();
	}

	public Beautician (String name,              String surname,       String phone,  String adress, 
			           String username,          String password,      Gender gender, 
			           short qualificationLevel, float workExperience, int salary,
			           ArrayList<TreatmentType> specializations) {
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
		this.specializations    = specializations;
		this.treatments = new ArrayList<>();
	}

	public void updateSalary() {
		this.salary = SalonManager.getInstance().calculateBeauticianSalary(qualificationLevel, workExperience);
	}

	public static Beautician parse(String csvData) {
		String[] values = csvData.split(" *, *");
		String[] specializations = values[13].split(" *\\| *");
		ArrayList<TreatmentType> types = new ArrayList<>();
		for(int i = 0; i < specializations.length; i++)
			types.add(TreatmentManager.getInstance().getTreatmentType(specializations[i]));
		Beautician beautician =  new Beautician(values[1], values[2], values[3], values[4], values[5], values[6], values[7].equals("M") ? Gender.male : Gender.female,
							  					Short.parseShort(values[8]), Float.parseFloat(values[9]), Integer.parseInt(values[10]), types);
		beautician.setDeleted(Boolean.getBoolean(values[11]));
		if (!values[12].equals("Not deleted"))
			beautician.setDateOfDeletion(LocalDate.parse(values[12]));
		if (!values[0].equals(""))
			beautician.setID(UUID.fromString(values[0]));
		return beautician;
	}

	public static String parse(Beautician beautician) {
		String[] specializationStrings = new String[beautician.specializations.size()];
		for(int i = 0 ; i < beautician.specializations.size(); i++)
			specializationStrings[i] = beautician.specializations.get(i).name;
		String specializations = String.join("|", specializationStrings);
		return String.join(", ", beautician.getID().toString(), beautician.name, beautician.surname, beautician.phone, beautician.adress, beautician.username, 
								 beautician.password, beautician.gender.toString(), Short.toString(beautician.qualificationLevel), Float.toString(beautician.workExperience), 
								 Integer.toString(beautician.salary), Boolean.toString(beautician.deleted), 
								 beautician.dateOfDeletion == null ? "Not deleted" : beautician.dateOfDeletion.toString(), specializations);
	}
	public static String isValid(Beautician beautician) {
		if (beautician.name.equals("") || beautician.name.equals(null))
			return "Ime ne sme biti prazno!";
		if (beautician.surname.equals("") || beautician.surname.equals(null))
			return "Prezime ne sme biti prazno!";
		if (beautician.username.equals("") || beautician.username.equals(null))
			return "Korisničko ime ne sme biti prazno!";
		if (beautician.phone.equals("") || beautician.phone.equals(null))
			return "Telefon ne sme biti prazan!";
		if (beautician.phone.length() < 9)
			return "Telefon ne sme biti kraći od 9 karaktera";
		if (beautician.password.equals("") || beautician.password.equals(null))
			return "Šifra ne sme biti prazna!";
		if (beautician.gender == null)
			return "Pol ne sme biti prazan!";
		if (beautician.qualificationLevel < 0)
			return "Nivo st. spreme ne sme biti prazan!";
		if (beautician.workExperience < 0)
			return "Staž ne sme biti prazan!";
		if (beautician.specializations == null)
			return "Lista specijalizacija ne sme biti prazna!";
		return null;
	}
	public double calculateBonusForMonth(int year, int month) {
		double bonus = 0;
		
		LocalDateTime monthStart = LocalDateTime.of(year, month, 1, 0, 0, 0);
		LocalDateTime monthEnd = monthStart.plusMonths(1).minusSeconds(1);
		LocalDate monthStartDate = monthStart.toLocalDate();
		
		if (dateOfDeletion == null || dateOfDeletion.compareTo(monthStartDate) > 0) {
			int completed = 0;
			for (Treatment treatment : treatments) 
				if (treatment.getTreatmentStatus().noMoneyLost() && treatment.getStartTime().compareTo(monthStart) > 0 
																 && treatment.getEndTime().compareTo(monthEnd) < 0)
					completed++;
			if (completed >= 10)
				bonus += SalonManager.getInstance().getSalon().getBeauticianBonus();
		}
		
		return bonus;
	}
	@Override
	public String toString() {
		return name + " " + surname;
	}
	
	// Getters and setters
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
	public ArrayList<TreatmentType> getSpecializations() {
		return specializations;
	}
	public void setSpecializations(ArrayList<TreatmentType> specializations) {
		this.specializations = specializations;
	}
	public void addSpecialization(TreatmentType specialization) {
		this.specializations.add(specialization);
	}
	public void removeSpecialization(TreatmentType specialization) {
		this.specializations.remove(specialization);
	}
	public void setSpecializations(TreatmentType... types) {
		this.specializations = new ArrayList<>();
		for (TreatmentType type : types)
			this.specializations.add(type);
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
	public LocalDate getDateOfDeletion() {
		return dateOfDeletion;
	}
	public void setDateOfDeletion(LocalDate dateOfDeletion) {
		this.dateOfDeletion = dateOfDeletion;
	}
}
