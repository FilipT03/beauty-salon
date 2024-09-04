package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import salon.Treatment;
import salon.TreatmentOffer;
import users.Beautician;
import users.Client;
import users.Manager;
import users.Receptionist;
import users.User;

public class Printing {
	public static void printUsers(ArrayList<User> list)
	{
		System.out.println();
		for(User user : list){
			System.out.print(user.getClass().getSimpleName() + " ");
			if (user instanceof Client)
				System.out.println(Client.parse((Client)user).substring(38));
			else if (user instanceof Beautician)
				System.out.println(Beautician.parse((Beautician)user).substring(38));
			else if (user instanceof Receptionist)
				System.out.println(Receptionist.parse((Receptionist)user).substring(38));
			else if (user instanceof Manager)
				System.out.println(Manager.parse((Manager)user).substring(38));
		}
		System.out.println();
	}
	public static void printUser(User user)
	{
		System.out.print(user.getClass().getSimpleName() + " ");
		if (user instanceof Client)
			System.out.println(Client.parse((Client)user).substring(38));
		else if (user instanceof Beautician)
			System.out.println(Beautician.parse((Beautician)user).substring(38));
		else if (user instanceof Receptionist)
			System.out.println(Receptionist.parse((Receptionist)user).substring(38));
		else if (user instanceof Manager)
			System.out.println(Manager.parse((Manager)user).substring(38));
	}
	public static void print(HashMap<UUID, TreatmentOffer> map)
	{
		System.out.println();
		for(TreatmentOffer treatmentOffer : map.values()){
			System.out.println(treatmentOffer.getName() + " " + treatmentOffer.getPrice() + " " + treatmentOffer.getLenght() + " " + treatmentOffer.getTreatmentType().name);
		}
		System.out.println();
	}
	public static void print2(HashMap<UUID, Treatment> map)
	{
		System.out.println();
		for(Treatment treatment : map.values()){
			System.out.println(treatment.getBeautician().getUsername() + " " + treatment.getClient().getUsername() + " " + treatment.getTreatmentOffer().getName());
		}
		System.out.println();
	}
	public static void printTreatments(ArrayList<Treatment> treatments) {
		for(Treatment treatment : treatments)
			printTreatment(treatment);
		System.out.println();
		
	}
	public static void printTreatment(Treatment treatment) {
		System.out.println(Treatment.parse(treatment).substring(38));
		
	}
}
