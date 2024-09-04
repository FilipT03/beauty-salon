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
import gui.otherpanels.ReceptionistTreatmentsPanel;
import gui.otherpanels.SchedulePanel;
import net.miginfocom.swing.MigLayout;

public class ReceptionistPanel extends UserPanel{
	private static final long serialVersionUID = -5528194023887629421L;
	protected CardLayout cardLayout;
	protected JLabel loyaltyLabel;
	protected ReceptionistTreatmentsPanel tp;
	protected SchedulePanel sp;
	
	public ReceptionistPanel() {
		UserPanel.initPanel(this);
		
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "[]", "[]50[]"));
		
		JLabel welcome = new JLabel("Dobro došli " + Application.currentUser.getUsername() + "!");
		welcome.setFont(welcome.getFont().deriveFont(20f));
		this.add(welcome, "wrap");
		
		JButton scheduleTreatmentsButton = new JButton("Zakaži tretman");
		JButton listTreatmentsButton = new JButton("Vidi zakazane tretmane");
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
				MainFrame.getInstance().switchPanel(PanelName.RECEPTIONIST_TREATMENT_PANEL);
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
		tp = new ReceptionistTreatmentsPanel();
		
		panelContainer.add(sp, PanelName.SCHEDULE_PANEL.toString());
		panelContainer.add(tp, PanelName.RECEPTIONIST_TREATMENT_PANEL.toString());
	}
	
}
