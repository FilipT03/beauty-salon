package gui.chartpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import debug.Debug;
import gui.MainFrame;
import managers.BeauticianManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import salon.TreatmentStatus;
import users.Beautician;

public class StatusesPieChartPanel extends JPanel{
	private static final long serialVersionUID = 6513052059258460270L;

	PieChart chart;
	
	public StatusesPieChartPanel(){
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons(), "10[]", "[][][]"));

		this.add(new JLabel("Statusi kozmetičkih tretmana u prethodnih 30 dana:"));
		
		chart = new PieChartBuilder().build();
		
		TreatmentStatus[] statuses = new TreatmentStatus[] {TreatmentStatus.IZVRŠEN, TreatmentStatus.ZAKAZAN, TreatmentStatus.OTKAZAO_KLIJENT, TreatmentStatus.OTKAZAO_SALON, TreatmentStatus.NIJE_SE_POJAVIO};
		for(TreatmentStatus s : statuses) {
			chart.addSeries(s.toString(), Reports.treatmentWithStatusBetween(s, LocalDateTime.now().minusMonths(1), LocalDateTime.now()));
		}
		
		XChartPanel<PieChart> chartPanel =  new XChartPanel<PieChart>(chart);
		this.add(chartPanel, "grow");
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		buttonHolder.add(btnBack);
		this.add(buttonHolder, "south");
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();
			}
		});
	}

	public void refreshData() {
		ArrayList<String> series = new ArrayList<>();
		for (String s : chart.getSeriesMap().keySet()) {
			series.add(s);
		}
		for (String s : series)
			chart.removeSeries(s);
		TreatmentStatus[] statuses = new TreatmentStatus[] {TreatmentStatus.IZVRŠEN, TreatmentStatus.ZAKAZAN, TreatmentStatus.OTKAZAO_KLIJENT, TreatmentStatus.OTKAZAO_SALON, TreatmentStatus.NIJE_SE_POJAVIO};
		for(TreatmentStatus s : statuses) {
			chart.addSeries(s.toString(), Reports.treatmentWithStatusBetween(s, LocalDateTime.now().minusMonths(1), LocalDateTime.now()));
		}
	}
	
}
