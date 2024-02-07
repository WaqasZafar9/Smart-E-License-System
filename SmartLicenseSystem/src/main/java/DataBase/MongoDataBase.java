package DataBase;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.Binary;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MongoDataBase {
    String uri;
    MongoDatabase database;
    MongoCollection<Document> collection;
    public MongoDataBase(String databaseName, String collectionName){
        // TODO: 04/02/2024 Enter URL of Your MongoDB Cluster or LocalHost
        uri = "mongodb://localhost:27017";


        try {
            MongoClient mongoClient = MongoClients.create(uri);
            database = mongoClient.getDatabase(databaseName);
            collection = database.getCollection(collectionName);


        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,"Database Connectivity Error");
            System.out.println(e);
        }

    }

    public boolean createDocument( Map<String, Object> documentMap){
        try {
            Document newPerson = new Document(documentMap);
            collection.insertOne(newPerson);
            return true;
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
            System.out.println(e.getStackTrace());
            return false;
        }
    }

    public boolean updateDocument(String fieldName,String prevData,String updateField,String UpdateFieldData){
        try {
        collection.updateOne(eq("Ide", prevData), set(updateField,UpdateFieldData ));
        System.out.println("Document updated.");
            return true;
        }
        catch (Exception e){
            System.out.println("Document updated.");
            return false;
        }

    }
    public boolean updateDriverDocument(String fieldName,String prevData,String issue,String issueDate,String expiry,String expiryDate,String learner,String learnerNo){
        try {
            collection.updateOne(eq(fieldName, prevData), combine(set(issue, issueDate), set(expiry, expiryDate), set(learner, learnerNo)));

            System.out.println("Document updated.");
            return true;
        }
        catch (Exception e){
            System.out.println("Document updated.");
            return false;
        }

    }

    public boolean updateLicenseDocument(String fieldName,String prevData,String issue,String issueDate,String expiry,String expiryDate,String learner,int LicenseNo){
        try {
            collection.updateOne(eq(fieldName, prevData), combine(set(issue, issueDate), set(expiry, expiryDate), set(learner, LicenseNo)));

            System.out.println("Document updated.");
            return true;
        }
        catch (Exception e){
            System.out.println("Document updated.");
            return false;
        }

    }
    public int updateId(String recordName,boolean isUpdate){
        Document documentToUpdate = collection.find().first();

            int currentRollNo = documentToUpdate.getInteger(recordName);

            if (isUpdate) {
                int newRollNo = currentRollNo + 1;
                collection.updateOne(eq(recordName, currentRollNo),
                        Updates.set(recordName, newRollNo));
            }

            System.out.println("Learner updated successfully." );
            return currentRollNo;

    }

    public boolean updateSymbol(Map<String, Object> documentMap,boolean isImage) {
        try {
            Document filter = new Document("questionID", documentMap.get("questionID"));
            Document update;
            if(isImage){
            update = new Document("$set", new Document()
                    .append("Question", documentMap.get("Question"))
                    .append("Option1", documentMap.get("Option1"))
                    .append("Option2", documentMap.get("Option2"))
                    .append("Option3", documentMap.get("Option3"))
                    .append("Option4", documentMap.get("Option4"))
                    .append("Symbol", documentMap.get("Symbol"))
                    .append("Correct", documentMap.get("Correct"))
            );
            }
            else {
                update = new Document("$set", new Document()
                        .append("Option1", documentMap.get("Option1"))
                        .append("Option2", documentMap.get("Option2"))
                        .append("Option3", documentMap.get("Option3"))
                        .append("Option4", documentMap.get("Option4"))
                        .append("Correct", documentMap.get("Correct"))
                );
            }

            collection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUser(Map<String, Object> documentMap,boolean isImage,String fieldName,String mappingName) {
        try {

            Document filter = new Document(fieldName, documentMap.get(mappingName));

            Document s1=new Document();
            for(Map.Entry<String, Object> entry : documentMap.entrySet()){
                s1.append(entry.getKey(),entry.getValue());
            }
            Document update = new Document("$set", s1);

            collection.updateOne(filter, update);
            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,e);
            e.printStackTrace();
            return false;
        }
    }



    public boolean deleteDocument(String fieldName, int id){
        try {
            collection.deleteOne(eq(fieldName, id));
            System.out.println("Document deleted.");
            return true;
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }

    }

    public boolean deleteDocument(String fieldName, String id){
        try {
            collection.deleteOne(eq(fieldName, id));
            System.out.println("Document deleted.");
            return true;
        }
        catch (Exception e){
            System.out.println(e.getStackTrace());
            return false;
        }

    }
    public Document readDocument(String fieldName,String ide){
        Document readDoc = collection.find(eq(fieldName, ide)).first();
        if (readDoc != null) {

            return readDoc;
        }
        else {
            System.out.println("No matching documents found.");
        }
        return null;

    }

    public Document readLicenseDocument(String fieldName,String ide,String type,String typeDetail){
        Document readDoc = collection.find(
                Filters.and(
                        Filters.eq(fieldName, ide),
                        Filters.eq(type, typeDetail)
                )
        ).first();
        if (readDoc != null) {
            JOptionPane.showMessageDialog(null,"Document found: " + readDoc.toJson());

            return readDoc;
        }
        else {
            JOptionPane.showMessageDialog(null,"Document Not found: " + readDoc.toJson());

            System.out.println("No matching documents found.");
        }
        return null;

    }

    public Document[] fetchFirst10Documents() {
        FindIterable<Document> result = collection.find().limit(10);
        List<Document> documentList = new ArrayList<>();
        result.into(documentList);
        return documentList.toArray(new Document[0]);
    }

    public ArrayList<Document> fetchAllDocuments() {
        FindIterable<Document> result = collection.find();
        ArrayList<Document> documentList = new ArrayList<>();
        return result.into(documentList);

    }

    public static byte[] storeImage(String imageP) throws IOException {
        // Step 1: Convert image to binary data

        Path imagePath = Paths.get(imageP);

        return Files.readAllBytes(imagePath);

    }

    public static byte[] fetchImage(Binary retrievedImageBinary){

        if (retrievedImageBinary != null) {

                byte[] retrievedImageBytes = retrievedImageBinary.getData();
                JFrame frame = new JFrame();
                frame.setTitle(" Added Title");
                JPanel j = new JPanel();
                JLabel l1 = new JLabel("");
                ImageIcon imageIcon = new ImageIcon(retrievedImageBytes);
                l1.setIcon(imageIcon);

                return retrievedImageBytes;
            }
            else {
                System.out.println("Image data is null. Unable to display the image.");
                return null;
            }

    }

    public Document searchDocument(String fieldname,int value){
        return collection.find(eq(fieldname, value)).first();

    }
    public Document searchDocument(String fieldname,String value){
        return collection.find(eq(fieldname, value)).first();

    }


    public static void main(String[] args) {



        new MongoDataBase("Driving_Center","testing");


    }

    public boolean updateImage(String id,String value,String toUpdate,String path) {
        try {
            collection.updateOne(eq(id, value), set(toUpdate, storeImage(path)));
            System.out.println("Document updated.");
            return true;
        }
        catch (Exception e){
            System.out.println("Document updated.");
            return false;
        }

    }
}
