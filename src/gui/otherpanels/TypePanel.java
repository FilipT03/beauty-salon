package gui.otherpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import debug.Debug;
import gui.MainFrame;
import gui.model.TypeModel;
import gui.otherdialogs.TypeAddEditDialog;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentType;

public class TypePanel extends JPanel{
	private static final long serialVersionUID = 2996640153864679255L;

	protected JTable table;

	public TypePanel() {
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons()));

		this.add(new JLabel("Tipovi:"), "north");
		
		// podesavanje tabele
		TypeModel model = new TypeModel();
		table = new JTable(model);		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		this.add(sc, "hmax 380, grow");

		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnEdit = new JButton("Izmeni");
		JButton btnAdd = new JButton("Dodaj");
		JButton btnDelete = new JButton("Obriši");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnEdit);
		buttonHolder.add(btnAdd);
		buttonHolder.add(btnDelete);
		this.add(buttonHolder, "south");
		
		btnEdit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					String name = (String)table.getModel().getValueAt(red, 0);
					TreatmentType t = TreatmentManager.getInstance().getTreatmentType(name);
					if(t != null) {
						TypeAddEditDialog dialog = new TypeAddEditDialog(t, TypePanel.this);
						dialog.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani tip!", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TypeAddEditDialog dialog = new TypeAddEditDialog(null, TypePanel.this);
				dialog.setVisible(true);
			}
		});
		
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					String name = (String)table.getModel().getValueAt(red, 0);
					TreatmentType t = TreatmentManager.getInstance().getTreatmentType(name);
					if(t != null) {
						if (t.isDeleted()) {
							JOptionPane.showMessageDialog(null, "Tip je već obrisan", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite da obrišete ovaj tip?", 
								"Brisanje", JOptionPane.YES_NO_OPTION);
						if (izbor == JOptionPane.YES_OPTION) {
							t.setDeleted(true);
							refreshData();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabrani tip!", "Greska", JOptionPane.ERROR_MESSAGE);
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
		TypeModel m = (TypeModel)this.table.getModel();
		m.fireTableDataChanged();
	}
}
