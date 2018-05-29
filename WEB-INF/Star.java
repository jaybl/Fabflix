public class Star{
	private String stagename;
	private String first;
	private String last;
	private int year;
	
	public Star(String stagename, String first, String last, int year){
		this.stagename = stagename;
		this.first = first;
		this.last = last;
		this.year = year;
	}

	public String getStageName(){return stagename;}
	public String getFirstName(){return first;}
	public String getLastName(){return last;}
	public int getDOB(){return year;}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Star(" + getStageName());
		sb.append(", " + getDOB() + ")");
		return sb.toString();
	}
}
