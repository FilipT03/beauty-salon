package gui.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.swing.table.AbstractTableModel;

import salon.Treatment;

public class TreatmentModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private String[] columnNames = {"ID", "Tretman", "Tip", "Kozmetičar", "Klijent", "Početak", "Dužina", "Status", "Cena tretmana", "Vaš deo"};
	ArrayList<Treatment> treatmentsList;
	HashMap<UUID, Treatment> treatmentsMap;

	public TreatmentModel(ArrayList<Treatment> treatments) {
		this.treatmentsList = treatments;
	}
	public TreatmentModel(HashMap<UUID, Treatment> treatments) {
		this.treatmentsMap = treatments;
	}

	@Override
	public int getRowCount() {
		if (treatmentsList != null)
			return treatmentsList.size();
		else
			return treatmentsMap.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Treatment t;
		if (treatmentsList != null)
			t = treatmentsList.get(rowIndex);
		else
			t = treatmentsMap.get(treatmentsMap.keySet().toArray()[rowIndex]);
		switch (columnIndex) {
		case 0:
			return t.getID();
		case 1:
			return t.getTreatmentOffer();
		case 2:
			return t.getTreatmentOffer().getTreatmentType();
		case 3:
			return t.getBeautician();
		case 4:
			return t.getClient();
		case 5:
			return t.getStartTime().toString().replace("T", " ");
		case 6:
			return t.getTreatmentOffer().getLenght();
		case 7:
			return t.getTreatmentStatus();
		case 8:
			return t.getPrice();
		case 9:
			return t.getItCostedClient();
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
		return this.getValueAt(0, columnIndex).getClass();
	}
	
	public void setColumnName(int column, String name) {
		this.columnNames[column] = name;
	}

}
