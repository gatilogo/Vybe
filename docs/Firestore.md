# How to use Firestore
Steps to connect to Firestore and query the tables:
1. Add the following line of code as an attribute for the class: 
	```java
	FirebaseFirestore db = FirebaseFirestore.getInstance();
	```
	Also need to add import statement for Firestore
	```java
	import com.google.firebase.firestore.FirebaseFirestore;
	```
2.  (Optional) If we are referencing the same collection can add the following:
	```java
	final CollectionReference collectionReference = db.collection(collectionName);
	```
3. Then need to define a Hashmap to be used as the data to be saved/added to the Database
	```java
	HashMap<String, Object> data = new HashMap<>();
	data.put("integer", 123);
	data.put("string", "hello world");
	data.put("date", localdate);
	```
4. See the list of example queires below. Can also add OnSuccess/Failure Listeners
	### Add/Update Query
	```java
	// Add with Document name as randomly-generated GUID
	collectionReference.add(data);
	
	// Add if documentName is not in the collection
	// Otherwise, it updates document equal to documentName
	collectionReference
		.document(documentName)
		.set(data);
	```
	### Delete Query
	```java
	collectionReference
		.document(documentName)
		.delete();
	```
	### Order By Query
	```java
	collectionReference
		.orderBy("date", Query.Direction.ASCENDING);
	```
	### Get Firestore generated ID
	```java
	String id = collectionReference.document().getId();
	data.put("id", id);
	collectionReference.document(id).set(data);
	```
5. Whenever there is an update to the Database, add the following event listener to get the up to date information:
	```java
	// 'this' will have this listener only run when the current activity is alive
	collectionReference.addSnapshotListener(this, new EventListener<QuerySnapshot>() {     
		@Override
		public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {  
		        for (QueryDocumentSnapshot doc : queryDocumentSnapshots){  
		              // doc.getId() is the name/ID of the document
		              // doc.getData().get(field_name)
		              // doc.getDateTime(field_name) if field is Timestamp in Firestore
		        }  
		}  
	});
	```
