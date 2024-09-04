package gui.model;

import java.util.HashMap;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import users.Beautician;
import users.Receptionist;
import users.User;

public class WorkerModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private String[] columnNames = {"ID", "Ime", "Prezime", "Telefon", "Adresa", "Korisničko ime", "Lozinka", "Pol", "Nivo st. spreme", "Staž", "Plata", "Obrisan", "Datum brisanja", "Specijalizacije/Broj zakz. tretmana"};
	HashMap<UUID, Beautician> beauticians;
	HashMap<UUID, Receptionist> receptionists;

	public WorkerModel(HashMap<UUID, Beautician> beauticians, HashMap<UUID, Receptionist> receptionists) {
		this.beauticians = beauticians;
		this.receptionists = receptionists;
	}

	@Override
	public int getRowCount() {
		return beauticians.size() + receptionists.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		User u;
		if (rowIndex >= beauticians.size())
			u = receptionists.get(receptionists.keySet().toArray()[rowIndex - beauticians.size()]);
		else
			u = beauticians.get(beauticians.keySet().toArray()[rowIndex]);
		switch (columnIndex) {
		case 0:
			return u.getID();
		case 1:
			return u.getName();
		case 2:
			return u.getSurname();
		case 3:
			return u.getPhone();
		case 4:
			return u.getAdress();
		case 5:
			return u.getUsername();
		case 6:
			return u.getPassword();
		case 7:
			return u.getGender();
		case 8:
			if (u instanceof Beautician)
				return ((Beautician)u).getQualificationLevel();
			else
				return ((Receptionist)u).getQualificationLevel();
		case 9:
			if (u instanceof Beautician)
				return ((Beautician)u).getWorkExperience();
			else
				return ((Receptionist)u).getWorkExperience();
		case 10:
			if (u instanceof Beautician)
				return ((Beautician)u).getSalary(true);
			else
				return ((Receptionist)u).getSalary(true);
		case 11:
			if (u instanceof Beautician)
				return ((Beautician)u).isDeleted();
			else
				return ((Receptionist)u).isDeleted();
		case 12:
			if (u instanceof Beautician)
				if (((Beautician)u).getDateOfDeletion() != null)
					return ((Beautician)u).getDateOfDeletion();
				else
					return "Not deleted";
			else
				if (((Receptionist)u).getDateOfDeletion() != null)
					return ((Receptionist)u).getDateOfDeletion();
				else
					return "Not deleted";
		case 13:
			if (u instanceof Beautician)
				return ((Beautician)u).getSpecializations();
			else
				return ((Receptionist)u).getTreatmentsScheduled();
		default:
			return null;
		}
	}

	@Override
	public String getColumnName(int column) {
		return this.columnNames[column];
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
//		System.out.println(getValueAt(0, columnIndex));
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public void setColumnName(int column, String name) {
		this.columnNames[column] = name;
	}

}
