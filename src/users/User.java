package users;

import java.util.UUID;

public abstract class User {
	private UUID id;
	
	protected String name, surname, phone, adress, username, password;
	protected Gender gender;
	protected boolean deleted;
	
	public User() {
		id = UUID.randomUUID();
		deleted = false;
	}
	public User(String name,   String surname,  String phone, 
			    String adress, String username, String password, Gender gender) {
		id = UUID.randomUUID();
		deleted = false;
		
		this.name     = name;
		this.surname  = surname;
		this.phone    = phone;
		this.adress   = adress;
		this.username = username;
		this.password = password;
		this.gender   = gender;
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
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return id.equals(other.getID());
	}
	
	// Getter and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAdress() {
		return adress;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Gender getGender() {
		return gender;
	}
	public void setGender(Gender gender) {
		this.gender = gender;
	}
	public boolean isDeleted() {
		return deleted;
	}
	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
}
