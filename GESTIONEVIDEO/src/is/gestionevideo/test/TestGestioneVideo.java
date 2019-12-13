package is.gestionevideo.test;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import is.gestionevideo.control.GestioneVideo;
import is.gestionevideo.database.DBManager;
import is.gestionevideo.entity.Sport;
import is.gestionevideo.entity.Video;
import is.gestionevideo.entity.VideoEvento;

public class TestGestioneVideo {
	
	GestioneVideo gestionevideo;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		try {
			
			Connection conn = DBManager.getConnection();
			
			String query;
			
			query = "CREATE TABLE VIDEO("
					+" ID INT NOT NULL AUTO_INCREMENT PRIMARY KEY,"
					+" NOME VARCHAR(30),"
					+" DATA DATE,"
					+" SPORT VARCHAR(30),"
					+" TIPO VARCHAR(30)"
					+");";
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			query = "CREATE TABLE GIORNALISTI("
					+" ID_VIDEO INT NOT NULL,"
					+" NOME VARCHAR(30) NOT NULL,"
					+" COGNOME VARCHAR(30) NOT NULL,"
					+" PRIMARY KEY(ID_VIDEO,NOME,COGNOME)"
					+");";
			
			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			
			System.out.println("Inizializzazione DB completata.");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
		
		try {
			
			Connection conn = DBManager.getConnection();
			
			String query;
			
			query = "DROP TABLE GIORNALISTI; DROP TABLE VIDEO;";

			
			try(PreparedStatement stmt = conn.prepareStatement(query)) {
				
				stmt.executeUpdate();
			}
			
			
			System.out.println("Rimozione DB completata.");
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Before
	public void setUp() throws Exception {
		
		gestionevideo = new GestioneVideo();
	}

	@After
	public void tearDown() throws Exception {
		
		gestionevideo = null;
		
		
		Connection conn = DBManager.getConnection();
		
		
		String query = "DELETE FROM VIDEO;";
		
		try(PreparedStatement stmt = conn.prepareStatement(query)) {
			
			stmt.executeUpdate();
		}
		
				
		String query2 = "DELETE FROM GIORNALISTI;";

		try(PreparedStatement stmt2 = conn.prepareStatement(query2)) {
			
			stmt2.executeUpdate();
		}
	}

	
	
	@Test
	public void test01UnVideoPerNomeNonTrovato() throws SQLException {

		//ricerca per nome, un video, nessun risultato
		//inserisco un video v1, lo carico tramite caricaVideo in gestionevideo
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		//eseguo la ricerca tramite la funzione ricercaVideo di gestionevideo (istanza della classe control) di un video tramite parola chiave "samp" nel nome, e nient'altro
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Samp");
		
		//stampo i risultati della ricerca (quali video ha trovato, col for) e la size della ricerca (quanti video ha trovato)
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		//verifico che la ricerca ha prodotto 0 risultati
		assertEquals(0, v_ricerca.size());
		
		//dopodichè rimuovo il video dalla classe gestionevideo (pulisco)
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test02UnVideoPerSportNonTrovato() throws SQLException {

		//ricerca per sport, un video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test03UnVideoPerNomeESportNonTrovato() throws SQLException {

		//ricerca per nome e sport, un video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test04UnVideoPerNomeUnRisultato() throws SQLException {

		//ricerca per nome, un video, un risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test05UnVideoPerSportUnRisultato() throws SQLException {

		//ricerca per sport, un video, un risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, null);
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test06UnVideoPerNomeESportUnRisultato() throws SQLException {

		//ricerca per nome e sport, un video, un risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		gestionevideo.caricaVideo(v1);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Napoli");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	}
	
	@Test
	public void test07PiuVide0oPerNomeNessunRisultato() throws SQLException {

		//ricerca per sport, piu' video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
	
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Milan");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	}
	
	
	
	
	@Test
	public void test08PiuVideoPerSportNessunRisultato() throws SQLException {

		//ricerca per sport, piu' video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
	
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	}
	
	
	@Test
	public void test09PiuVideoPerNomeESportNessunRisultato() throws SQLException {

		//ricerca per nome e sport, piu' video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
	
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Milan");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	}
	
	@Test
	public void test10PiuVideoPerNomeUnRisultato() throws SQLException {

		//ricerca per nome, piu' video, un risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
	
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Samp");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	}
	
	@Test
	public void test11PiuVideoPerSportUnRisultato() throws SQLException {

		//ricerca per nome e sport, piu' video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
		VideoEvento v4 = new VideoEvento("GP Monza ScrivoQualcosaPerEccedereI30CaratteriEFarFallireIlTestdsfddfsfsddsfdsffdsdfsdfsfsdfwregrev",
										 LocalDate.of(2020, Month.APRIL, 8),
										 Sport.FORMULA1);
		
	
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		gestionevideo.caricaVideo(v4);
	
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.FORMULA1, null);
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
		gestionevideo.rimuoviVideo(v4);
	}
	
	@Test
	public void test12PiuVideoPerNomeESportUnRisultato() throws SQLException {

		//ricerca per nome e sport, piu' video, nessun risultato
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
	
		
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
			
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	
	}
	
	@Test
	public void test13PiuVideoPerNomePiuRisultati() throws SQLException {

		//ricerca per nome, più video, più risultati
		VideoEvento v1 = new VideoEvento("Partita Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Partita Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Partita Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
	
		
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
			
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(3, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	
	}
	
	@Test
	public void test14PiuVideoPerNomeApprofPiuRisultati() throws SQLException {

		//ricerca per nome, più video (approfondim.) , più risultati
		VideoEvento v1 = new VideoEvento("Commento Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Commento Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Commento Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
	
		
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
			
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(3, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
	
	}
	
	@Test
	public void test15PiuVideoMistiPerNomePiuRisultati() throws SQLException {

		//ricerca per nome, più video (misti) , più risultati
		VideoEvento v1 = new VideoEvento("Evento Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
		
		VideoEvento v2 = new VideoEvento("Evento Napoli-Sampdoria", 
										 LocalDate.of(2019, Month.NOVEMBER, 29), 
										 Sport.CALCIO);
		
		VideoEvento v3 = new VideoEvento("Evento Juventus-Napoli", 
									 	 LocalDate.of(2019, Month.AUGUST, 31), 
										 Sport.CALCIO);
		
		VideoEvento v4 = new VideoEvento("Commento Napoli-Sampdoria", 
				 						 LocalDate.of(2019, Month.NOVEMBER, 29), 
				 						 Sport.CALCIO);
	
		
		gestionevideo.caricaVideo(v1);
		gestionevideo.caricaVideo(v2);
		gestionevideo.caricaVideo(v3);
		gestionevideo.caricaVideo(v4);
			
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(Sport.CALCIO, "Samp");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(2, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
		gestionevideo.rimuoviVideo(v2);
		gestionevideo.rimuoviVideo(v3);
		gestionevideo.rimuoviVideo(v4);
	
	}
	
	@Test
	public void test16NessunVideoPerNome() throws SQLException {

		//ricerca per nome, più video (misti) , più risultati
		
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, "Napoli");
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(0, v_ricerca.size());
		
	}
	
	@Test
	public void test17UnVideoNoNomeNoSportUnRisultato() throws SQLException {

		//ricerca senza nome, senza sport, un video, un risultato
		VideoEvento v1 = new VideoEvento("Evento Fiorentina-Napoli", 
										 LocalDate.of(2019, Month.AUGUST, 24), 
										 Sport.CALCIO);
	
		
		gestionevideo.caricaVideo(v1);
			
		
		ArrayList<Video> v_ricerca = gestionevideo.ricercaVideo(null, null);
		
	
		System.out.println("Risultati della ricerca: " + v_ricerca.size());
		
		for(Video v : v_ricerca) {
			
			System.out.println(v+"\n");
		}

		
		assertEquals(1, v_ricerca.size());
		
		gestionevideo.rimuoviVideo(v1);
	
	}
	
	
	
	
	
	
	
}
