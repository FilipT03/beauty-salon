package salon;

public enum TreatmentStatus {
	ZAKAZAN("Zakazan"), IZVRŠEN("Izvršen"), OTKAZAO_KLIJENT("Otkazao klijent"), OTKAZAO_SALON("Otkazao salon"), NIJE_SE_POJAVIO("Nije se pojavio");
	
	private String action;
	
	private TreatmentStatus(String action) {
		this.action = action;
	}
	
	public boolean noMoneyLost() {
		return this == ZAKAZAN || this == IZVRŠEN || this == NIJE_SE_POJAVIO;
	}
	
	@Override
	public String toString() {
		return action;
	}
	
	public static TreatmentStatus parse(String string) {
		switch (string) {
		case "Izvršen":
			return IZVRŠEN;
		case "Otkazao klijent":
			return OTKAZAO_KLIJENT;
		case "Otkazao salon":
			return OTKAZAO_SALON;
		case "Nije se pojavio":
			return NIJE_SE_POJAVIO;
		case "Zakazan":
		default:
			return ZAKAZAN;
		}
	}
}
