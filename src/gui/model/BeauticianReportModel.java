package gui.model;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import users.Beautician;

public class BeauticianReportModel extends AbstractTableModel {
	private static final long serialVersionUID = 173122351138550735L;
	private String[] columnNames = {"Kozmetičar", "Prihodovao", "Uspešnih tretmana"};

	ArrayList<Beautician> beauticians;
	ArrayList<Double> incomes;
	ArrayList<Integer> completeds;
	
	public BeauticianReportModel(ArrayList<Beautician> beauticians, ArrayList<Double> incomes, ArrayList<Integer> completeds){
		this.beauticians = beauticians;
		this.incomes = incomes;
		this.completeds = completeds;
	}

	@Override
	public int getRowCount() {
		return beauticians.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return beauticians.get(rowIndex);
		case 1:
			return incomes.get(rowIndex);
		case 2:
			return completeds.get(rowIndex);
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
	
	public void updateData(ArrayList<Beautician> beauticians, ArrayList<Double> incomes, ArrayList<Integer> completeds) {
		this.beauticians = beauticians;
		this.incomes = incomes;
		this.completeds = completeds;
	}

}
