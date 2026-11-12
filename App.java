package ing_dei_dati.hw3;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
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
    	String directoryIndici="./DirectoryIndici";
    	Indexer indexer=new Indexer(directoryIndici);
    	indexer.index();
    	
        Path path = Paths.get(directoryIndici);
        Directory directory = FSDirectory.open(path);
        
        IndexReader reader = DirectoryReader.open(directory);
        IndexSearcher searcher = new IndexSearcher(reader);
 
        
        // --- QUERY UTENTE ------ 
        Scanner scanner = new Scanner(System.in);
                
        System.out.print("Fai la tua query (con \"titolo\" fai una ricerca nel titolo, con \"contenuto\" nel corpo del file): ");
        String input = scanner.nextLine().trim(); // Legge la riga e rimuove spazi iniziali/finali
                
        if (input.isEmpty()) {
        	System.out.println("La stringa è vuota.");
            scanner.close();
            return;
        }
                
                // Separa in 2 parti la query utente
        String[] parti = input.split("\\s+", 2);
        if(parti.length<2) {
        	System.out.println("Query non valida");
        	scanner.close();
        	return;
        } 
        String comando = parti[0]; 
        String queryTxt = (parti.length > 1) ? parti[1] : ""; // Il resto del testo (se esiste)

        
        
        
        if(comando.equals("titolo")||comando.equals("contenuto")) {
            //System.out.println("Eseguo " + comando);
        }else {
        	System.out.println("Comando \"" + comando+"\" non valido.");
        	scanner.close();
        	return;
        }
        
        
                
        scanner.close();
        
//finito di leggere la query dell'utente e interpretarla
        
        

        // Verifica se è una phrase query (quindi che la stringa sia lunga almeno 2 caratteri e che inizi e finisca con " )
        if (queryTxt != null && queryTxt.length() >= 2 && queryTxt.startsWith("\"") && queryTxt.endsWith("\"")) {
            // Estrae la sottostringa escludendo il primo e l'ultimo carattere
            String senzaVirgolette = queryTxt.substring(1, queryTxt.length() - 1);
            
            //FAI PHRASE QUERY
            PhraseQuery phQuery = new PhraseQuery(0, comando, queryTxt);

            
            //cerca
            TopDocs hits = searcher.search(phQuery, 10);
            ScoreDoc scoreDoc = hits.scoreDocs[0];
            StoredFields storedFields = searcher.storedFields();
            Document doc = storedFields.document(scoreDoc.doc);
            System.out.println(doc.get(comando));
            
            
        }else {
        	
        	//faccio termquery
            QueryParser queryParser = new QueryParser(comando, new StandardAnalyzer());
            Query query = queryParser.parse(queryTxt);
            System.out.println(query);

            TopDocs hits = searcher.search(query, 10);
            ScoreDoc scoreDoc = hits.scoreDocs[0];
            StoredFields storedFields = searcher.storedFields();
            Document doc = storedFields.document(scoreDoc.doc);
            System.out.println(doc.get(comando));
            
        }
        
        
    }
}



// GESTIRE LE ECCEZIONI IN CASO IN CUI LA RICERCA NON DIA RISULTATI
//	CONTROLLARE PERCHè LA PHRASE NON FUNIONA