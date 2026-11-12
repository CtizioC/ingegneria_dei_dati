package ing_dei_dati.hw3;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.custom.CustomAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.codecs.simpletext.SimpleTextCodec;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;





public class Indexer {
	private Path path;
	
	public Indexer(String s){
		this.path = Paths.get(s);
	}
	
	
	
	public void index() throws IOException {
    Directory directory = FSDirectory.open(this.path);
    
    
    
   /* //	PROVA ---------------
    Map<String, Analyzer> byField = Map.of(
    	    "titolo", new WhitespaceAnalyzer(),
    	    "contenuto", CustomAnalyzer.builder().withTokenizer("whitespace").addTokenFilter("lowercase").build());
    	Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), byField);
    //
    // se aggiungo la parte qua sopra l'indice viene salvato in modo leggibile da un essere umano
    */
    
    
    
    Map<String, Analyzer> byField = Map.of(
    	    "titolo", new StandardAnalyzer(),
    	    "contenuto", new StandardAnalyzer());
    	Analyzer perFieldAnalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), byField);
    
    
    //Creo l'indexWriter per scrivere l'index
    IndexWriterConfig config = new IndexWriterConfig(/*perFieldAnalyzer*/);
    config.setCodec(new SimpleTextCodec());
    IndexWriter writer = new IndexWriter(directory, config);
    
   //Indicizzazione files
    File files=new File("./DirectoryTXT");
    List<File> listaFile=new ArrayList<>(Arrays.asList(files.listFiles()));
    for(File f:listaFile) {
    	if(f.isFile()) {
    		Document d=new Document();

    		d.add(new TextField("titolo", f.getName(), Field.Store.YES));
    		d.add(new TextField("contenuto", Files.readString(Path.of(f.getAbsolutePath())), Field.Store.YES));
    		
    		writer.addDocument(d);
    		writer.commit();
    	}
    }
    writer.close();
    directory.close();
    
	}
}
