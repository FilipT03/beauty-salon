package gui;


public enum PanelName {
	USER_PANEL("UserPanel"), CLIENT_BEAUTICIAN_TREATMENTS_PANEL("ClientBeauticianTreatmentsPanel"),
	SCHEDULE_PANEL("SchedulePanel"), RECEPTIONIST_TREATMENT_PANEL("ReceptionistTreatmentsPanel");
	
	private String name;
	
	private PanelName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
