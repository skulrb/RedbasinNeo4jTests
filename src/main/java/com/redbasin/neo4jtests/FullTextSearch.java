/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redbasin.neo4jtests;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;

/**
 *
 * @author redbasin
 */
public class FullTextSearch {
   
   
    private static Map map;
    private static RestGraphDatabase graphDb;
    
    private static void registerShutdownHook( RestGraphDatabase graphDb1) {
    // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread() {
        @Override
            public void run() {
                graphDb.shutdown();
            }
        } );
    }
    
    
    private static void setup() throws URISyntaxException {
        graphDb = new RestGraphDatabase("http://localhost:7474/db/data");
        registerShutdownHook(graphDb);
    }    
   
    
    public static void main(String[] args) throws java.io.IOException {
      
        Node geneNode = null;
        Transaction tx = null;
        try {
            setup();
            tx = graphDb.beginTx();
                   
            //geneNode = graphDb.createNode();
                // search
            IndexManager index = graphDb.index();
            Index<Node> geneFullText = index.forNodes("genefull", MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext "));
             
            int num = geneFullText.query("geneid", "gene").size();
            System.out.println("num = " + num);
            
            //System.out.println(geneFullText.query("geneid", "gene").toString());
            
           /* Node geneFound = geneFullText.query("geneid", "gene").getSingle();
            * if (geneFound != null)
               System.out.println("geneFound through fullText" + geneFound.toString());  
               * 
               */
                
                tx.success();  
            } catch (Exception e) {
                System.out.println("exception caught" + e.toString());
                e.printStackTrace();
            } finally {
                System.out.println("finally");   
                tx.finish();
                System.out.println("came out of finally");
            }
      
    }
}
