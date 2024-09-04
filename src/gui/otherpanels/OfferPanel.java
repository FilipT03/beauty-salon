package gui.otherpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import gui.model.OfferModel;
import gui.otherdialogs.OfferAddEditDialog;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentOffer;

public class OfferPanel extends JPanel{
	private static final long serialVersionUID = 1996640153864679255L;

	protected JTable table;

	public OfferPanel() {
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons()));

		this.add(new JLabel("Ponude:"), "north");
		
		// podesavanje tabele
		OfferModel model = new OfferModel();
		table = new JTable(model);		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		this.add(sc, "hmax 380, grow");

		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); // remove the ID column
		tcm.removeColumn(tcm.getColumn(4)); // remove the minutes column

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
					UUID id = (UUID)table.getModel().getValueAt(red, 0);
					TreatmentOffer o = TreatmentManager.getInstance().getTreatmentOffer(id);
					if(o != null) {
						OfferAddEditDialog dialog = new OfferAddEditDialog(o, OfferPanel.this);
						dialog.setVisible(true);
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabranu ponudu!", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OfferAddEditDialog dialog = new OfferAddEditDialog(null, OfferPanel.this);
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
					UUID id = (UUID)table.getModel().getValueAt(red, 0);
					TreatmentOffer o = TreatmentManager.getInstance().getTreatmentOffer(id);
					if(o != null) {
						if (o.isDeleted()) {
							JOptionPane.showMessageDialog(null, "Ponuda je već obrisana", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						int izbor = JOptionPane.showConfirmDialog(null,"Da li ste sigurni da želite da obrišete ovu ponudu?", 
								"Brisanje", JOptionPane.YES_NO_OPTION);
						if (izbor == JOptionPane.YES_OPTION) {
							o.setDeleted(true);
							refreshData();
						}
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguće pronaći odabranu ponudu!", "Greska", JOptionPane.ERROR_MESSAGE);
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
		OfferModel m = (OfferModel)this.table.getModel();
		m.updateData();
		m.fireTableDataChanged();
	}
}
