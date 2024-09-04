package gui.chartpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;

import debug.Debug;
import gui.MainFrame;
import managers.BeauticianManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import users.Beautician;

public class BeauticianPieChartPanel extends JPanel{
	private static final long serialVersionUID = 6513052059258460270L;

	PieChart chart;
	
	public BeauticianPieChartPanel(){
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons(), "10[]", "[][][]"));

		this.add(new JLabel("Opterećenje kozmetičara u prethodnih 30 dana:"));
		
		chart = new PieChartBuilder().build();
		
		for (Beautician b : BeauticianManager.getInstance().getAllActiveBeauticians()) {
			chart.addSeries(b.toString(), Reports.treatmentDoneByBetween(b, LocalDateTime.now().minusMonths(1), LocalDateTime.now()));
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
		for (Beautician b : BeauticianManager.getInstance().getAllActiveBeauticians()) {
			chart.addSeries(b.toString(), Reports.treatmentDoneByBetween(b, LocalDateTime.now().minusMonths(1), LocalDateTime.now()));
		}
	}
	
}
