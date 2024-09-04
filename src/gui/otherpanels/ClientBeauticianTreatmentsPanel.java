package gui.otherpanels;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import app.Application;
import gui.MainFrame;
import gui.model.TreatmentModel;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.Treatment;
import salon.TreatmentStatus;
import users.Beautician;
import users.Client;

public class ClientBeauticianTreatmentsPanel extends JPanel{
	private static final long serialVersionUID = -8026201049950423764L;
	
	protected JTable table;
	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();

	public ClientBeauticianTreatmentsPanel(ArrayList<Treatment> treatments) {
		this.setLayout(new MigLayout("fill"));
		
		JToolBar mainToolbar = new JToolBar();
		JButton btnBack = new JButton("Nazad");
		JButton btnDelete = null;
		

		mainToolbar.add(btnBack);
		if (Application.currentUser instanceof Client) {
			btnDelete = new JButton("Otkaži odabrani tretman");
			mainToolbar.add(btnDelete);
		}
		mainToolbar.setFloatable(false);		
		add(mainToolbar, BorderLayout.NORTH);
		
		
		// podesavanje tabele
		table = new JTable(new TreatmentModel(treatments));		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		add(sc, "grow 1");

		tableSorter.setModel((AbstractTableModel) table.getModel());
		table.setRowSorter(tableSorter);
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); // remove the ID column
		
		if (Application.currentUser instanceof Client)
			tcm.removeColumn(tcm.getColumn(3)); //Klijent
		else if (Application.currentUser instanceof Beautician) {
			tcm.removeColumn(tcm.getColumn(2)); //Kozmeticar
			tcm.removeColumn(tcm.getColumn(7)); //Vas deo
		}
		
		if (Application.currentUser instanceof Client)
			btnDelete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int red = table.getSelectedRow();
					if(red == -1) {
						JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
					}else {
						UUID id = (UUID)table.getModel().getValueAt(tableSorter.convertRowIndexToModel(red), 0);
						Treatment t = TreatmentManager.getInstance().getTreatment(id);
						if(t != null) {
							if (t.getTreatmentStatus() == TreatmentStatus.ZAKAZAN) {
								int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite da otkažete ovaj termin?", 
										"Otkazivanje", JOptionPane.YES_NO_OPTION);
								if(izbor == JOptionPane.YES_OPTION) {
									t.cancelByClient();
									refreshData();
								}
							} else {
								JOptionPane.showMessageDialog(null, "Ne možete otkazati tretman koji nije zakazan!", "Greska", JOptionPane.ERROR_MESSAGE);
							}
						}else {
							JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani tretman!", "Greska", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			});
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();;
			}
		});
	}
	
	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		TreatmentModel tm = (TreatmentModel)this.table.getModel();
		tm.fireTableDataChanged();
	}
}
