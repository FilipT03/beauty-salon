package gui.otherdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import debug.Debug;
import gui.MainFrame;
import managers.BeauticianManager;
import managers.ReceptionistManager;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentType;
import users.Beautician;
import users.Gender;
import users.Receptionist;

public class RegisterWorkerDialog extends JDialog{
	private static final long serialVersionUID = -2244714954398336928L;

	protected boolean beautician;
	
	public RegisterWorkerDialog(boolean beautician) {
		super(MainFrame.getInstance(), true);
		this.beautician = beautician;
		
		if (beautician)
			this.setTitle("Registracija kozmetičara");
		else
			this.setTitle("Registracija recepcionera");
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.setResizable(false);
		initDialog();
		this.pack();
	}

	private void initDialog() {
		MigLayout layout = new MigLayout("wrap 2" + Debug.debugCons(), "[][]", "[]20[][][][][][][][][]20[]");
		if (beautician)
			layout.setRowConstraints("[]20[][][][][][][][][][]20[]");
		this.setLayout(layout);

		JTextField tfName = new JTextField(20);
		JTextField tfSurname = new JTextField(20);
		JTextField tfPhone = new JTextField(20);
		JTextField tfAdress = new JTextField(20);
		String[] genders = {"Muški", "Ženski"};
		JComboBox<String> cbGender = new JComboBox<String>(genders);
		JTextField tfUsername = new JTextField(20);
		JTextField pfPassword = new JTextField(20);
		JTextField tfQualLevel = new JTextField(20);
		JTextField tfWorkExp = new JTextField(20);
		JList<TreatmentType> listSpecializations;
		JScrollPane sc;
		if (beautician) {
			listSpecializations = new JList<TreatmentType>(TreatmentManager.getInstance().getActiveTreatmentTypesArray());
			sc = new JScrollPane(listSpecializations);
		}
		else {
			listSpecializations = null;
			sc = null;
		}

		JButton btnOk = new JButton("Potvrdi");
		JButton btnCancel = new JButton("Otkaži");

		this.getRootPane().setDefaultButton(btnOk);
		
		if (beautician)
			this.add(new JLabel("Uneniste podatke kozmetičara: "), "span 2");
		else
			this.add(new JLabel("Uneniste podatke recepcionera: "), "span 2");
		this.add(new JLabel("Ime:"));
		this.add(tfName);
		this.add(new JLabel("Prezime:"));
		this.add(tfSurname);
		this.add(new JLabel("Adresa:"));
		this.add(tfAdress);
		this.add(new JLabel("Telefon:"));
		this.add(tfPhone);
		this.add(new JLabel("Pol:"));
		this.add(cbGender);
		this.add(new JLabel("Korisničko ime:"));
		this.add(tfUsername);
		this.add(new JLabel("Lozinka:"));
		this.add(pfPassword);
		this.add(new JLabel("Nivo st. spreme:"));
		this.add(tfQualLevel);
		this.add(new JLabel("Staž:"));
		this.add(tfWorkExp);
		if (beautician){
			this.add(new JLabel("Specijalizacije:"));
			this.add(sc, "hmax 40, grow");
		}
		this.add(btnOk, "split 2, span, grow 33");
		this.add(btnCancel, "grow 33");
		
		// Login
		btnOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String name = tfName.getText().trim();
				String surname = tfSurname.getText().trim();
				String adress = tfAdress.getText().trim();
				String phone = tfPhone.getText().trim();
				Gender gender = cbGender.getSelectedItem().equals("Muški") ? Gender.male : Gender.female;
				String username = tfUsername.getText().trim();
				String password = pfPassword.getText().trim();
				Short qualLevel = -1;
				try {
					qualLevel = Short.parseShort(tfQualLevel.getText());
				}
				catch (NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Nivo st. spreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				Float workExp = -1f;
				try {
					workExp = Float.parseFloat(tfWorkExp.getText());
				}
				catch (NullPointerException ex) {
					JOptionPane.showMessageDialog(null, "Staž nije unet!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;
				}	
				catch (NumberFormatException ex){
					JOptionPane.showMessageDialog(null, "Staž nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
					return;	
				}
				ArrayList<TreatmentType> specializations;
				if (beautician) {
					List<TreatmentType> list = listSpecializations.getSelectedValuesList();
					specializations = new ArrayList<>();
					for (Object o : list)
						specializations.add((TreatmentType) o);
				}
				else
					specializations = null;
				String error;
				if (beautician) 
					error = BeauticianManager.getInstance().register(new Beautician(name, surname, phone, adress, username, password, gender, qualLevel, workExp, 0, specializations));
				else
					error = ReceptionistManager.getInstance().register(new Receptionist(name, surname, phone, adress, username, password, gender, qualLevel, workExp, 0, 0));
				if (error != null) {
					JOptionPane.showMessageDialog(null, error, "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				RegisterWorkerDialog.this.setVisible(false);
				RegisterWorkerDialog.this.dispose();
			}
		});
		
		// Cancel
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				RegisterWorkerDialog.this.setVisible(false);
				RegisterWorkerDialog.this.dispose();
			}
		});
	}

}
