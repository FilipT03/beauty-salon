package gui.otherpanels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import debug.Debug;
import gui.MainFrame;
import gui.model.TreatmentModel;
import gui.otherdialogs.TreatmentEditDialog;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.Treatment;

public class ReceptionistTreatmentsPanel extends JPanel{
	private static final long serialVersionUID = -8026201049950423764L;
	
	protected JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();

	RowFilter<Object,Object> offerFilter = null, typeFilter = null, priceFilterMin = null, priceFilterMax = null;

	public ReceptionistTreatmentsPanel() {
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons()));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(new Color(209, 255, 250));
		JTextField offerField = new JTextField(8);
		JTextField typeField = new JTextField(8);
		JTextField priceFieldMin = new JTextField(8);
		JTextField priceFieldMax = new JTextField(8);
		searchPanel.add(new JLabel("Tretman:"));
		searchPanel.add(offerField);
		searchPanel.add(new JLabel("Tip:"));
		searchPanel.add(typeField);
		searchPanel.add(new JLabel("Cena od:"));
		searchPanel.add(priceFieldMin);
		searchPanel.add(new JLabel("do:"));
		searchPanel.add(priceFieldMax);
		this.add(searchPanel, "north");

		// podesavanje tabele
		TreatmentModel model = new TreatmentModel(TreatmentManager.getInstance().getAllTreatments());
		model.setColumnName(9, "Salonov deo");
		table = new JTable(model);		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		this.add(sc, "hmax 380, grow");

		tableSorter.setModel((AbstractTableModel) table.getModel());
		table.setRowSorter(tableSorter);
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); // remove the ID column

		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnEdit = new JButton("Izmeni");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnEdit);
		this.add(buttonHolder, "south");
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					UUID id = (UUID)table.getModel().getValueAt(tableSorter.convertRowIndexToModel(red), 0);
					Treatment t = TreatmentManager.getInstance().getTreatment(id);
					if(t != null) {
						TreatmentEditDialog dialog = new TreatmentEditDialog(t, ReceptionistTreatmentsPanel.this);
						dialog.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani tretman!", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();
			}
		});
		

		offerField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (offerField.getText().trim().length() == 0)
					offerFilter = null;
				else
					offerFilter = RowFilter.regexFilter("(?i)" + offerField.getText().trim(), 1);
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(4);
				if (offerFilter != null) list.add(offerFilter);   
				if (typeFilter != null) list.add(typeFilter);   
				if (priceFilterMin != null) list.add(priceFilterMin);
				if (priceFilterMax != null) list.add(priceFilterMax);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		typeField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (typeField.getText().trim().length() == 0)
					typeFilter = null;
				else
					typeFilter = RowFilter.regexFilter("(?i)" + typeField.getText().trim(), 2);
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(4);
				if (offerFilter != null) list.add(offerFilter);   
				if (typeFilter != null) list.add(typeFilter);   
				if (priceFilterMin != null) list.add(priceFilterMin);
				if (priceFilterMax != null) list.add(priceFilterMax);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		priceFieldMin.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (priceFieldMin.getText().trim().length() == 0)
					priceFilterMin = null;
				else
					try {
						priceFilterMin = RowFilter.numberFilter(ComparisonType.AFTER, Integer.parseInt(priceFieldMin.getText().trim())-1, 8);
					}
					catch (Exception ex) {
						priceFilterMin = null;
					}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(4);
				if (offerFilter != null) list.add(offerFilter);   
				if (typeFilter != null) list.add(typeFilter);   
				if (priceFilterMin != null) list.add(priceFilterMin);
				if (priceFilterMax != null) list.add(priceFilterMax);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		priceFieldMax.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (priceFieldMax.getText().trim().length() == 0)
					priceFilterMax = null;
				else
					try {
						priceFilterMax = RowFilter.numberFilter(ComparisonType.BEFORE, Integer.parseInt(priceFieldMax.getText().trim())-1, 8);
					}
					catch (Exception ex){
						priceFilterMax = null;
					}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(4);
				if (offerFilter != null) list.add(offerFilter);   
				if (typeFilter != null) list.add(typeFilter);   
				if (priceFilterMin != null) list.add(priceFilterMin);
				if (priceFilterMax != null) list.add(priceFilterMax);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
	}
	
	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		TreatmentModel tm = (TreatmentModel)this.table.getModel();
		tm.fireTableDataChanged();
	}
}
