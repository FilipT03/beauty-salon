package gui.otherpanels;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.RowFilter.ComparisonType;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import app.Application;
import debug.Debug;
import gui.MainFrame;
import gui.model.OfferModel;
import managers.BeauticianManager;
import managers.ClientManager;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentException;
import salon.TreatmentOffer;
import users.Beautician;
import users.Client;
import users.Receptionist;

public class SchedulePanel extends JPanel{
	private static final long serialVersionUID = -6452337855692734619L;

	protected CardLayout layout;
	protected JPanel panelContainer;
	protected JTable table;
	protected JPanel clientPanel = new JPanel();
	protected JPanel offerPanel = new JPanel();
	protected JPanel beauticianPanel = new JPanel();
	protected JPanel timePanel = new JPanel();

	protected JList<String> clientList;
	protected DefaultListModel<Object> beauticianListModel;
	protected JList<Object> beauticianList;
	protected JDatePickerImpl datePicker;
	protected DefaultComboBoxModel<LocalTime> timeBoxModel;
	protected JComboBox<LocalTime> timeBox;
	
	protected Client currentClient;
	protected TreatmentOffer currentOffer;
	protected Beautician currentBeautician;
	
	protected boolean initializedBeauticianPanel = false;
	protected boolean initializedTimePanel = false;

	protected TableRowSorter<AbstractTableModel> tableSorter = new TableRowSorter<AbstractTableModel>();
	
	RowFilter<Object,Object> typeFilter = null, lenghtFilter = null, priceFilter = null;
	
	public SchedulePanel() {
		layout = new CardLayout();
		panelContainer = new JPanel(layout);
	
		if (Application.currentUser instanceof Receptionist) {
			initClientPanel();
			panelContainer.add(clientPanel, "ClientPanel");
		}
		else
			currentClient = (Client)Application.currentUser;
		initOfferPanel();
		panelContainer.add(offerPanel, "OfferPanel");
		panelContainer.add(beauticianPanel, "BeauticianPanel");
		panelContainer.add(timePanel, "TimePanel");
		
		this.add(panelContainer);
	}
	
	// potrebno osvezavanje podataka u tabeli bez gasenja prozora
	public void refreshData() {
		OfferModel tm = (OfferModel)table.getModel();
		tm.fireTableDataChanged();
	}

	protected void initClientPanel() {
		JPanel cp = clientPanel;

		cp.setLayout(new MigLayout("fill, wrap" + Debug.debugCons()));
		
		cp.add(new JLabel("Izaberite klijenta:"), "north");
		
		ArrayList<Client> arrayList = ClientManager.getInstance().getAllActiveClients();
		String[] elements = new String[arrayList.size()];
		for(int i=0;i<arrayList.size();i++)
			elements[i] = arrayList.get(i).toString() + ", " + arrayList.get(i).getUsername();
		clientList = new JList<String>(elements);
		clientList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sc = new JScrollPane(clientList);
		cp.add(sc, "hmax 380, grow");
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnContinue = new JButton("Dalje");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnContinue);
		cp.add(buttonHolder, "south");
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainFrame.getInstance().home();
			}
		});
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = clientList.getSelectedValue();
				if (o == null) {
					JOptionPane.showMessageDialog(null, "Izaberite klijenta.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String value = (String) o;
				String username = value.substring(value.lastIndexOf(",") + 2);
				currentClient = ClientManager.getInstance().getClientByUsername(username);
				refreshData();
				layout.show(panelContainer, "OfferPanel");
			}
		});
	}
	
	protected void initOfferPanel(){
		offerPanel.setLayout(new MigLayout("wrap" + Debug.debugCons()));
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(new Color(209, 255, 250));
		JTextField typeField = new JTextField(8);
		JTextField lenghtField = new JTextField(8);
		JComboBox<String> lenghtCompareBox = new JComboBox<String>(new String[] {"<", "=", ">"});
		JTextField priceField = new JTextField(8);
		JComboBox<String> priceCompareBox = new JComboBox<String>(new String[] {"<", "=", ">"});
		searchPanel.add(new JLabel("Tip:"));
		searchPanel.add(typeField);
		searchPanel.add(new JLabel("Dužina (min):"));
		searchPanel.add(lenghtField);
		searchPanel.add(lenghtCompareBox);
		searchPanel.add(new JLabel("Cena:"));
		searchPanel.add(priceField);
		searchPanel.add(priceCompareBox);
		offerPanel.add(searchPanel, "north");
		
		offerPanel.add(new JLabel("Izaberite tretman:"), "north");

		// podesavanje tabele
		table = new JTable(new OfferModel());		
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		JScrollPane sc = new JScrollPane(table);
		offerPanel.add(sc, "hmax 370, grow");

		tableSorter.setModel((AbstractTableModel) table.getModel());
		table.setRowSorter(tableSorter);
		
		TableColumnModel tcm = table.getColumnModel();
		tcm.removeColumn(tcm.getColumn(0)); // remove the ID column
		tcm.removeColumn(tcm.getColumn(4)); // remove the minutes column
		tcm.removeColumn(tcm.getColumn(4)); // remove the deleted column

		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnContinue = new JButton("Dalje");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnContinue);
		offerPanel.add(buttonHolder, "south");
		
		typeField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (typeField.getText().trim().length() == 0)
					typeFilter = null;
				else
					typeFilter = RowFilter.regexFilter("(?i)" + typeField.getText().trim(), 2);
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(3);
				if (typeFilter != null) list.add(typeFilter);   
				if (lenghtFilter != null) list.add(lenghtFilter);   
				if (priceFilter != null) list.add(priceFilter);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		lenghtField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (lenghtField.getText().trim().length() == 0)
				    lenghtFilter = null;
				else {
					ComparisonType comType;
					if (lenghtCompareBox.getSelectedItem().equals("<"))
						comType = ComparisonType.BEFORE;
					else if (lenghtCompareBox.getSelectedItem().equals("="))
						comType = ComparisonType.EQUAL;
					else
						comType = ComparisonType.AFTER;
					try {
						lenghtFilter = RowFilter.numberFilter(comType, Integer.parseInt(lenghtField.getText().trim()), 5);
					}
					catch (Exception ex) {
						lenghtFilter = null;
					}
				}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(3);
				if (typeFilter != null) list.add(typeFilter);   
				if (lenghtFilter != null) list.add(lenghtFilter);   
				if (priceFilter != null) list.add(priceFilter);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		priceField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void removeUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void insertUpdate(DocumentEvent e) { changedUpdate(e); }
			@Override
			public void changedUpdate(DocumentEvent e) {
				if (priceField.getText().trim().length() == 0)
					priceFilter = null;
				else {
					ComparisonType comType;
					if (priceCompareBox.getSelectedItem().equals("<"))
						comType = ComparisonType.BEFORE;
					else if (priceCompareBox.getSelectedItem().equals("="))
						comType = ComparisonType.EQUAL;
					else
						comType = ComparisonType.AFTER;
					try {
						priceFilter = RowFilter.numberFilter(comType, Integer.parseInt(priceField.getText().trim()), 4);
					}
					catch (Exception ex) {
						priceFilter = null;
					}
				}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(3);
				if (typeFilter != null) list.add(typeFilter);   
				if (lenghtFilter != null) list.add(lenghtFilter);   
				if (priceFilter != null) list.add(priceFilter);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		lenghtCompareBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (lenghtField.getText().trim().length() == 0)
				    lenghtFilter = null;
				else {
					ComparisonType comType;
					if (lenghtCompareBox.getSelectedItem().equals("<"))
						comType = ComparisonType.BEFORE;
					else if (lenghtCompareBox.getSelectedItem().equals("="))
						comType = ComparisonType.EQUAL;
					else
						comType = ComparisonType.AFTER;
					try {
						lenghtFilter = RowFilter.numberFilter(comType, Integer.parseInt(lenghtField.getText().trim()), 5);
					}
					catch (Exception ex) {
						lenghtFilter = null;
					}
				}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(3);
				if (typeFilter != null) list.add(typeFilter);   
				if (lenghtFilter != null) list.add(lenghtFilter);   
				if (priceFilter != null) list.add(priceFilter);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		priceCompareBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (priceField.getText().trim().length() == 0)
					priceFilter = null;
				else {
					ComparisonType comType;
					if (priceCompareBox.getSelectedItem().equals("<"))
						comType = ComparisonType.BEFORE;
					else if (priceCompareBox.getSelectedItem().equals("="))
						comType = ComparisonType.EQUAL;
					else
						comType = ComparisonType.AFTER;
					try {
						priceFilter = RowFilter.numberFilter(comType, Integer.parseInt(priceField.getText().trim()), 4);
					}
					catch (Exception ex) {
						priceFilter = null;
					}
				}
				List<RowFilter<Object,Object>> list = new ArrayList<RowFilter<Object,Object>>(3);
				if (typeFilter != null) list.add(typeFilter);   
				if (lenghtFilter != null) list.add(lenghtFilter);   
				if (priceFilter != null) list.add(priceFilter);
				tableSorter.setRowFilter(RowFilter.andFilter(list));
			}
		});
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (Application.currentUser instanceof Client)
					MainFrame.getInstance().home();
				else
					layout.show(panelContainer, "ClientPanel");
			}
		});
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int red = table.getSelectedRow();
				if(red == -1) {
					JOptionPane.showMessageDialog(null, "Morate odabrati red u tabeli.", "Greska", JOptionPane.WARNING_MESSAGE);
				}else {
					UUID id = (UUID)table.getModel().getValueAt(tableSorter.convertRowIndexToModel(red), 0);
					TreatmentOffer offer = TreatmentManager.getInstance().getTreatmentOffer(id);
					if(offer != null) {
						currentOffer = offer;
						if(!initializedBeauticianPanel)
							initBeauticianPanel();
						else {
							beauticianListModel.removeAllElements();
							ArrayList<Beautician> arrayList = BeauticianManager.getInstance().getAllQualifiedBeauticians(offer.getTreatmentType());
							beauticianListModel.addElement("Ne želim da izaberem");
							for (Beautician b : arrayList)
								beauticianListModel.addElement(b);	
						}
						layout.show(panelContainer, "BeauticianPanel");
					}else {
						JOptionPane.showMessageDialog(null, "Nije moguce pronaći odabrani tretman!", "Greska", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
	}
	protected void initBeauticianPanel() {
		initializedBeauticianPanel = true;
		JPanel bp = beauticianPanel;

		bp.setLayout(new MigLayout("fill, wrap" + Debug.debugCons()));
		
		bp.add(new JLabel("Izaberite kozmetičara (ili samo pritisnite dalje):"), "north");
		
		ArrayList<Beautician> arrayList = BeauticianManager.getInstance().getAllQualifiedBeauticians(currentOffer.getTreatmentType());
		beauticianListModel = new DefaultListModel<>();
		beauticianListModel.addElement("Ne želim da izaberem");
		for (Beautician b : arrayList)
			beauticianListModel.addElement(b);
		beauticianList = new JList<Object>(beauticianListModel);
		beauticianList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane sc = new JScrollPane(beauticianList);
		bp.add(sc, "hmax 380, grow");
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnContinue = new JButton("Dalje");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnContinue);
		bp.add(buttonHolder, "south");
		
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.show(panelContainer, "OfferPanel");
			}
		});
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object o = beauticianList.getSelectedValue();
				if (o instanceof Beautician) 
					currentBeautician = (Beautician) o;
				else
					currentBeautician = null;
				if (!initializedTimePanel)
					initTimePanel();
				else{
					datePicker.getModel().setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
					datePicker.getModel().setSelected(true);
					ArrayList<LocalTime> freeTimes;
					freeTimes = BeauticianManager.getInstance().getFreeTimes(LocalDate.now(), currentOffer, currentBeautician);
					if (freeTimes.size() == 0) {
						timeBox.setEnabled(false);
						timeBox.setSelectedIndex(-1);
						timeBoxModel.removeAllElements();
						return;
					}
					timeBox.setEnabled(true);
					timeBoxModel.removeAllElements();
					for (LocalTime time : freeTimes)
						timeBoxModel.addElement(time);
					timeBox.setSelectedIndex(-1);
				}
				layout.show(panelContainer, "TimePanel");
			}
		});
	}
	protected void initTimePanel() {
		initializedTimePanel = true;
		JPanel tp = timePanel;
		
		tp.setLayout(new MigLayout("fill, wrap" + Debug.debugCons()));
		
		tp.add(new JLabel("Izaberite datum i vreme tretmana:"), "north");
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		datePicker = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		tp.add(datePicker, "growx 50, split");

		timeBoxModel = new DefaultComboBoxModel<>();
		timeBox = new JComboBox<>(timeBoxModel);
		timeBox.setEnabled(false);
		tp.add(timeBox, "growx 50");
		
		datePicker.getModel().setDate(LocalDate.now().getYear(), LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
		datePicker.getModel().setSelected(true);
		ArrayList<LocalTime> freeTimes;
		freeTimes = BeauticianManager.getInstance().getFreeTimes(LocalDate.now(), currentOffer, currentBeautician);
		if (freeTimes.size() == 0) {
			timeBox.setEnabled(false);
			timeBox.setSelectedIndex(-1);
			timeBoxModel.removeAllElements();
			return;
		}
		timeBox.setEnabled(true);
		timeBoxModel.removeAllElements();
		for (LocalTime time : freeTimes)
			timeBoxModel.addElement(time);
		timeBox.setSelectedIndex(-1);
		
		JPanel buttonHolder = new JPanel();
		JButton btnBack = new JButton("Nazad");
		JButton btnContinue = new JButton("Zakaži");
		buttonHolder.add(btnBack);
		buttonHolder.add(btnContinue);
		tp.add(buttonHolder, "south");
		
		datePicker.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date _date = (Date)datePicker.getModel().getValue();
				LocalDate date = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (date.isBefore(LocalDate.now())) {
					JOptionPane.showMessageDialog(null, "Ne možete birati datume koji su prošli!.", "Greska", JOptionPane.ERROR_MESSAGE);
					datePicker.getModel().setDate(LocalDate.now().getYear(),LocalDate.now().getMonthValue()-1,LocalDate.now().getDayOfMonth());
					return;
				}
				ArrayList<LocalTime> freeTimes;
				freeTimes = BeauticianManager.getInstance().getFreeTimes(date, currentOffer, currentBeautician);
				if (freeTimes.size() == 0) {
					timeBox.setEnabled(false);
					timeBox.setSelectedIndex(-1);
					timeBoxModel.removeAllElements();
					return;
				}
				timeBox.setEnabled(true);
				LocalTime previousTime = (LocalTime) timeBox.getSelectedItem();
				timeBoxModel.removeAllElements();
				for (LocalTime time : freeTimes)
					timeBoxModel.addElement(time);
				if (previousTime != null && freeTimes.contains(previousTime))
					timeBox.setSelectedIndex(freeTimes.indexOf(previousTime));
				else
					timeBox.setSelectedIndex(-1);
			}
		});
		btnBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				layout.show(panelContainer, "BeauticianPanel");
			}
		});
		btnContinue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Date _date = (Date)datePicker.getModel().getValue();
				if (_date == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali datum.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				LocalDate date = _date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				LocalTime time = (LocalTime) timeBox.getSelectedItem();
				if (time == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali vreme.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					Receptionist r;
					if (Application.currentUser instanceof Receptionist)
						r = (Receptionist)Application.currentUser;
					else
						r = null;
					if (currentBeautician == null)
						TreatmentManager.getInstance().scheduleTreatment(currentOffer, currentClient, LocalDateTime.of(date, time), r);
					else
						TreatmentManager.getInstance().scheduleTreatment(currentOffer, currentBeautician, currentClient, LocalDateTime.of(date, time), r);
				} catch (TreatmentException e1) {
					JOptionPane.showMessageDialog(null, "Nismo uspeli da zakažemo tretman.", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}
				JOptionPane.showMessageDialog(null, "Termin uspešno zakazan!", "Uspeh", JOptionPane.INFORMATION_MESSAGE);
				
				MainFrame.getInstance().home();
				if (Application.currentUser instanceof Receptionist)					
					layout.show(panelContainer, "ClientPanel");
				else
					layout.show(panelContainer, "OfferPanel");
			}
		});
	}
}
