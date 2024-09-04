package gui.userpanels;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import app.Application;
import debug.Debug;
import gui.MainFrame;
import gui.PanelName;
import gui.authentication.LoginDialog;
import gui.otherpanels.ClientBeauticianTreatmentsPanel;
import gui.otherpanels.SchedulePanel;
import net.miginfocom.swing.MigLayout;
import users.Client;

public class ClientPanel extends UserPanel{
	private static final long serialVersionUID = -5528194023887629421L;
	protected CardLayout cardLayout;
	protected JLabel loyaltyLabel;
	protected ClientBeauticianTreatmentsPanel tp;
	protected SchedulePanel sp;
	
	public ClientPanel() {
		UserPanel.initPanel(this);
		
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "[]", "[][]50[]"));
		
		JLabel welcome = new JLabel("Dobro došli " + Application.currentUser.getUsername() + "!");
		welcome.setFont(welcome.getFont().deriveFont(20f));
		this.add(welcome, "wrap");
		Client client = (Client) Application.currentUser;
		if (client.hasLoyaltyCard())
			loyaltyLabel = new JLabel("Ostvarili ste pravo na karticu lojalnosti! Dosad ste potrošili " + client.calculateMoneySpent() + " dinara!");
		else
			loyaltyLabel = new JLabel("Nemate karticu lojalnosti. Dosad ste potrošili " + client.calculateMoneySpent() + " dinara.");
		this.add(loyaltyLabel, "flowx");
		
		JButton scheduleTreatmentsButton = new JButton("Zakaži tretman");
		JButton listTreatmentsButton = new JButton("Vidi svoje tretmane");
		JButton logoutButton = new JButton("Odjavi se");
		
		this.add(scheduleTreatmentsButton, "split 5, grow 30");
		this.add(new JLabel(), "grow 40");
		this.add(listTreatmentsButton, "grow 30");
		this.add(new JLabel(), "grow 40");
		this.add(logoutButton, "grow 30");
		
		
		scheduleTreatmentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sp.refreshData();
				MainFrame.getInstance().switchPanel(PanelName.SCHEDULE_PANEL);
			}
		});
		listTreatmentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tp.refreshData();
				MainFrame.getInstance().switchPanel(PanelName.CLIENT_BEAUTICIAN_TREATMENTS_PANEL);
			}
		});
		logoutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.deleteInstance();
				new LoginDialog();
			}
		});
		
	}

	@Override
	public void addPanels(JPanel panelContainer) {
		sp = new SchedulePanel();
		tp = new ClientBeauticianTreatmentsPanel(((Client)Application.currentUser).getTreatments());
		
		panelContainer.add(sp, PanelName.SCHEDULE_PANEL.toString());
		panelContainer.add(tp, PanelName.CLIENT_BEAUTICIAN_TREATMENTS_PANEL.toString());
	}

	@Override
	public void updateData() {
		Client client = (Client) Application.currentUser;
		if (client.hasLoyaltyCard())
			loyaltyLabel.setText("Ostvarili ste pravo na karticu lojalnosti! Dosad ste potrošili " + client.calculateMoneySpent() + " dinara!");
		else
			loyaltyLabel.setText("Nemate karticu lojalnosti. Dosad ste potrošili " + client.calculateMoneySpent() + " dinara.");
	}
	
}
