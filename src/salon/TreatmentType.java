package salon;

import java.util.Objects;

public class TreatmentType {
	public static final String HEADER = "Name, Deleted";
	
	public String name;
	private boolean deleted;
	
	public TreatmentType(String name) {
		this.name = name;
	}
	public static TreatmentType parse(String csvData) {
		String[] values = csvData.split(" *, *");
		TreatmentType type = new TreatmentType(values[0]);
		type.setDeleted(Boolean.getBoolean(values[1]));
		return type;
	}
	public static String parse(TreatmentType type) {
		return String.join(", ", type.name, Boolean.toString(type.deleted));
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TreatmentType other = (TreatmentType) obj;
		return Objects.equals(name, other.name);
	}

	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
