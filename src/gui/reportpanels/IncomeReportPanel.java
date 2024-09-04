package gui.reportpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import debug.Debug;
import gui.MainFrame;
import gui.model.BeauticianReportModel;
import gui.otherpanels.DateLabelFormatter;
import managers.BeauticianManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import users.Beautician;

public class IncomeReportPanel extends JPanel{
	private static final long serialVersionUID = 2996640153864679255L;

	protected JTable table;
	
	protected JDatePickerImpl datePickerStart;
	protected JDatePickerImpl datePickerEnd;
	
	protected BeauticianReportModel tmodel;
	
	public IncomeReportPanel() {
		this.setLayout(new MigLayout("wrap, fill" + Debug.debugCons()));

		this.add(new JLabel("Početni datum i vreme:"), "grow, split 2");
		this.add(new JLabel("Krajnji datum i vreme:"), "grow");
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePickerStart = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		this.add(datePickerStart, "growx 25, split 4");
		
		datePickerStart.getModel().setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
		datePickerStart.getModel().setSelected(true);
		
		JTextField tfTimeStart = new JTextField("00:00", 14);
		this.add(tfTimeStart, "growx 25");
		
		
		UtilDateModel model2 = new UtilDateModel();
		Properties p2 = new Properties();
		p2.put("text.today", "Today");
		p2.put("text.month", "Month");
		p2.put("text.year", "Year");
		JDatePanelImpl datePanel2 = new JDatePanelImpl(model2, p2);
		datePickerEnd = new JDatePickerImpl(datePanel2, new DateLabelFormatter());
		this.add(datePickerEnd, "growx 25");
		
		datePickerEnd.getModel().setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
		datePickerEnd.getModel().setSelected(true);
		
		JTextField tfTimeEnd = new JTextField("00:00", 14);
		this.add(tfTimeEnd, "growx 25");

		
		// podesavanje tabele
		tmodel = new BeauticianReportModel(new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
		table = new JTable(tmodel);		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		this.add(sc, "hmax 300, grow");
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnGet = new JButton("Izveštaj");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnGet);
		this.add(buttonHolder, "south");
		
		btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LocalDate startDate = null, endDate = null;
				LocalTime startTime = null, endTime = null;
				try {
					Date _date = (Date)datePickerStart.getModel().getValue();
					startDate = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					startTime = LocalTime.parse(tfTimeStart.getText().trim());
				}
				catch (DateTimeParseException ex){
					JOptionPane.showMessageDialog(null, "Početno vreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Date _date = (Date)datePickerEnd.getModel().getValue();
					endDate = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					endTime = LocalTime.parse(tfTimeEnd.getText().trim());
				}
				catch (DateTimeParseException ex){
					JOptionPane.showMessageDialog(null, "Krajnje vreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				ArrayList<Beautician> beauticians = BeauticianManager.getInstance().getAllActiveBeauticians();
				ArrayList<Double> incomes = new ArrayList<>();
				ArrayList<Integer> completeds = new ArrayList<>();
				for (Beautician b : beauticians) {
					incomes.add(Reports.incomeEarnedByBetween(b, LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime)));
					completeds.add(Reports.treatmentDoneByBetween(b, LocalDateTime.of(startDate, startTime), LocalDateTime.of(endDate, endTime)));
				}
				tmodel.updateData(beauticians, incomes, completeds);
				refreshData();
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
		BeauticianReportModel m = (BeauticianReportModel)this.table.getModel();
		m.fireTableDataChanged();
	}
}
