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
public class ExampleRegularIndex {
   
   
    private static Map map;
    private static RestGraphDatabase graphDb;
    private static RestIndex<Node> geneidIndex;
    
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
                   
            geneNode = graphDb.createNode();
            if (geneidIndex == null) {
                geneidIndex = graphDb.index().forNodes("geneid");
                
            } 
            geneidIndex.add(geneNode, "geneid", "BRCA1");  
            geneNode.setProperty("geneid", "BRCA1 this is a gene symbol");
               
            
            
            tx.success();  
        } catch (Exception e) {
            System.out.println("exception caught");
            e.printStackTrace();
        } finally {
            // System.out.println("geneNode" + geneNode.toString());
            System.out.println("finally");   
            tx.finish();
            System.out.println("came out of finally");
        }
      
    }
}
