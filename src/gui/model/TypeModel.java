package gui.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import managers.TreatmentManager;
import salon.TreatmentType;

public class TypeModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private String[] columnNames = {"Ime", "Obrisan"};

	ArrayList<TreatmentType> types;
	
	public TypeModel(){
		types = TreatmentManager.getInstance().getAllTreatmentTypes();
	}

	@Override
	public int getRowCount() {
		return types.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		TreatmentType t = types.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return t.name;
		case 1:
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

}
