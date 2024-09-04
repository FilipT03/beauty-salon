package gui.otherpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;

import debug.Debug;
import gui.MainFrame;
import gui.model.WorkerModel;
import gui.otherdialogs.WorkerEditDialog;
import managers.BeauticianManager;
import managers.ReceptionistManager;
import managers.UserManager;
import net.miginfocom.swing.MigLayout;
import users.Beautician;
import users.Receptionist;
import users.User;

public class WorkersPanel extends JPanel{
	private static final long serialVersionUID = -6922869467182749019L;
	protected JTable table;

	public WorkersPanel() {
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons()));

		this.add(new JLabel("Zaposleni:"), "north");
		
		// podesavanje tabele
		WorkerModel model = new WorkerModel(BeauticianManager.getInstance().getAllBeauticians(), ReceptionistManager.getInstance().getAllReceptionists());
		table = new JTable(model);		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		this.add(sc, "hmax 380, grow");

		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); // remove the ID column

		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnEdit = new JButton("Izmeni");
		JButton btnDelete = new JButton("Obriši");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnEdit);
		buttonHolder.add(btnDelete);
		this.add(buttonHolder, "south");
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					UUID id = (UUID)table.getModel().getValueAt(red, 0);
					User u = UserManager.getInstance().getUser(id);
					if(u != null) {
						WorkerEditDialog dialog = new WorkerEditDialog(u, WorkersPanel.this);
						dialog.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabranog korisnika!", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					UUID id = (UUID)table.getModel().getValueAt(red, 0);
					User u = UserManager.getInstance().getUser(id);
					if(u != null) {
						if (u.isDeleted()) {
							JOptionPane.showMessageDialog(null, "Korisnik je već obrisan.", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite da obrišete ovog korisnika?", 
								"Brisanje", JOptionPane.YES_NO_OPTION);
						if (izbor == JOptionPane.YES_OPTION) {
							u.setDeleted(true);
							if (u instanceof Beautician)
								((Beautician)u).setDateOfDeletion(LocalDate.now());
							else
								((Receptionist)u).setDateOfDeletion(LocalDate.now());
							refreshData();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabranog korisnika!", "Greska", JOptionPane.ERROR_MESSAGE);
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
	}
	
	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		WorkerModel m = (WorkerModel)this.table.getModel();
		m.fireTableDataChanged();
	}
}
