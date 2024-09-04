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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import reports.Reports;
import salon.TreatmentOffer;

public class OfferReportPanel extends JPanel{
	private static final long serialVersionUID = 2996640153864679255L;

	protected JDatePickerImpl datePickerStart;
	protected JDatePickerImpl datePickerEnd;
	
	DefaultComboBoxModel<TreatmentOffer> offerBoxModel = new DefaultComboBoxModel<>();
	
	public OfferReportPanel() {
		this.setLayout(new MigLayout("wrap" + Debug.debugCons(), "", "[]20[]60[]10[]80[]"));

		this.add(new JLabel("Početni datum i vreme:"), "split 5, grow");
		this.add(new JLabel("                                     "), "grow");
		this.add(new JLabel("Krajnji datum i vreme:"), "grow");
		this.add(new JLabel("                                             "), "grow");
		this.add(new JLabel("Ponuda tretmana:"), "grow");
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		
		
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePickerStart = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		this.add(datePickerStart, "growx 25, split 5");
		
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
		
		ArrayList<TreatmentOffer> offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
		for (TreatmentOffer o : offers)
			offerBoxModel.addElement(o);
		JComboBox<TreatmentOffer> offerComboBox = new JComboBox<TreatmentOffer>(offerBoxModel);
		this.add(offerComboBox, "growx 25");
		
		JLabel name = new JLabel("");
		JLabel type = new JLabel("");
		JLabel lenght = new JLabel("");
		JLabel price = new JLabel("");
		JLabel deleted = new JLabel("");
		JLabel income = new JLabel("0");
		JLabel scheduled = new JLabel("0");
		
		JPanel firstPanel = new JPanel();
		firstPanel.add(new JLabel("Naziv:"), "split 2");
		firstPanel.add(name);
		firstPanel.add(new JLabel("  Tip:"), "split 2");
		firstPanel.add(type);
		firstPanel.add(new JLabel("  Dužina:"), "split 2");
		firstPanel.add(lenght);
		firstPanel.add(new JLabel("  Cena:"), "split 2");
		firstPanel.add(price);
		firstPanel.add(new JLabel("  Deleted:"), "split 2");
		firstPanel.add(deleted);
		JPanel secPanel = new JPanel();
		secPanel.add(new JLabel("Prihod:"), "split 2");
		secPanel.add(income);
		secPanel.add(new JLabel("  Zakazanih tretmana:"), "split 2");
		secPanel.add(scheduled);
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
				TreatmentOffer offer = (TreatmentOffer)offerComboBox.getSelectedItem();
				int[] result = Reports.analyzeTreatmentOffer(offer, startDateTime, endDateTime);
				income.setText(Integer.toString(result[1]));
				scheduled.setText(Integer.toString(result[0]));
			}
		});

		offerComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreatmentOffer offer = (TreatmentOffer)offerComboBox.getSelectedItem();
				if (offer != null) {
					name.setText(offer.getName());
					type.setText(offer.getTreatmentType().name);
					lenght.setText(offer.getLenght().toString());
					price.setText(Double.toString(offer.getPrice()));
					deleted.setText(Boolean.toString(offer.isDeleted()));
				}
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
		ArrayList<TreatmentOffer> offers = TreatmentManager.getInstance().getAllTreatmentOffersArrayList();
		offerBoxModel.removeAllElements();
		for (TreatmentOffer o : offers)
			offerBoxModel.addElement(o);
	}
	
}
