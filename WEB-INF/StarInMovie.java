public class StarInMovie{
	private String id;
	private String title;
	private String actor;

	public StarInMovie(String id, String title, String actor){
		this.id = id;
		this.title = title;
		this.actor = actor;
	}

	public String getID(){return id;}
	public String getTitle(){return title;}
	public String getActor(){return actor;}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append(getActor() + " - ");
		sb.append(getTitle() + ", ");
		sb.append(getID());
		return sb.toString();
	}
} 
