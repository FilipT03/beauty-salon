package gui.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import managers.TreatmentManager;
import salon.TreatmentOffer;

public class OfferModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private String[] columnNames = {"ID", "Ime", "Tip", "Dužina", "Cena", "U minutima", "Obrisan"};

	ArrayList<TreatmentOffer> offers;
	
	public OfferModel(){
		offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
	}

	@Override
	public int getRowCount() {
		return offers.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TreatmentOffer t = offers.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return t.getID();
		case 1:
			return t.getName();
		case 2:
			return t.getTreatmentType();
		case 3:
			return t.getLenght();
		case 4:
			return t.getPrice();
		case 5:
			return t.getLenght().getHour() * 60 + t.getLenght().getMinute();
		case 6:
			return t.isDeleted();
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
	
	public void updateData() {
		offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
	}

}
