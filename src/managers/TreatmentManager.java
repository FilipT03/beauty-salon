package managers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import salon.Salon;
import salon.Treatment;
import salon.TreatmentException;
import salon.TreatmentOffer;
import salon.TreatmentStatus;
import salon.TreatmentType;
import users.Beautician;
import users.Client;
import users.Receptionist;

public class TreatmentManager {
	public final String TREATMENTS_FILE_PATH = "./data/treatments.csv";
	public final String TREATMENT_OFFERS_FILE_PATH = "./data/treatment_offers.csv";
	public final String TREATMENT_TYPES_FILE_PATH = "./data/treatment_types.csv";
	
	private HashMap<UUID, TreatmentOffer> treatmentOffers = new HashMap<>();
	private HashMap<UUID, Treatment> treatments = new HashMap<>();
	private ArrayList<TreatmentType> treatmentTypes = new ArrayList<>();
	
	private static TreatmentManager instance;
	private TreatmentManager() {};
	public static TreatmentManager getInstance() {
		if (instance == null)
			instance = new TreatmentManager();
		return instance;
	}
	
	
	// Treatment CRUD
	public boolean addTreatment(Treatment treatment) {
		if(treatments.containsKey(treatment.getID()))
			return false;
		treatments.put(treatment.getID(), treatment);
		return true;
	}
	public void updateTreatment(Treatment treatment) {
		treatments.replace(treatment.getID(), treatment);
	}
	public boolean removeTreatment(Treatment treatment) {
		if(treatments.containsKey(treatment.getID()))
			treatments.remove(treatment.getID());
		else
			return false;
		return true;
	}
	public boolean removeTreatment(UUID id) {
		if(treatments.containsKey(id))
			treatments.remove(id);
		else
			return false;
		return true;
	}
	public Treatment getTreatment(UUID id) {
		return treatments.get(id);
	}
	public HashMap<UUID, Treatment> getAllTreatments() {
		return treatments;
	}
	public ArrayList<Treatment> getAllTreatmentsList() {
		ArrayList<Treatment> result = new ArrayList<>();
		for (Treatment t : treatments.values())
			result.add(t);
		return result;
	}
	public Treatment scheduleTreatment(TreatmentOffer treatmentOffer, Beautician beautician, Client client, 
									 LocalDateTime startTime, Receptionist receptionist) throws TreatmentException{
		double price = client.hasLoyaltyCard() ? treatmentOffer.getPrice() * 0.9 : treatmentOffer.getPrice();
		if (!beautician.getSpecializations().contains(treatmentOffer.getTreatmentType()))
			throw new TreatmentException("Beautician is not qualified for that offer.");
		if(!isFree(startTime, treatmentOffer, beautician))
			throw new TreatmentException("Beautician is not free in the given period.");
		Treatment newTreatment = new Treatment(treatmentOffer, beautician, client, startTime, TreatmentStatus.ZAKAZAN, price);
		beautician.addTreatment(newTreatment);
		client.addTreatment(newTreatment);
		if (receptionist != null)
			receptionist.increaseTreatmentsScheduled();
		SalonManager.getInstance().addBalance(price);
		addTreatment(newTreatment);
		return newTreatment;
	}
	public boolean isFree(LocalDateTime startTime, TreatmentOffer offer, Beautician beautician) {
		Salon salon = SalonManager.getInstance().getSalon();
		int day = startTime.getDayOfWeek().getValue() - 1;
		LocalDateTime endTime = startTime.plusMinutes(offer.getLenght().getMinute() + 60 * offer.getLenght().getHour());
		if (salon.getOpenTime(day).compareTo(startTime.toLocalTime()) > 0)
			return false;
		if (salon.getCloseTime(day).compareTo(endTime.toLocalTime()) < 0)
			return false;
		for (Treatment treatment : treatments.values())
			if(treatment.getBeautician() == beautician) {
				if (treatment.getEndTime().compareTo(startTime) > 0 && treatment.getStartTime().compareTo(endTime) < 0)
					return false;
			}
		return true;
	}
	public Treatment scheduleTreatment(TreatmentOffer treatmentOffer, Client client, LocalDateTime startTime, Receptionist receptionist) throws TreatmentException{
		double price = client.hasLoyaltyCard() ? treatmentOffer.getPrice() * 0.9 : treatmentOffer.getPrice();
		Beautician beautician = BeauticianManager.getInstance().getOneQualifiedAndFreeBeautician(treatmentOffer, startTime);
		if (beautician == null)
			throw new TreatmentException("There aren't any free qualified beauticians.");
		Treatment newTreatment = new Treatment(treatmentOffer, beautician, client, startTime, TreatmentStatus.ZAKAZAN, price);
		beautician.addTreatment(newTreatment);
		client.addTreatment(newTreatment);
		if (receptionist != null)
			receptionist.increaseTreatmentsScheduled();
		SalonManager.getInstance().addBalance(price);
		addTreatment(newTreatment);
		return newTreatment;
	}
	public void updateAllTreatments() {
		for (Treatment treatment : treatments.values())
			treatment.update();
	}
	public void loadTreatments(){
		loadTreatments(TREATMENTS_FILE_PATH);
	}
	public void loadTreatments(String path){
		HashMap<UUID, Treatment> newTreatments = new HashMap<>();
		Treatment newTreatment;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newTreatment = Treatment.parse(line);
				newTreatments.put(newTreatment.getID(), newTreatment);
			}
			br.close();
			
			treatments.clear();
			treatments = newTreatments;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveTreatments(){
		saveTreatments(TREATMENTS_FILE_PATH);
	}
	public void saveTreatments(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(Treatment.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(Treatment treatment : treatments.values()) {
				bw.write(Treatment.parse(treatment));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	
	// TreatmentOffer CRUD
	public boolean addTreatmentOffer(TreatmentOffer treatmentOffer) {
		if(treatmentOffers.containsKey(treatmentOffer.getID()))
			return false;
		treatmentOffers.put(treatmentOffer.getID(), treatmentOffer);
		return true;
	}
	public void updateTreatmentOffer(TreatmentOffer treatment) {
		treatmentOffers.replace(treatment.getID(), treatment);
	}
	public boolean removeTreatmentOffer(TreatmentOffer treatment) {
		if(treatmentOffers.containsKey(treatment.getID()))
			treatmentOffers.remove(treatment.getID());
		else
			return false;
		return true;
	}
	public boolean removeTreatmentOffer(UUID id) {
		if(treatmentOffers.containsKey(id))
			treatmentOffers.remove(id);
		else
			return false;
		return true;
	}
	public TreatmentOffer getTreatmentOffer(UUID id) {
		return treatmentOffers.get(id);
	}
	public HashMap<UUID, TreatmentOffer> getAllTreatmentOffers() {
		return treatmentOffers;
	}
	// returns the first if there are multiple
	public TreatmentOffer getTreatmentOfferByName(String name) {
		for(TreatmentOffer offer : treatmentOffers.values())
			if (offer.getName().equals(name))
				return offer;
		return null;
	}
	public void loadTreatmentOffers(){
		loadTreatmentOffers(TREATMENT_OFFERS_FILE_PATH);
	}
	public void loadTreatmentOffers(String path){
		HashMap<UUID, TreatmentOffer> newTreatmentOffers = new HashMap<>();
		TreatmentOffer newTreatmentOffer;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newTreatmentOffer = TreatmentOffer.parse(line);
				newTreatmentOffers.put(newTreatmentOffer.getID(), newTreatmentOffer);
			}
			br.close();
			
			treatmentOffers.clear();
			treatmentOffers = newTreatmentOffers;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveTreatmentOffers(){
		saveTreatmentOffers(TREATMENT_OFFERS_FILE_PATH);
	}
	public void saveTreatmentOffers(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(TreatmentOffer.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(TreatmentOffer treatmentOffer : treatmentOffers.values()) {
				bw.write(TreatmentOffer.parse(treatmentOffer));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public ArrayList<TreatmentOffer> getActiveTreatmentOffers() {
		ArrayList<TreatmentOffer> offers = new ArrayList<>();
		for (TreatmentOffer offer : treatmentOffers.values())
			if (!offer.isDeleted())
				offers.add(offer);
		return offers;
	}
	public ArrayList<TreatmentOffer> getAllTreatmentOffersArrayList() {
		ArrayList<TreatmentOffer> offers = new ArrayList<>();
		for (TreatmentOffer offer : treatmentOffers.values())
			offers.add(offer);
		return offers;
	}
	

	// TreatmentType CRUD
	public void addTreatmentType(TreatmentType type) {
		if(!treatmentTypes.contains(type))
			treatmentTypes.add(type);
	}
	public boolean updateTreatmentType(TreatmentType oldType, TreatmentType newType) {
		if (treatmentTypes.contains(oldType)) {
			int index = treatmentTypes.indexOf(oldType);
			TreatmentType type = treatmentTypes.get(index);
			type.name = newType.name;
			type.setDeleted(newType.isDeleted());
			treatmentTypes.set(index, type);
			return true;
		}
		return false;
	}
	public boolean removeTreatmentType(TreatmentType type) {
		if(!treatmentTypes.contains(type))
			return false;
		for(TreatmentOffer offer : treatmentOffers.values()) 
			if (offer.getTreatmentType().equals(type))
				offer.setDeleted(true);
		type.setDeleted(true);
		return true;
	}
	public boolean forceRemoveTreatmentType(TreatmentType type) {
		if(!treatmentTypes.contains(type))
			return false;
		treatmentTypes.remove(type);
		return true;
	}
	public TreatmentType getTreatmentType(String name) {
		for (TreatmentType type : treatmentTypes)
			if (type.name.equals(name))
				return type;
		return null;
	}
	public ArrayList<TreatmentType> getAllTreatmentTypes() {
		return treatmentTypes;
	}
	public TreatmentType[] getAllTreatmentTypesArray() {
		TreatmentType[] result = new TreatmentType[treatmentTypes.size()];
		int i = 0;
		for (TreatmentType t : treatmentTypes)
			result[i++] = t;
		return result;
	}
	public ArrayList<TreatmentType> getActiveTreatmentTypes() {
		ArrayList<TreatmentType> types = new ArrayList<>();
		for (TreatmentType t : treatmentTypes)
			if (!t.isDeleted())
				types.add(t);
		return types;
	}
	public TreatmentType[] getActiveTreatmentTypesArray() {
		ArrayList<TreatmentType> types = new ArrayList<>();
		for (TreatmentType t : treatmentTypes)
			if (!t.isDeleted())
				types.add(t);
		TreatmentType[] result = new TreatmentType[types.size()];
		int i = 0;
		for (TreatmentType t : types)
			result[i++] = t;
		return result;
	}
	public void setAllTreatmentTypes(ArrayList<TreatmentType> treatmentTypes) {
		this.treatmentTypes = treatmentTypes;
	}
	public void loadTreatmentTypes(){
		loadTreatmentTypes(TREATMENT_TYPES_FILE_PATH);
	}
	public void loadTreatmentTypes(String path){
		ArrayList<TreatmentType> newTreatmentTypes = new ArrayList<>();
		TreatmentType newTreatmentType;
		BufferedReader br;
		try {
			File file = new File(path);
			if(!file.exists())
				return;
			br = new BufferedReader(new FileReader(file));
			String line = br.readLine(); // Read the header
			while((line = br.readLine()) != null) {
				newTreatmentType = TreatmentType.parse(line);
				newTreatmentTypes.add(newTreatmentType);
			}
			br.close();
			
			treatmentTypes.clear();
			treatmentTypes = newTreatmentTypes;
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	public void saveTreatmentTypes(){
		saveTreatmentTypes(TREATMENT_TYPES_FILE_PATH);
	}
	public void saveTreatmentTypes(String path) {
		BufferedWriter bw;
		try {
			File file = new File(path);
			if(!file.exists())
				file.createNewFile();
			bw = new BufferedWriter(new FileWriter(file));
			bw.write(TreatmentType.HEADER); // Write the header
			bw.write(System.lineSeparator());
			for(TreatmentType treatmentType : treatmentTypes) {
				bw.write(TreatmentType.parse(treatmentType));
				bw.write(System.lineSeparator());
			}
			bw.close();
		} catch (IOException e) {
			System.err.println("An error occured.");
			e.printStackTrace();
		}
	}
	
}
