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
     * Bassed on this documentation
     * https://api.iak.id/docs/reference/ZG9jOjE-introduction
     *
     * Contributor : Teguh Rijanandi <teguhrijanandi02@gmail.com>
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
     * Prepaid Check balance
     * @return
     */
    public Object checkBalance() {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\": \"" + this.getNohp() + "\", \"sign\": \"" + this.sign("bl") + "\"}";
        return this.HTTP_POST("/api/check-balance", body);
    }

    /**
     * Prepaid pricelist
     * @return
     */
    public Object pricelist(String type, String operator, String status) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\" : \"" + this.getNohp() + "\", \"sign\": \"" + this.sign("pl") + "\", \"status\": \"" + status + "\"}";
        String url = "/api/pricelist/" + type + "/" + operator;
        return this.HTTP_POST(url, body);
    }

    /**
     * Inquiry PLN
     * @return
     */
    public Object inquiry_pln(String customer_id) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\": \"" + this.getNohp() + "\", \"customer_id\": \"" + customer_id + "\", \"sign\": \"" + this.sign(customer_id) + "\"}";
        return this.HTTP_POST("/api/inquiry-pln", body);
    }

    /**
     * Inquiry game id
     * @param customer_id
     * @return
     */
    public Object inquiry_gameId(String game_code, String customer_id) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\": \"" + this.getNohp() + "\", \"game_code\": \"" + game_code + "\", \"customer_id\": \"" + customer_id + "\", \"sign\": \"" + this.sign(game_code) + "\"}";
        return this.HTTP_POST("/api/inquiry-game", body);
    }

    /**
     * Inquiry game server
     * @param game_code
     * @return
     */
    public Object inquiry_gameServer(String game_code) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\": \"" + this.getNohp() + "\", \"game_code\": \"" + game_code + "\", \"sign\": \"" + this.sign(game_code) + "\"}";
        return this.HTTP_POST("/api/inquiry-game-server", body);
    }

    /**
     * Top up prepaid product
     * @param ref_id
     * @param customer_id
     * @param product_code
     * @return
     */
    public Object topup(String ref_id, String customer_id, String product_code) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"commands\": \"topup\", \"username\": \"" + this.getNohp() + "\", \"ref_id\": \"" + ref_id + "\", \"hp\": \"" + customer_id + "\", \"pulsa_code\": \"" + product_code + "\", \"sign\": \"" + this.sign(ref_id) + "\"}";
        // Pakai API v1 karena saya mencoba pada tanggal 10/08/2021 13:30 gagal, errornya hp required pulsa_code required
        return this.HTTP_POST("/v1/legacy/index", body);
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
