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
import gui.authentication.LoginDialog;
import gui.chartpanels.BeauticianPieChartPanel;
import gui.chartpanels.PerTypeChartPanel;
import gui.chartpanels.StatusesPieChartPanel;
import gui.otherdialogs.RegisterWorkerDialog;
import gui.otherpanels.OfferPanel;
import gui.otherpanels.ReceptionistTreatmentsPanel;
import gui.otherpanels.TypePanel;
import gui.otherpanels.WorkersPanel;
import gui.reportpanels.IncomeReportPanel;
import gui.reportpanels.LoyaltyCardReportPanel;
import gui.reportpanels.OfferReportPanel;
import gui.reportpanels.StatusReportPanel;
import net.miginfocom.swing.MigLayout;

public class ManagerPanel extends UserPanel{
	private static final long serialVersionUID = -5528194023887629421L;
	protected CardLayout cardLayout;
	protected JLabel loyaltyLabel;
	
	public ManagerPanel() {
		UserPanel.initPanel(this);

		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "[]", "[]40[][]15[][]15[][]15[][]"));
		
		JLabel welcome = new JLabel("Dobro došli " + Application.currentUser.getUsername() + "!");
		welcome.setFont(welcome.getFont().deriveFont(20f));
		this.add(welcome, "wrap");
		
		JButton listUsersButton = new JButton("Lista zaposlenih");
		JButton listTreatmentsButton = new JButton("Lista zakazanih tretmana");
		JButton listTreatmentOffersButton = new JButton("Lista ponuda tretmana");
		JButton listTreatmentTypesButton = new JButton("Lista tipova tretmana");

		this.add(new JLabel("Liste:"));
		JPanel listPanel = new JPanel(new MigLayout());
		listPanel.add(listUsersButton, "split 4, grow 30");
		listPanel.add(listTreatmentsButton, "grow 30");
		listPanel.add(listTreatmentOffersButton, "grow 30");
		listPanel.add(listTreatmentTypesButton, "grow 30");
		this.add(listPanel, "growx");
		
		JButton incomeReport = new JButton("Izveštaj prihoda/rashoda");
		JButton statusReport = new JButton("Izveštaj statusa za period");
		JButton offerReport = new JButton("Izveštaj ponude tretmana");
		JButton loyaltyCardReport = new JButton("Izveštaj kartice lojalnosti");
		
		this.add(new JLabel("Izveštaji:"));
		JPanel reportPanel = new JPanel(new MigLayout());
		reportPanel.add(incomeReport, "split 3, grow 30");
		reportPanel.add(statusReport, "grow 30");
		reportPanel.add(offerReport, "grow 30, wrap");
		reportPanel.add(loyaltyCardReport, "");
		this.add(reportPanel, "growx");
		
		JButton incomePerTypeGraph = new JButton("Dijagram prihoda u poslednjih 12 meseci po tipu");
		JButton beauticianGraph = new JButton("Dijagram opterećenja po kozmetičaru");
		JButton status30daysGraph = new JButton("Dijagram statusa u poslednjih 30 dana");
		
		this.add(new JLabel("Dijagrami:"));
		JPanel graphPanel = new JPanel(new MigLayout());
		graphPanel.add(incomePerTypeGraph, "grow 30");
		graphPanel.add(beauticianGraph, "grow 30, wrap");
		graphPanel.add(status30daysGraph, "grow 30");
		this.add(graphPanel, "growx");
		
		JButton registerBeauticianButton = new JButton("Registruj kozmetičara");
		JButton registerReceptionistButton = new JButton("Registruj recepcionera");
		JButton logoutButton = new JButton("Odjavi se");

		this.add(new JLabel("Registracija/odjava:"));
		JPanel authPanel = new JPanel(new MigLayout());
		authPanel.add(registerBeauticianButton, "split 3, grow 30");
		authPanel.add(registerReceptionistButton, "grow 30");
		authPanel.add(logoutButton, "grow 30");
		this.add(authPanel, "growx");
		
//		scheduleTreatmentsButton.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				sp.refreshData();
//				MainFrame.getInstance().switchPanel(PanelName.SCHEDULE_PANEL);
//			}
//		});
		listUsersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				wp.refreshData();
				MainFrame.getInstance().switchPanel("Workers");
			}
		});
		listTreatmentOffersButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				op.refreshData();
				MainFrame.getInstance().switchPanel("Offers");
			}
		});
		listTreatmentTypesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tp.refreshData();
				MainFrame.getInstance().switchPanel("Types");
			}
		});
		listTreatmentsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				rtp.refreshData();
				MainFrame.getInstance().switchPanel("Treatments");
			}
		});
		registerBeauticianButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterWorkerDialog dialog = new RegisterWorkerDialog(true);
				dialog.setVisible(true);
			}
		});
		registerReceptionistButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterWorkerDialog dialog = new RegisterWorkerDialog(false);
				dialog.setVisible(true);
			}
		});
		incomeReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				irp.refreshData();
				MainFrame.getInstance().switchPanel("IncomeReport");
			}
		});
		statusReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().switchPanel("StatusReport");
			}
		});
		offerReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				orp.refreshData();
				MainFrame.getInstance().switchPanel("OfferReport");
			}
		});
		loyaltyCardReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lcrp.refreshData();
				MainFrame.getInstance().switchPanel("LoyaltyReport");
			}
		});
		incomePerTypeGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ptcp.refreshData();
				MainFrame.getInstance().switchPanel("PerTypeChart");
			}
		});
		beauticianGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				bpcp.refreshData();
				MainFrame.getInstance().switchPanel("BeauticianPieChart");
			}
		});
		status30daysGraph.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				spcp.refreshData();
				MainFrame.getInstance().switchPanel("StatusPieChart");
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

	WorkersPanel wp;
	OfferPanel op;
	TypePanel tp;
	ReceptionistTreatmentsPanel rtp;
	
	IncomeReportPanel irp;
	StatusReportPanel srp;
	OfferReportPanel orp;
	LoyaltyCardReportPanel lcrp;
	
	PerTypeChartPanel ptcp;
	BeauticianPieChartPanel bpcp;
	StatusesPieChartPanel spcp;
	
	@Override
	public void addPanels(JPanel panelContainer) {
		wp = new WorkersPanel();
		op = new OfferPanel();
		tp = new TypePanel();
		rtp = new ReceptionistTreatmentsPanel();
		
		irp = new IncomeReportPanel();
		srp = new StatusReportPanel();
		orp = new OfferReportPanel();
		lcrp = new LoyaltyCardReportPanel();
		
		ptcp = new PerTypeChartPanel();
		bpcp = new BeauticianPieChartPanel();
		spcp = new StatusesPieChartPanel();
		
		panelContainer.add(wp, "Workers");
		panelContainer.add(op, "Offers");
		panelContainer.add(tp, "Types");
		panelContainer.add(rtp, "Treatments");
		
		panelContainer.add(irp, "IncomeReport");
		panelContainer.add(srp, "StatusReport");
		panelContainer.add(orp, "OfferReport");
		panelContainer.add(lcrp, "LoyaltyReport");
		
		panelContainer.add(ptcp, "PerTypeChart");
		panelContainer.add(bpcp, "BeauticianPieChart");
		panelContainer.add(spcp, "StatusPieChart");
	}
	
}
