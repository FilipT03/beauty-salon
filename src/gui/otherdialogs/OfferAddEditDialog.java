package gui.otherdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MainFrame;
import gui.otherpanels.OfferPanel;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentOffer;
import salon.TreatmentType;

public class OfferAddEditDialog extends JDialog {
	private static final long serialVersionUID = -780596202714142314L;
	private TreatmentOffer editO;
	private JPanel parent;
	
	public OfferAddEditDialog(TreatmentOffer offer, JPanel parent) {
		super(MainFrame.getInstance(), true);
		this.parent = parent;
		setTitle("Izmena ponude");
		this.editO = offer;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		initGUI();
		pack();
	}

	private void initGUI() {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]");
		setLayout(ml);
		
		
		JComboBox<TreatmentType> typeCB = new JComboBox<TreatmentType>(TreatmentManager.getInstance().getActiveTreatmentTypesArray());
		
		JTextField name;
		JTextField lenght;
		JTextField price;
		
		if (editO != null) {
			typeCB.setSelectedIndex(TreatmentManager.getInstance().getActiveTreatmentTypes().indexOf(editO.getTreatmentType()));
			name = new JTextField(editO.getName(), 14);
			lenght = new JTextField(editO.getLenght().toString(), 14);
			price = new JTextField(Double.toString(editO.getPrice()), 14);
		}
		else {
			name = new JTextField(14);
			lenght = new JTextField(14);
			price = new JTextField(14);
		}

		add(new JLabel("Ime"));
		add(name, "span 2");
		add(new JLabel("Tip"));
		add(typeCB, "span 2");
		add(new JLabel("Dužina"));
		add(lenght, "span 2");
		add(new JLabel("Cena"));
		add(price, "span 2");
		
		add(new JLabel());

		JButton btnCancel = new JButton("Odustani");
		add(btnCancel);

		JButton btnOK = new JButton("OK");
		add(btnOK);

		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				TreatmentOffer o;
				if (editO != null) {
					o = TreatmentOffer.parse(TreatmentOffer.parse((TreatmentOffer) editO));
					o.setDeleted(editO.isDeleted());
				}
				else {
					o = new TreatmentOffer();
					o.setDeleted(false);
				}
				if (name.getText().equals("") || name.getText() == null) {
					JOptionPane.showMessageDialog(null, "Ime ne sme biti prazno!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				if (lenght.getText().equals("") || lenght.getText() == null) {
					JOptionPane.showMessageDialog(null, "Dužina ne sme biti prazna!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				if (price.getText().equals("") || price.getText() == null) {
					JOptionPane.showMessageDialog(null, "Cena ne sme biti prazna!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				if (typeCB.getSelectedItem() == null) {
					JOptionPane.showMessageDialog(null, "Niste izabrali tip!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				o.setName(name.getText());
				o.setTreatmentType((TreatmentType)typeCB.getSelectedItem());
				try {
					o.setPrice(Double.parseDouble(price.getText()));
				}
				catch (NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Cena nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				try {
					o.setLenght(LocalTime.parse(lenght.getText()));
				}
				catch (DateTimeParseException ex){
					JOptionPane.showMessageDialog(null, "Dužina nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				if (editO != null)
					TreatmentManager.getInstance().updateTreatmentOffer(o);
				else
					TreatmentManager.getInstance().addTreatmentOffer(o);					
				((OfferPanel) parent).refreshData();
				OfferAddEditDialog.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OfferAddEditDialog.this.dispose();
			}
		});
	}

}
