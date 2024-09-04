package gui.reportpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import debug.Debug;
import gui.MainFrame;
import managers.SalonManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import users.Client;

public class LoyaltyCardReportPanel extends JPanel{
	private static final long serialVersionUID = 2996640153864679255L;

	protected DefaultListModel<String> clientListModel;
	protected JList<String> clients;
	protected JTextField tfMinimum;
	
	public LoyaltyCardReportPanel() {
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "10[]", "[]20[]60[]10[]80[]"));

		this.add(new JLabel("Kartice lojalnosti:"));
		
		ArrayList<Client> arrayList = Reports.clientsThatSatisfyLoyaltyMinimum();
		clientListModel = new DefaultListModel<>();
		for(int i=0;i<arrayList.size();i++)
			clientListModel.addElement(arrayList.get(i).toString() + ", " + arrayList.get(i).getUsername() + ", Potrošio/la " + arrayList.get(i).calculateMoneySpent());
		clients = new JList<String>(clientListModel);
		JScrollPane sc = new JScrollPane(clients);
		this.add(sc, "hmax 380, grow");
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnGet = new JButton("Ažuriraj minimum");
		tfMinimum = new JTextField(Integer.toString(SalonManager.getInstance().getSalon().getLoyaltyMinimum()), 10);
		buttonHolder.add(btnBack);
		buttonHolder.add(btnGet);
		buttonHolder.add(tfMinimum);
		this.add(buttonHolder, "south");
		
		btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tfMinimum.getText() == null) {
					JOptionPane.showMessageDialog(null, "Minimum ne sme biti prazan.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int newMinimum;
				try{
					newMinimum = Integer.parseInt(tfMinimum.getText());
				}
				catch (Exception ex) {
					JOptionPane.showMessageDialog(null, "Nije dobar format.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				SalonManager.getInstance().changeLoyaltyMinimum(newMinimum);
				ArrayList<Client> arrayList = Reports.clientsThatSatisfyLoyaltyMinimum();
				clientListModel.removeAllElements();
				for(int i=0;i<arrayList.size();i++)
					clientListModel.addElement(arrayList.get(i).toString() + ", " + arrayList.get(i).getUsername() + ", Potrošio/la " + arrayList.get(i).calculateMoneySpent());
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();
			}
		});
	}
	
	public void refreshData() {
		ArrayList<Client> arrayList = Reports.clientsThatSatisfyLoyaltyMinimum();
		clientListModel.removeAllElements();
		for(int i=0;i<arrayList.size();i++)
			clientListModel.addElement(arrayList.get(i).toString() + ", " + arrayList.get(i).getUsername() + ", Potrošio/la " + arrayList.get(i).calculateMoneySpent());
		tfMinimum.setText(Integer.toString(SalonManager.getInstance().getSalon().getLoyaltyMinimum()));
	}
	
}
