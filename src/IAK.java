import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class IAK {

    /**
     * IAK / Mobilepulsa Java SDK
     */

    // ---------------------------------------------------------------------------------------------------------- //

    protected String apikey;
    protected String nohp;
    protected String stage = "sandbox";

    protected String BASEURL_PRODUCTION = "https://prepaid.iak.id";
    protected String BASEURL_DEVELOPMENT ="https://prepaid.iak.dev";

    protected Boolean prepaid_access = false;
    protected Boolean postpaid_access = false;

    // ---------------------------------------------------------------------------------------------------------- //

    // Getter and setter
    protected String getApikey() { return apikey; }
    public void setApikey(String apikey) { this.apikey = apikey; }
    protected String getNohp() { return nohp; }
    public void setNohp(String nohp) { this.nohp = nohp; }
    protected String getStage() { return stage; }
    public void setStage(String stage) { this.stage = stage; }
    public String getBASEURL() { return getStage() == "sandbox" ? BASEURL_DEVELOPMENT : BASEURL_PRODUCTION; }
    public String sign(String action) {
        String input = this.getNohp() + this.getApikey() + action;
        try {
            // Static getInstance method is called with hashing MD5
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest
            //  of an input digest() return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
    protected String HTTP_POST(String request_url, String body) {
        String baseurl;
        StringBuilder response = new StringBuilder();

        try {

            if (this.getStage() == "sandbox") {
                baseurl = BASEURL_DEVELOPMENT + request_url;
            } else {
                baseurl = BASEURL_PRODUCTION + request_url;
            }

            URL url = new URL (baseurl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String jsonInputString = body;
            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                //return response.toString();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    // ---------------------------------------------------------------------------------------------------------- //

    /**
     * Prepaid Area
     */

    public IAK prepaid() {
        this.prepaid_access = true;
        return this;
    }

    /**
     * Check balance
     * @return
     */
    public Object checkBalance() {
        if(!this.prepaid_access) {
            try {
                throw new Exception("Please call prepaid method first!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String body = "{\"username\": \"" + this.getNohp() + "\", \"sign\": \"" + this.sign("bl") + "\"}";
        return this.HTTP_POST("/api/check-balance", body);
    }

    // ---------------------------------------------------------------------------------------------------------- //

    /**
     * Postpaid area
     */

    public IAK postpaid() {
        this.postpaid_access = true;
        return this;
    }
}
