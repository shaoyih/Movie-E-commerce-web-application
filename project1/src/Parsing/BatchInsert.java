package Parsing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;
import javax.annotation.Resource;

public class BatchInsert {
	private static HashSet<Movie> movies;
	private static HashMap<String,Integer> genresMap;
	private static HashMap<String,List<Star>> stars;
	private static HashMap<String,Star> actors;
	private static HashSet<String> movies_id;
	@Resource(name = "jdbc/moviedb")
    private DataSource dataSource;

	 private Connection dbcon ;
	 
	 public BatchInsert() {
		 try {
				Final_parse gp = new Final_parse();
				genresMap=gp.getGenres();
				
				SIM_parse sp = new SIM_parse();
				sp.parseDocument();
				stars=sp.getMovieStar();
				actors=sp.getActor();
				Movie_parse mp = new Movie_parse();
				mp.parseDocument();
				movies=mp.getMovies();
				movies_id=mp.getMoviesID();
				
				
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	String jdbcURL="jdbc:mysql://localhost:3306/moviedb";
	    	try {
				dbcon = DriverManager.getConnection(jdbcURL,"mytestuser", "mypassword");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	 
	
	private  int batchInsert() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PreparedStatement psInsertRecord=null;
    	String sqlInsertRecord="INSERT INTO movies (id, title, year, director)\n" + 
    			"VALUES (?,?,?,?);";
    	int[] iNoRows;
    	
    	
    	int count=0;
    
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			
    		System.out.println("starting to insert movie");
			dbcon.setAutoCommit(false);
		
		    psInsertRecord=dbcon.prepareStatement(sqlInsertRecord);
		    Iterator it = movies.iterator();
		    
	        while (it.hasNext()) 
		   	     {
	        	 	
	        	 	Movie movie=(Movie) it.next();
	        	 		String id=movie.getId();
	        	 		String title=movie.getTitle();
	        	 		int year=movie.getYear();
	        	 		String director=movie.getDirector();
	        	 		
	        	 		    ++count;
	        	 			
			    		    psInsertRecord.setString(1, id);
			    		    psInsertRecord.setString(2, title);
			    		    psInsertRecord.setInt(3, year);
			    		    psInsertRecord.setString(4, director);
			    		    psInsertRecord.setString(5, id);
			    		    psInsertRecord.addBatch();
	        	 	
		    	 }
		    		
		    iNoRows=psInsertRecord.executeBatch();
		    dbcon.commit();
    	
		   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
    	System.out.println("finished inserting movies");
    	return count;
		
	}
	private  int batchInsertGenre() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PreparedStatement psInsertRecord=null;
		
    	String sqlInsertRecord="INSERT INTO genres_in_movies (genreId, movieId)\n" + 
    			"VALUES (?,?);";
    	int[] iNoRows;

    	int count=0;
    	try {
    	

			dbcon.setAutoCommit(false);
			System.out.println("starting to insert genres_in_movies");
		    psInsertRecord=dbcon.prepareStatement(sqlInsertRecord);
		    Iterator it = movies.iterator();
		    
	        while (it.hasNext()) 
		   	     {
	        	 	
	        	Movie movie=(Movie) it.next();
    	 		String id=movie.getId();
    	 		List<String> genres=movie.getGenre();
    	 		if (genres==null ||genres.size()==0) {
    	 			//????
    	 			System.out.println("movie "+movie.getTitle()+" doesn't have genre");
    	 			continue;
    	 		}
    	 		else {
    	 			Iterator g = genres.iterator();
    	 			while (g.hasNext()) {
   	        	 	
    	 					String genre_name=((String) g.next()).toLowerCase();
    	 					
    	 					
    	 					int genre_id=genresMap.get(genre_name);

	        	 		    ++count;
	        	 			
			    		    psInsertRecord.setInt(1, genre_id);
			    		    
			    		    psInsertRecord.setString(2, id);

			    		    psInsertRecord.addBatch();
    	 					
    	 			}
    	 			
    	 		}
	        	 	
	        	 	
		    	 }
		    		
		    iNoRows=psInsertRecord.executeBatch();
		    dbcon.commit();
		   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
    	System.out.println("finished inserting genres_in_movies");
    	return count;
		
	}
	private  int batchInsertStar() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PreparedStatement psInsertRecord=null;
		PreparedStatement psInsertRecord1=null;
    	String sqlInsertRecord="insert into stars(id, name,birthYear) VALUES(?,?,default);";
    	String sqlInsertRecord1="insert into stars(id, name,birthYear) VALUES(?,?,?);";
    	HashSet<Star> haveBirth=new HashSet<Star> ();
    	int[] iNoRows;

    	int count=0;
    	try {
    		System.out.println("starting to insert stars");

			dbcon.setAutoCommit(false);
		
		    psInsertRecord=dbcon.prepareStatement(sqlInsertRecord);
		    Iterator it = actors.values().iterator();
		    
	        while (it.hasNext()){
	        	 	
	        	Star star=(Star) it.next();
    	 		String id=star.getId();
    	 		String name=star.getName();
    	 		int dob=star.getDob();
    	 		
    	 		if (dob!=0) {
    	 			haveBirth.add(star);
    	 			continue;
    	 		}
    	 		else {
    	 			
	        	 		    ++count;
	        	 			
			    		    psInsertRecord.setString(1, id);
			    		    
			    		    psInsertRecord.setString(2, name);
			    		    psInsertRecord.addBatch();
    	 					
    	 		
    	 		} 	
		   }
		    		
		    psInsertRecord.executeBatch();
		    dbcon.commit();
		   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	try {
			dbcon.setAutoCommit(false);
		
		    psInsertRecord1=dbcon.prepareStatement(sqlInsertRecord1);
		    Iterator it = haveBirth.iterator();
		    
	        while (it.hasNext()){
	        	 	
	        	Star star=(Star) it.next();
    	 		String id=star.getId();
    	 		String name=star.getName();
    	 		int dob=star.getDob();
    	 	
	        	 ++count;
	        	 			
			    psInsertRecord1.setString(1, id);
			    		    
			    psInsertRecord1.setString(2, name);
			    psInsertRecord1.setInt(3, dob);
			    psInsertRecord1.addBatch();
    	 					
    	 		
	        }
		  
		    		
		    psInsertRecord1.executeBatch();
		    dbcon.commit();
		   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	
    	
    	System.out.println("finished inserting stars");
		return count;
		
	}
	private  int batchInsertStarMovie() throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PreparedStatement psInsertRecord=null;
		
    	
    	String sqlInsertRecord="insert into stars_in_movies (starId, movieId)" + 
    			"VALUES (?,?);";
    	int[] iNoRows;

    	int count=0;
    	try {
    		Class.forName("com.mysql.jdbc.Driver").newInstance();

			dbcon.setAutoCommit(false);
			System.out.println("starting to insert stars_in_movies");
		    psInsertRecord=dbcon.prepareStatement(sqlInsertRecord);
		    Iterator it = movies.iterator();
		    
	        while (it.hasNext()) 
		   	     {
	        	 	
	        	Movie movie=(Movie) it.next();
    	 		String id=movie.getId();
    	 		List<Star> star_list=stars.get(id);
    	 		if (star_list==null ||star_list.size()==0) {
    	 			//????
    	 			
    	 			System.out.println("movie "+movie.getTitle()+" "+movie.getId()+" doesn't have stars "+star_list);
    	 			continue;
    	 		}
    	 		else {
    	 			Iterator g = star_list.iterator();
    	 			while (g.hasNext()) {
   	        	 	
    	 					Star single_star=(Star) g.next();
    	 					
    	 					String star_id=single_star.getId();

	        	 		    ++count;
	        	 			
			    		    psInsertRecord.setString(1, star_id);
			    		    
			    		    psInsertRecord.setString(2, id);

			    		    psInsertRecord.addBatch();
    	 					
    	 			}
    	 			
    	 		}
	        	 	
	        	 	
		    	 }
		    		
		    iNoRows=psInsertRecord.executeBatch();
		    dbcon.commit();
		   
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
    	try {
	           if(psInsertRecord!=null) psInsertRecord.close();
	           if(dbcon!=null) dbcon.close();
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
    	
    	System.out.println("finished inserting stars_in_movies");
		return count;
		
	}
	
	
	 public static void main(String[] args) {
		long tStart = System.currentTimeMillis(); 
		BatchInsert sp = new BatchInsert();
	
//		System.out.println(stars);
//		System.out.println(genresMap);
		HashSet<String> ids=new HashSet<>();

		try {
			int c1=sp.batchInsert();
			int c2=sp.batchInsertGenre();
			int c3=sp.batchInsertStar();
			int c4=sp.batchInsertStarMovie();
			System.out.println("# of movies :  "+c1 );
			System.out.println("# of genres—in-movie: "+c2 );
			System.out.println("# of genres "+genresMap.size() );
			
			System.out.println("# of stars :"+c3 );
			System.out.println("# of stars_in_movies :"+c4 );
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(movies.size());
	    long tEnd = System.currentTimeMillis();
	    long tDelta = tEnd - tStart;
	    double elapsedSeconds = tDelta / 1000.0;
	    System.out.println("time used: "+elapsedSeconds );
}

	

}