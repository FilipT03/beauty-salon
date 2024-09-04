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
import users.Beautician;

public class BeauticianPanel extends UserPanel{
	private static final long serialVersionUID = -5528194023887629421L;
	protected CardLayout cardLayout;
	protected ClientBeauticianTreatmentsPanel tp;
	protected SchedulePanel sp;
	
	public BeauticianPanel() {
		UserPanel.initPanel(this);
		
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "[]", "[]50[]"));
		
		JLabel welcome = new JLabel("Dobro došli " + Application.currentUser.getUsername() + "!");
		welcome.setFont(welcome.getFont().deriveFont(20f));
		this.add(welcome, "wrap");
		
		JButton listTreatmentsButton = new JButton("Vidi svoje tretmane");
		JButton logoutButton = new JButton("Odjavi se");
		
		this.add(listTreatmentsButton, "split 3, grow 30");
		this.add(new JLabel(), "grow 40");
		this.add(logoutButton, "grow 30");
		
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
		tp = new ClientBeauticianTreatmentsPanel(((Beautician)Application.currentUser).getTreatments());
		
		panelContainer.add(tp, PanelName.CLIENT_BEAUTICIAN_TREATMENTS_PANEL.toString());
	}
	
}
