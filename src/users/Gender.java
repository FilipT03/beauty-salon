package users;

public enum Gender {
	male, female;

	@Override
	public String toString() {
		return this == male ? "M" : "F";
	}
}
