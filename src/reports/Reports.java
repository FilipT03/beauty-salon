package reports;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import managers.BeauticianManager;
import managers.ClientManager;
import managers.ReceptionistManager;
import managers.SalonManager;
import managers.TreatmentManager;
import salon.Treatment;
import salon.TreatmentOffer;
import salon.TreatmentStatus;
import salon.TreatmentType;
import users.Beautician;
import users.Client;

public class Reports {
	public static double getExpensesForMonth(int year, int month) {
		double expense = 0;
		expense += BeauticianManager.getInstance().calculateAllSalaries(year, month);
		expense += BeauticianManager.getInstance().calculateAllBonuses(year, month);
		expense += ReceptionistManager.getInstance().calculateAllSalaries(year, month);
		expense += ReceptionistManager.getInstance().calculateAllBonuses(year, month);
		return expense;
	}
	public static double getIncomeForMonth(int year, int month) {
		double income = 0;
		for (Client client : ClientManager.getInstance().getAllClients().values())
			income += client.calculateMoneySpentInMonth(year, month);
		return income;
	}
	public static int treatmentDoneByBetween(Beautician beautician, LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime))
			return 0;
		int completed = 0;
		for (Treatment treatment : beautician.getTreatments()) 
			if (treatment.getTreatmentStatus().noMoneyLost() && treatment.getStartTime().compareTo(startTime) > 0 
					&& treatment.getEndTime().compareTo(endTime) < 0)
				completed++;
		return completed;
	}
	public static double incomeEarnedByBetween(Beautician beautician, LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime))
			return 0;
		double income = 0;
		for (Treatment treatment : beautician.getTreatments()) 
			if (treatment.getStartTime().compareTo(startTime) > 0 
			 && treatment.getEndTime().compareTo(endTime) < 0) {
				treatment.update();
				income += treatment.getItCostedClient();
			}
		return income;
	}
	public static int treatmentWithStatusBetween(TreatmentStatus status, LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime))
			return 0;
		int result = 0;
		for (Treatment treatment : TreatmentManager.getInstance().getAllTreatments().values()) 
			if (treatment.getTreatmentStatus().equals(status) && treatment.getStartTime().compareTo(startTime) > 0 
															  && treatment.getEndTime().compareTo(endTime) < 0)
				result++;
		return result;
	}
	public static int[] analyzeTreatmentOffer(TreatmentOffer offer, LocalDateTime startTime, LocalDateTime endTime) {
		if (startTime.isAfter(endTime))
			return null;
		int result = 0;
		int income = 0;
		for (Treatment treatment : TreatmentManager.getInstance().getAllTreatments().values()) {
			if (treatment.getTreatmentOffer() == offer && treatment.getStartTime().compareTo(startTime) > 0 
												 	   && treatment.getEndTime().compareTo(endTime) < 0) {
				treatment.update();
				income += treatment.getItCostedClient();
				if (treatment.getTreatmentStatus().equals(TreatmentStatus.ZAKAZAN))
					result++;
			}
		}
		return new int[]{result, income};
//		System.out.println("Zakazanih tretmanata za ponudu: " + result);
//		System.out.println("Prihod od ponude: " + income);
	}
	public static ArrayList<Client> clientsThatSatisfyLoyaltyMinimum() {
		ArrayList<Client> result = new ArrayList<>();
		int minimum = SalonManager.getInstance().getSalon().getLoyaltyMinimum();
		HashMap<UUID, Client> allClients = ClientManager.getInstance().getAllClients();
		for (Client client: allClients.values()) 
			if (client.calculateMoneySpent() >= minimum)
				result.add(client);
		return result;
	}
	public static Double[] incomeByTypeLast12months(TreatmentType type) {
		LocalDateTime endTime = LocalDateTime.now();
		LocalDateTime startTime = LocalDateTime.now().minusYears(1);
		LocalDate start = LocalDate.now();
		int[] decoder = new int[12];
		for (int i=11;i>=0;i--) {
			decoder[start.getMonthValue()-1] = i;
			start = start.minusMonths(1);
		}
		double[] result = new double[12];
		for (Treatment treatment : TreatmentManager.getInstance().getAllTreatments().values()) {
			if (type != null && treatment.getTreatmentOffer().getTreatmentType() != type)
				continue;
			if (treatment.getStartTime().compareTo(startTime) > 0 && treatment.getEndTime().compareTo(endTime) < 0) {
				treatment.update();
				result[decoder[treatment.getStartTime().getMonthValue() - 1]] += treatment.getItCostedClient();
			}
		}
		Double[] result2 = new Double[12];
		for (int i=0;i<12;i++)
			result2[i] = result[i];
		return result2;
	}
}
