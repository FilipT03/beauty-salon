package gui.otherdialogs;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gui.MainFrame;
import gui.otherpanels.WorkersPanel;
import managers.BeauticianManager;
import managers.ReceptionistManager;
import managers.TreatmentManager;
import net.miginfocom.swing.MigLayout;
import salon.TreatmentType;
import users.Beautician;
import users.Gender;
import users.Receptionist;
import users.User;

public class WorkerEditDialog extends JDialog {
	private static final long serialVersionUID = -5247231764310200252L;
	private User editU;
	private JPanel parent;
	
	public WorkerEditDialog(User editUser, JPanel parent) {
		super(MainFrame.getInstance(), true);
		this.parent = parent;
		setTitle("Izmena radnika");
		this.editU = editUser;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		initGUI();
		pack();
	}

	private void initGUI() {
		MigLayout ml = new MigLayout("wrap 3", "[][][]", "[]10[]10[]10[]10[]10[]10[]10[]10[]10[]");
		setLayout(ml);
		
		
		JComboBox<Gender> genderCB = new JComboBox<Gender>(new Gender[] {Gender.male, Gender.female});
		genderCB.setSelectedIndex(editU.getGender() == Gender.male ? 0 : 1);
		
		JTextField name = new JTextField(editU.getName(), 14);
		JTextField surname = new JTextField(editU.getSurname(), 14);
		JTextField phone = new JTextField(editU.getPhone(), 14);
		JTextField adress = new JTextField(editU.getAdress(), 14);
		JTextField username = new JTextField(editU.getUsername(), 14);
		JTextField password = new JTextField(editU.getPassword(), 14);
		JTextField qualificationLevel;
		JTextField workExperience;
		JTextField specializations;
		if (editU instanceof Beautician) {
			qualificationLevel = new JTextField(Short.toString(((Beautician)editU).getQualificationLevel()), 14);
			workExperience = new JTextField(Float.toString(((Beautician)editU).getWorkExperience()), 14);
			String[] specializationStrings = new String[((Beautician)editU).getSpecializations().size()];
			for(int i = 0 ; i < ((Beautician)editU).getSpecializations().size(); i++)
				specializationStrings[i] = ((Beautician)editU).getSpecializations().get(i).name;
			String specializationsS = String.join("|", specializationStrings);
			specializations = new JTextField(specializationsS, 14);
		}
		else {
			qualificationLevel = new JTextField(Short.toString(((Receptionist)editU).getQualificationLevel()), 14);
			workExperience = new JTextField(Float.toString(((Receptionist)editU).getWorkExperience()), 14);
			specializations = null;
		}

		add(new JLabel("Ime"));
		add(name, "span 2");
		add(new JLabel("Prezime"));
		add(surname, "span 2");
		add(new JLabel("Telefon"));
		add(phone, "span 2");
		add(new JLabel("Adresa"));
		add(adress, "span 2");
		add(new JLabel("Korisničko ime"));
		add(username, "span 2");
		add(new JLabel("Sifra"));
		add(password, "span 2");
		add(new JLabel("Pol"));
		add(genderCB, "span 2");
		add(new JLabel("Nivo st. spreme"));
		add(qualificationLevel, "span 2");
		add(new JLabel("Staž"));
		add(workExperience, "span 2");
		if (editU instanceof Beautician) {
			add(new JLabel("Specijalizacija"));
			add(specializations, "span 2");
		}
		
		add(new JLabel());

		JButton btnCancel = new JButton("Odustani");
		add(btnCancel);

		JButton btnOK = new JButton("OK");
		add(btnOK);

		btnOK.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (editU instanceof Beautician) {
					Beautician u = Beautician.parse(Beautician.parse((Beautician) editU));
					u.setDeleted(editU.isDeleted());
					u.setName(name.getText());
					u.setSurname(surname.getText());
					u.setPhone(phone.getText());
					u.setAdress(adress.getText());
					u.setUsername(username.getText());
					u.setPassword(password.getText());
					u.setGender((Gender) genderCB.getSelectedItem());
					try {
						u.setQualificationLevel(Short.parseShort(qualificationLevel.getText()));
					}
					catch (NumberFormatException ex){
						JOptionPane.showMessageDialog(null, "Nivo st. spreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					try {
						u.setWorkExperience(Float.parseFloat(workExperience.getText()));
					}
					catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, "Staž nije unet!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}	
					catch (NumberFormatException ex){
						JOptionPane.showMessageDialog(null, "Staž nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					String[] ss = specializations.getText().split(" *\\| *");
					ArrayList<TreatmentType> types = new ArrayList<>();
					for(int i = 0; i < ss.length; i++) {
						if (TreatmentManager.getInstance().getTreatmentType(ss[i]) == null) {
							JOptionPane.showMessageDialog(null, "Polje specijalizacije nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
							return;
						}
						types.add(TreatmentManager.getInstance().getTreatmentType(ss[i]));
					}
					u.setSpecializations(types);
					String p = Beautician.isValid(u);
					if (p != null) {
						JOptionPane.showMessageDialog(null, p, "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					BeauticianManager.getInstance().updateBeautician(u);
				}
				else {
					Receptionist u = Receptionist.parse(Receptionist.parse((Receptionist) editU));
					u.setDeleted(editU.isDeleted());
					u.setName(name.getText());
					u.setSurname(surname.getText());
					u.setPhone(phone.getText());
					u.setAdress(adress.getText());
					u.setUsername(username.getText());
					u.setPassword(password.getText());
					u.setGender((Gender) genderCB.getSelectedItem());
					try {
						u.setQualificationLevel(Short.parseShort(qualificationLevel.getText()));
					}
					catch (NumberFormatException ex){
						JOptionPane.showMessageDialog(null, "Nivo st. spreme nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					try {
						u.setWorkExperience(Float.parseFloat(workExperience.getText()));
					}
					catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, "Staž nije unet!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;
					}	
					catch (NumberFormatException ex){
						JOptionPane.showMessageDialog(null, "Staž nije u dobrom formatu!", "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					String p = Receptionist.isValid(u);
					if (p != null) {
						JOptionPane.showMessageDialog(null, p, "Greska", JOptionPane.ERROR_MESSAGE);
						return;	
					}
					ReceptionistManager.getInstance().updateReceptionist(u);
				}
				((WorkersPanel) parent).refreshData();
				WorkerEditDialog.this.dispose();
			}
		});

		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WorkerEditDialog.this.dispose();
			}
		});
	}

}
