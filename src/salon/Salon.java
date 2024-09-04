package salon;

import java.time.LocalTime;

public class Salon {
	public static final String HEADER = "Name, Mon. open time, Tue. open time, Wen. open time, Thu. open time, Fri. open time, Sat. open time, Sun. open time, "
											+ "Mon. close time, Tue. close time, Wen. close time, Thu. close time, Fri. close time, Sat. close time, Sun. close time,"
											+ "Balance, Loyalty minimum, Beautician salary base, Receptionist salary base, Beautician bonus, Receptionist bonus";
	
	private String name;
	private LocalTime[] openTime = new LocalTime[7], closeTime = new LocalTime[7];
	private double balance;
	private int loyaltyMinimum;
	private int beauticianSalaryBase, receptionistSalaryBase;
	private int beauticianBonus, receptionistBonus;
	
	public Salon() {}
	public Salon(String name, LocalTime[] openTime, LocalTime[] closeTime, double balance) {
		super();
		this.name = name;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.balance = balance;
	}
	public Salon(String name, LocalTime workdayOpenTime,  LocalTime workdayCloseTime, 
			LocalTime saturdayOpenTime, LocalTime saturdayCloseTime, LocalTime sundayOpenTime,   
			LocalTime sundayCloseTime, double balance) {
		super();
		this.name = name;
		for(int i = 0; i < 5; i++) {
			this.openTime[i]  = workdayOpenTime;
			this.closeTime[i] = workdayCloseTime;
		}
		this.openTime[5]  = saturdayOpenTime;
		this.closeTime[5] = saturdayCloseTime;
		this.openTime[6]  = sundayOpenTime;
		this.closeTime[6] = sundayCloseTime;
		this.balance = balance;
	}
	public Salon(String name, LocalTime openTime,  LocalTime closeTime, double balance) {
		super();
		this.name = name;
		for(int i = 0; i < 7; i++) {
			this.openTime[i]  = openTime;
			this.closeTime[i] = closeTime;
		}
		this.balance = balance;
	}
	public static Salon parse(String csvData) {
		String[] values = csvData.split(" *, *");
		LocalTime[] openTimes = new LocalTime[7];
		for (int i=1;i<=7;i++)
			openTimes[i-1] =  LocalTime.parse(values[i]);
		LocalTime[] closeTimes = new LocalTime[7];
		for (int i=8;i<=14;i++)
			closeTimes[i-8] =  LocalTime.parse(values[i]);
		Salon salon = new Salon(values[0], openTimes, closeTimes, Double.parseDouble(values[15]));
		salon.setLoyaltyMinimum(Integer.parseInt(values[16]));
		salon.setPayments(Integer.parseInt(values[17]), Integer.parseInt(values[18]), Integer.parseInt(values[19]), Integer.parseInt(values[20]));
		return salon;
	}
	public static String parse(Salon salon) {
		String openTimes = String.join(", ", salon.openTime[0].toString(), salon.openTime[1].toString(), salon.openTime[2].toString(), salon.openTime[3].toString(),
									   salon.openTime[4].toString(), salon.openTime[5].toString(), salon.openTime[6].toString());
		String closeTimes = String.join(", ", salon.closeTime[0].toString(), salon.closeTime[1].toString(), salon.closeTime[2].toString(), salon.closeTime[3].toString(),
				 					    salon.closeTime[4].toString(), salon.closeTime[5].toString(), salon.closeTime[6].toString());
		String payments = String.join(", ", Integer.toString(salon.loyaltyMinimum), Integer.toString(salon.beauticianSalaryBase), 
											Integer.toString(salon.receptionistSalaryBase), Integer.toString(salon.beauticianBonus), 
											Integer.toString(salon.receptionistBonus));
		return String.join(", ", salon.name, openTimes, closeTimes, Double.toString(salon.balance), payments);
	}
	
	public void setPayments(int beauticianSalaryBase, int receptionistSalaryBase, int beauticianBonuns, int receptionistBonus) {
		this.beauticianSalaryBase = beauticianSalaryBase;
		this.receptionistSalaryBase = receptionistSalaryBase;
		this.beauticianBonus = beauticianBonuns;
		this.receptionistBonus = receptionistBonus;
	}


	// Getter and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public LocalTime[] getAllOpenTimes() {
		return openTime;
	}
	public void setAllOpenTimes(LocalTime[] openTime) {
		this.openTime = openTime;
	}
	public LocalTime getOpenTime(int day) {
		return openTime[day];
	}
	public void setOpenTime(LocalTime openTime, int day) {
		this.openTime[day] = openTime;
	}
	public LocalTime[] getAllCloseTimes() {
		return closeTime;
	}
	public void setAllCloseTimes(LocalTime[] closeTime) {
		this.closeTime = closeTime;
	}
	public LocalTime getCloseTime(int day) {
		return closeTime[day];
	}
	public void setCloseTime(LocalTime closeTime, int day) {
		this.closeTime[day] = closeTime;
	}
	public int getLoyaltyMinimum() {
		return loyaltyMinimum;
	}
	public void setLoyaltyMinimum(int loyaltyMinimum) {
		this.loyaltyMinimum = loyaltyMinimum;
	}
	public double getBalance() {
		return balance;
	}
	public void setBalance(double balance) {
		this.balance = balance;
	}
	public void addBalance(double value) {
		this.balance += value;
	}
	public int getBeauticianSalaryBase() {
		return beauticianSalaryBase;
	}
	public void setBeauticianSalaryBase(int beauticianSalaryBase) {
		this.beauticianSalaryBase = beauticianSalaryBase;
	}
	public int getReceptionistSalaryBase() {
		return receptionistSalaryBase;
	}
	public void setReceptionistSalaryBase(int receptionistSalaryBase) {
		this.receptionistSalaryBase = receptionistSalaryBase;
	}
	public int getBeauticianBonus() {
		return beauticianBonus;
	}
	public void setBeauticianBonus(int beauticianBonus) {
		this.beauticianBonus = beauticianBonus;
	}
	public int getReceptionistBonus() {
		return receptionistBonus;
	}
	public void setReceptionistBonus(int receptionistBonus) {
		this.receptionistBonus = receptionistBonus;
	}
}
