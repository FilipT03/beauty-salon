package salon;

import java.time.LocalTime;
import java.util.UUID;

import managers.TreatmentManager;

public class TreatmentOffer {
	public static final String HEADER = "ID, Name, Type, Lenght, Price, Deleted";
	private UUID id;
	
	private String name;
	private TreatmentType treatmentType;
	private LocalTime lenght;
	private double price;
	private boolean deleted;
	
	public TreatmentOffer() {
		id = UUID.randomUUID();
	}
	public TreatmentOffer(String name, TreatmentType treatmentType, LocalTime lenght, double price) {
		id = UUID.randomUUID();
		
		this.name = name;
		this.treatmentType = treatmentType;
		this.lenght = lenght;
		this.price = price;
	}
	public static TreatmentOffer parse(String csvData) {
		String[] values = csvData.split(" *, *");
		TreatmentType type = TreatmentManager.getInstance().getTreatmentType(values[2]);
		TreatmentOffer treatmentOffer = new TreatmentOffer(values[1], type, LocalTime.parse(values[3]), Double.parseDouble(values[4]));
		treatmentOffer.setDeleted(Boolean.getBoolean(values[5]));
		if (!values[0].equals(""))
			treatmentOffer.setID(UUID.fromString(values[0]));
		return treatmentOffer;
	}
	public static String parse(TreatmentOffer offer) {
		return String.join(", ", offer.getID().toString(), offer.name, offer.treatmentType.name, offer.lenght.toString(), Double.toString(offer.price), Boolean.toString(offer.deleted));
	}
	@Override
	public int hashCode() {
		return id.hashCode();
	}
	public UUID getID() {
		return id;
	}
	protected void setID(UUID id) {
		this.id = id;
	}
	@Override
	public String toString() {
		return name;
	}
	

	// Getter and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TreatmentType getTreatmentType() {
		return treatmentType;
	}
	public void setTreatmentType(TreatmentType treatmentType) {
		this.treatmentType = treatmentType;
	}
	public LocalTime getLenght() {
		return lenght;
	}
	public void setLenght(LocalTime lenght) {
		this.lenght = lenght;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
