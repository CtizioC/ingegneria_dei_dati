package ing_dei_dati.hw3;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;



public class App 
{
    public static void main( String[] args ) throws IOException, ParseException
    {
    	Indexer indexer=new Indexer("./DirectoryIndici");
    	indexer.index();
    	
        Path path = Paths.get("./DirectoryIndici");
        Directory directory = FSDirectory.open(path);
        
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
        
        
        /*QueryParser queryParser = new QueryParser(“contenuto”, new WhitespaceAnalyzer());
        Query query = parser.parse("+connettiamo +adsl");
        */
        //Query allDocsQuery = MatchAllDocsQuery.INSTANCE;
        
        
 
        
        // --- QUERY UTENTE ------ 
        Scanner scanner = new Scanner(System.in);
                
        System.out.print("Fai la tua query (con \"titolo\" fai una ricerca nel titolo, con \"contenuto\" nel corpo del file): ");
        String input = scanner.nextLine().trim(); // Legge la riga e rimuove spazi iniziali/finali
                
        if (input.isEmpty()) {
        	System.out.println("La stringa è vuota.");
            scanner.close();
            return;
        }
                
                // Separa in massimo 2 parti usando uno o più spazi come delimitatore
        String[] parti = input.split("\\s+", 2);
                
        String comando = parti[0]; // La prima parola
        String queryTxt = (parti.length > 1) ? parti[1] : ""; // Il resto del testo (se esiste)
        
        if(comando.equals("titolo")) {
            System.out.println("Eseguo " + comando);
        }else {
        	if(comando.equals("contenuto")) {
                System.out.println("Eseguo " + comando);
        	}else {
                System.out.println("Comando \"" + comando+"\" non valido.");
        		return;
        	}
        }
                
        scanner.close();

        // Verifica che la stringa sia lunga almeno 2 caratteri e che inizi e finisca con "\"" (quindi è una phrase query
        if (queryTxt != null && queryTxt.length() >= 2 && queryTxt.startsWith("\"") && queryTxt.endsWith("\"")) {
            // Estrae la sottostringa escludendo il primo e l'ultimo carattere
            String senzaVirgolette = queryTxt.substring(1, queryTxt.length() - 1);
            
            //FAI PHRASE QUERY
        }else {
        	// FAI NORMALE QUERY
        }

        
        
        
        
        
        
        
        
        TermQuery termQuery = new TermQuery(new Term("contenuto", "connettiamo"));

        TopDocs hits = searcher.search(termQuery, 10);
        ScoreDoc scoreDoc = hits.scoreDocs[0];
        StoredFields storedFields = searcher.storedFields();
        Document doc = storedFields.document(scoreDoc.doc);
        System.out.println(doc.get("contenuto"));
        
        // -- QUERY DI PROVA ----------------
        
        
        
        
        
        
        // -- QUERY FATTA DA PDF --------

        //IndexReader reader = DirectoryReader.open(directory); //← obtain read access to the index
        //IndexSearcher searcher = new IndexSearcher(reader); 
        QueryParser parser = new QueryParser("titolo", new WhitespaceAnalyzer());
        Query query = parser.parse("stelle"); //← define the query 
        TopDocs topdocs = searcher.search(query, 10); //← search for the top 10 documents that match the query
        StoredFields storedFields2 = searcher.storedFields();
        for (int i = 0; i < topdocs.scoreDocs.length; i++) { 
        	ScoreDoc scoreDoc2 = topdocs.scoreDocs[i]; 
            Document doc2 = storedFields2.document(scoreDoc2.doc); //← fetch returned document
            System.out.println(doc2.get("titolo"));
        }
        
    }
}

/* DA FARE -
 * 	PRENDE L'INPUT DELL'UTENTE
 * TOGLIE LA PRIMA PAROLA
 * SE ERA "TITOLO" FA RICERCA SUL TITOLO
 * SE ERA "CONTENUTO" FA RICERCA SUL CONTENUTO
 * SE CI SONO LE VIRGOLETTE FA UNA FRASE QUERY
 */







/*	DA FARE - Definire un proprio indexer che ha come campi 
	Path p - dove si trovano gli indici
	Directory d - directory contenente gli indici
	IndexWriter i -
	
	
	
	NEL MAIN:
	1) Crea l'indexer
	2) chiama il metodo indexFiles con la directory dei documenti da indicizzare
	3) fai dare in input la stringa di richiesta
	4) Crea un nuovo QueryInterrogator passandogli la query dell'utente (formattata) e l'indexer
	5) Chiudere con .close() sia l'interrogator che l'indexer
	
	Indexer e QueryInterrogator sono classi da definire
	
	QUERYINTERROGATOR
	Ha come campi
	IndexReader ir 
	IndexSearcher is
	QueryParser qp
	
	
	
	

*/
