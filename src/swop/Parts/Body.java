package swop.Parts;

public class Body extends Part{
	
	public Body() {
		super(new String[] {"sedan", "break"}, "Body");
	}

	public Body(String value) {
		super(value, new String[] {"sedan", "break"}, "Body");
	}

}