package gui.reportpanels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import debug.Debug;
import gui.MainFrame;
import gui.otherpanels.DateLabelFormatter;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import salon.TreatmentStatus;

public class StatusReportPanel extends JPanel{
	private static final long serialVersionUID = 2996640153864679255L;

	protected JDatePickerImpl datePickerStart;
	protected JDatePickerImpl datePickerEnd;
	
	public StatusReportPanel() {
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "", "[]20[]60[]10[]80[]"));

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

		JLabel scheduled = new JLabel("0");
		JLabel completed = new JLabel("0");
		JLabel canceledByClient = new JLabel("0");
		JLabel canceledBySalon = new JLabel("0");
		JLabel didntShowUp = new JLabel("0");
		
		JPanel firstPanel = new JPanel();
		firstPanel.add(new JLabel("Zakazano:"), "split 2");
		firstPanel.add(scheduled);
		firstPanel.add(new JLabel(" Izvršeno:"), "split 2");
		firstPanel.add(completed);
		firstPanel.add(new JLabel(" Otkazao klijent:"), "split 2");
		firstPanel.add(canceledByClient);
		JPanel secPanel = new JPanel();
		secPanel.add(new JLabel("Otkazao salon:"), "split 2");
		secPanel.add(canceledBySalon);
		secPanel.add(new JLabel(" Nije se pojavio:"), "split 2");
		secPanel.add(didntShowUp);
		this.add(firstPanel, "growx");
		this.add(secPanel, "growx");
		
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
				LocalDateTime startDateTime = LocalDateTime.of(startDate, startTime);
				LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);
				scheduled.setText(Integer.toString(Reports.treatmentWithStatusBetween(TreatmentStatus.ZAKAZAN, startDateTime, endDateTime)));
				completed.setText(Integer.toString(Reports.treatmentWithStatusBetween(TreatmentStatus.IZVRŠEN, startDateTime, endDateTime)));
				canceledByClient.setText(Integer.toString(Reports.treatmentWithStatusBetween(TreatmentStatus.OTKAZAO_KLIJENT, startDateTime, endDateTime)));
				canceledBySalon.setText(Integer.toString(Reports.treatmentWithStatusBetween(TreatmentStatus.OTKAZAO_SALON, startDateTime, endDateTime)));
				didntShowUp.setText(Integer.toString(Reports.treatmentWithStatusBetween(TreatmentStatus.NIJE_SE_POJAVIO, startDateTime, endDateTime)));
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();
			}
		});
	}
	
}
