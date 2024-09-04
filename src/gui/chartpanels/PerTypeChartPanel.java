package gui.chartpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;

import debug.Debug;
import gui.MainFrame;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import salon.TreatmentType;

public class PerTypeChartPanel extends JPanel{
	private static final long serialVersionUID = 6513052059258460270L;
	
	XYChart chart;
	
	public PerTypeChartPanel(){
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons(), "10[]", "[][][]"));

		this.add(new JLabel("Prikaz prihoda za prethodnih 12 meseci po tipu tretmana i ukupan prihod:"));
		
		ArrayList<TreatmentType> types = TreatmentManager.getInstance().getAllTreatmentTypes();
		int i = 0;
		Double[] total = Reports.incomeByTypeLast12months(null);
		Date[] xData = new Date[12];
		LocalDate start = LocalDate.now().withDayOfMonth(1).plusMonths(1);
		xData[11] = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
		for (i=10;i>=0;i--) {
			start = start.minusMonths(1);
			xData[i] = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		chart = new XYChartBuilder().title("Prihodi po tipu kozmetičkog tretmana").xAxisTitle("Mesec").yAxisTitle("Prihod").build();
	    chart.getStyler().setXAxisLabelRotation(90);
		chart.addSeries("Ukupno", Arrays.asList(xData), Arrays.asList(total));
		Double[] result;
		for (TreatmentType type : types) {
			result = Reports.incomeByTypeLast12months(type);
			chart.addSeries(type.name, Arrays.asList(xData), Arrays.asList(result));
		}
		XChartPanel<XYChart> chartPanel =  new XChartPanel<XYChart>(chart);
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
		ArrayList<TreatmentType> types = TreatmentManager.getInstance().getAllTreatmentTypes();
		int i = 0;
		Double[] total = Reports.incomeByTypeLast12months(null);
		Date[] xData = new Date[12];
		LocalDate start = LocalDate.now().plusMonths(1);
		xData[11] = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
		for (i=10;i>=0;i--) {
			start = start.minusMonths(1);
			xData[i] = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		
		chart.addSeries("Ukupno", Arrays.asList(xData), Arrays.asList(total));
		Double[] result;
		for (TreatmentType type : types) {
			result = Reports.incomeByTypeLast12months(type);
			chart.addSeries(type.name, Arrays.asList(xData), Arrays.asList(result));
		}
	}
	
	
}
