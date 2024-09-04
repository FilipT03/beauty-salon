package gui.otherdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MainFrame;
import gui.otherpanels.ReceptionistTreatmentsPanel;
import managers.BeauticianManager;
import managers.ClientManager;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.Treatment;
import salon.TreatmentOffer;
import salon.TreatmentStatus;
import users.Beautician;
import users.Client;

public class TreatmentEditDialog extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private Treatment editT;
	private JPanel parent;
	
	public TreatmentEditDialog(Treatment editTreatment, JPanel parent) {
		super(MainFrame.getInstance(), true);
		this.parent = parent;
		setTitle("Izmena tretmana");
		this.editT = editTreatment;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		initGUI();
		pack();
	}

	private void initGUI() {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]20[]");
		setLayout(ml);
		
		ArrayList<TreatmentOffer> offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
		TreatmentOffer[] offerArray = new TreatmentOffer[offers.size()];
		offerArray = offers.toArray(offerArray);
		JComboBox<TreatmentOffer> offerComboBox = new JComboBox<TreatmentOffer>(offerArray);
		offerComboBox.setSelectedIndex(offers.indexOf(editT.getTreatmentOffer()));
		ArrayList<Beautician> beauticians = BeauticianManager.getInstance().getAllActiveBeauticians();
		Beautician[] beauticianArray = new Beautician[beauticians.size()];
		beauticianArray = beauticians.toArray(beauticianArray);
		JComboBox<Beautician> beauticiansComboBox = new JComboBox<Beautician>(beauticianArray);
		beauticiansComboBox.setSelectedIndex(beauticians.indexOf(editT.getBeautician()));
		ArrayList<Client> clients = ClientManager.getInstance().getAllActiveClients();
		Client[] clientArray = new Client[clients.size()];
		clientArray = clients.toArray(clientArray);
		JComboBox<Client> clientsComboBox = new JComboBox<Client>(clientArray);
		clientsComboBox.setSelectedIndex(clients.indexOf(editT.getClient()));
		TreatmentStatus[] statuses = new TreatmentStatus[] {
				TreatmentStatus.ZAKAZAN, TreatmentStatus.OTKAZAO_KLIJENT, TreatmentStatus.OTKAZAO_SALON,
				TreatmentStatus.IZVRŠEN, TreatmentStatus.NIJE_SE_POJAVIO};
		JComboBox<TreatmentStatus> statusesComboBox = new JComboBox<TreatmentStatus>(statuses);
		int i = 0;
		for(i = 0; i < 5; i++)
			if (statuses[i] == editT.getTreatmentStatus())
				break;
		statusesComboBox.setSelectedIndex(i);
		
		JTextField startTimeField = new JTextField(14);
		startTimeField.setText(editT.getStartTime().toString());
		JTextField priceField = new JTextField(14);
		priceField.setText(Double.toString(editT.getPrice()));

		add(new JLabel("Tretman"));
		add(offerComboBox, "span 2");
		add(new JLabel("Kozmetičar"));
		add(beauticiansComboBox, "span 2");
		add(new JLabel("Klijent"));
		add(clientsComboBox, "span 2");
		add(new JLabel("Status"));
		add(statusesComboBox, "span 2");
		add(new JLabel("Datum i vreme"));
		add(startTimeField, "span 2");
		add(new JLabel("Cena"));
		add(priceField, "span 2");
		
		add(new JLabel());

		JButton btnCancel = new JButton("Odustani");
		add(btnCancel);

		JButton btnOK = new JButton("OK");
		add(btnOK);

		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Treatment newT = Treatment.parse(Treatment.parse(editT)); // ovo je ekvivalentno shallow copy
				newT.setBeautician((Beautician)beauticiansComboBox.getSelectedItem());
				newT.setClient((Client)clientsComboBox.getSelectedItem());
				newT.setTreatmentOffer((TreatmentOffer)offerComboBox.getSelectedItem());
				newT.setTreatmentStatus((TreatmentStatus)statusesComboBox.getSelectedItem());
				try {
					newT.setPrice(Double.parseDouble(priceField.getText().trim()));
				}
				catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Cena nije uneta!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				catch (NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Cena nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					newT.setStartTime(LocalDateTime.parse(startTimeField.getText()));
				}
				catch (DateTimeParseException ex){
					JOptionPane.showMessageDialog(null, "Polje datum i vreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				newT.update();
				TreatmentManager.getInstance().updateTreatment(newT);
				((ReceptionistTreatmentsPanel) parent).refreshData();
				TreatmentEditDialog.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreatmentEditDialog.this.dispose();
			}
		});
	}
	
	public void updateData() {
		ArrayList<TreatmentOffer> offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
		TreatmentOffer[] offerArray = new TreatmentOffer[offers.size()];
		offerArray = offers.toArray(offerArray);
		JComboBox<TreatmentOffer> offerComboBox = new JComboBox<TreatmentOffer>(offerArray);
		offerComboBox.setSelectedIndex(offers.indexOf(editT.getTreatmentOffer()));
	}
}
