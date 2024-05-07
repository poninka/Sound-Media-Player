//Example method to get an access token from Spotify
public String getAccessToken() {
 String clientId = "your_client_id";  // Replace with your client ID
 String clientSecret = "your_client_secret";  // Replace with your client secret
 String encodedCredentials = Base64.getEncoder().encodeToString((clientId + ":" + client_secret).getBytes());

 try {
     URL url = new URL("https://accounts.spotify.com/api/token");
     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
     connection.setRequestMethod("POST");
     connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
     connection.setDoOutput(true);
     OutputStream os = connection.getOutputStream();
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
     writer.write("grant_type=client_credentials");
     writer.flush();
     writer.close();
     os.close();

     connection.connect();

     // Check for successful response and parse it
     if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
         // parse access token from the response
     }

 } catch (Exception e) {
     e.printStackTrace();
 }
 return null; // Return the access token or handle errors appropriately
}
