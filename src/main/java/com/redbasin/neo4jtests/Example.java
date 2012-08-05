/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.redbasin.neo4jtests;

/*
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.redbasin.bio.BioEntityTypes;
import com.redbasin.bio.NCIDiseaseUtil;
import com.redbasin.neo4j.NeoUtil;
*/

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
import org.neo4j.rest.graphdb.entity.RestNode;

/**
 *
 * @author redbasin
 */
public class Example {
   
   
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
      
        RestNode geneNode = null;
        Transaction tx = null;
        try {
            setup();
            tx = graphDb.beginTx();
                   
            geneNode = (RestNode)graphDb.createNode();
            geneNode.setProperty("geneid", "TNIP2 this is a gene symbol");
            
                // search
            IndexManager index = graphDb.index();
            Index<Node> geneFullText = index.forNodes("genefull", MapUtil.stringMap(IndexManager.PROVIDER, "lucene", "type", "fulltext "));
            geneFullText.add(geneNode, "geneid", "TNIP2 this is a gene symbol");
                
            tx.success();  
        } catch (Exception e) {
            System.out.println("exception caught");
            e.printStackTrace();
        } finally {
            // we though id was a long, but it prints below: geneNode id: {1}
            System.out.println("geneNode id: " + geneNode.toString());
            for (String propName : geneNode.getPropertyKeys()) {
                System.out.println("propName = " + propName); // nothing printed here
            }
            // the below line throws the following error:
            // Exception in thread "main" org.neo4j.graphdb.NotFoundException: 'geneid' on {1}
            System.out.println("geneNode property = " + geneNode.getProperty("geneid").toString());
            System.out.println("finally");   
            tx.finish();
            System.out.println("came out of finally");
        }

    }
}
