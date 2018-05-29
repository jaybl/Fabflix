public class Movie{
	private String id;
	private String title;
	private int year;
	private String director;
	private String genre;

	public Movie(String id, String title, int year, String director, String genre){
		this.id = id;
		this.title = title;
		this.year = year;
		this.director = director;
		this.genre = genre;
	}

	public String getTitle(){return title;}
	public void setTitle(String title){this.title = title;}
	public String getDirector(){return director;}
	public void setDirector(String director){this.director = director;}
	public int getYear(){return year;}
	public void setYear(int year){this.year = year;}
	public String getGenre(){return genre;}
	public void setGenre(String genre){this.genre = genre;}
	public String getID(){return id;}

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Movie(");
		sb.append(getID() + ", ");
		sb.append(getTitle() + ", ");
		sb.append(getYear() + ", ");
		sb.append(getDirector() + ", ");
		sb.append(getGenre() + ")");
		return sb.toString();
	}
}
