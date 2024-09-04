package salon;

import java.time.LocalDateTime;
import java.util.UUID;

import managers.BeauticianManager;
import managers.ClientManager;
import managers.SalonManager;
import managers.TreatmentManager;
import users.Beautician;
import users.Client;

public class Treatment {
	public static final String HEADER = "ID, Offer, Beautician, Client, Start Time, Treatment Status, Price";
	private UUID id;
	
	private TreatmentOffer treatmentOffer;
	private Beautician beautician;
	private Client client;
	private LocalDateTime startTime;
	private TreatmentStatus treatmentStatus;
	private double price, itCostedClient;
	
	public Treatment() {
		id = UUID.randomUUID();
	}
	public Treatment(TreatmentOffer treatmentOffer, Beautician beautician, 
			Client client, LocalDateTime startTime, TreatmentStatus treatmentStatus, double price) {
		id = UUID.randomUUID();
		
		this.treatmentOffer = treatmentOffer;
		this.beautician = beautician;
		this.client = client;
		this.startTime = startTime;
		this.treatmentStatus = treatmentStatus;
		this.price = price;
	}
	
	public static Treatment parse(String csvData) {
		String[] values = csvData.split(" *, *");
		TreatmentOffer offer = TreatmentManager.getInstance().getTreatmentOfferByName(values[1]);
		Beautician beautician = BeauticianManager.getInstance().getBeauticianByUsername(values[2]);
		Client client = ClientManager.getInstance().getClientByUsername(values[3]);
		Treatment treatment = new Treatment(offer, beautician, client, LocalDateTime.parse(values[4]), TreatmentStatus.parse(values[5]), Double.parseDouble(values[6]));
		if (!values[0].equals(""))
			treatment.setID(UUID.fromString(values[0]));
		treatment.update();
		return treatment;
	}
	public static String parse(Treatment treatment) {
		return String.join(", ", treatment.getID().toString(), treatment.treatmentOffer.getName(), treatment.beautician.getUsername(), 
								 treatment.client.getUsername(), treatment.startTime.toString(), treatment.treatmentStatus.toString(), Double.toString(treatment.price));
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}
	public UUID getID() {
		return id;
	}
	private void setID(UUID id) {
		this.id = id;
	}
	public LocalDateTime getEndTime() {
		return startTime.plusMinutes(treatmentOffer.getLenght().getMinute() + 60 * treatmentOffer.getLenght().getHour());
	}
	public void update() {
		switch(treatmentStatus) {
		case ZAKAZAN:
			if (startTime.compareTo(LocalDateTime.now()) < 0)
				treatmentStatus = TreatmentStatus.IZVRŠEN;
		case IZVRŠEN:
		case NIJE_SE_POJAVIO:
			itCostedClient = price;
			break;
		case OTKAZAO_KLIJENT:
			itCostedClient = price * 0.1;
			break;
		case OTKAZAO_SALON:
			itCostedClient = 0;
			break;
		}
	}
	public void cancelByClient() {
		if (treatmentStatus.noMoneyLost())
			SalonManager.getInstance().addBalance(-0.9 * price);
		else if(treatmentStatus == TreatmentStatus.OTKAZAO_SALON)
			SalonManager.getInstance().addBalance(0.1 * price);
		setTreatmentStatus(TreatmentStatus.OTKAZAO_KLIJENT);
		update();
	}
	public void cancelBySalon() {
		if (treatmentStatus.noMoneyLost())
			SalonManager.getInstance().addBalance(-price);
		else if(treatmentStatus == TreatmentStatus.OTKAZAO_KLIJENT)
			SalonManager.getInstance().addBalance(-0.1 * price);
		setTreatmentStatus(TreatmentStatus.OTKAZAO_SALON);
		update();
	}
	public void complete() {
		if(treatmentStatus == TreatmentStatus.OTKAZAO_KLIJENT)
			SalonManager.getInstance().addBalance(0.9 * price);
		if(treatmentStatus == TreatmentStatus.OTKAZAO_SALON)
			SalonManager.getInstance().addBalance(price);
		setTreatmentStatus(TreatmentStatus.IZVRŠEN);
		update();
	}
	public void didntShowUp() {
		if(treatmentStatus == TreatmentStatus.OTKAZAO_KLIJENT)
			SalonManager.getInstance().addBalance(0.9 * price);
		if(treatmentStatus == TreatmentStatus.OTKAZAO_SALON)
			SalonManager.getInstance().addBalance(price);
		setTreatmentStatus(TreatmentStatus.NIJE_SE_POJAVIO);
		update();
	}
	
	
	// Getter and setters
	public TreatmentOffer getTreatmentOffer() {
		return treatmentOffer;
	}
	public void setTreatmentOffer(TreatmentOffer treatmentOffer) {
		this.treatmentOffer = treatmentOffer;
	}
	public Beautician getBeautician() {
		return beautician;
	}
	public void setBeautician(Beautician beautician) {
		this.beautician = beautician;
	}
	public Client getClient() {
		return client;
	}
	public void setClient(Client client) {
		this.client = client;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public TreatmentStatus getTreatmentStatus() {
		return treatmentStatus;
	}
	public void setTreatmentStatus(TreatmentStatus treatmentStatus) {
		this.treatmentStatus = treatmentStatus;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getItCostedClient() {
		update();
		return itCostedClient;
	}
	public void setItCostedClient(double itCostedClient) {
		this.itCostedClient = itCostedClient;
	}
}
