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

    protected String BASEURL_PRODUCTION_POSTPAID = "https://mobilepulsa.net";
    protected String BASEURL_DEVELOPMENT_POSTPAID ="https://testpostpaid.mobilepulsa.net";

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

    // ---------------------------------------------------------------------------------------------------------- //

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

    // ---------------------------------------------------------------------------------------------------------- //

    protected Object HTTP_POST(String request_url, String body) {
        String baseurl;
        StringBuilder response = new StringBuilder();
        try {
            String jsonInputString = body;
            if (this.getStage() == "sandbox") { baseurl = BASEURL_DEVELOPMENT + request_url; } else { baseurl = BASEURL_PRODUCTION + request_url; }
            URL url = new URL (baseurl);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setRequestProperty("Content-Type", "application/json; utf-8");
            con.setRequestProperty("Accept", "application/json");
            con.setDoInput(true);
            con.setDoOutput(true);
            try(OutputStream os = con.getOutputStream()) { byte[] input = jsonInputString.getBytes("utf-8");os.write(input, 0, input.length); }
            try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"))) { String responseLine = null;while ((responseLine = br.readLine()) != null) { response.append(responseLine.trim()); } }
            con.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }
    protected Object HTTP_POST_POSTPAID(String request_url, String body) {
        String baseurl;
        StringBuilder response = new StringBuilder();
        if (this.getStage() == "sandbox") { baseurl = BASEURL_DEVELOPMENT_POSTPAID + request_url; } else { baseurl = BASEURL_PRODUCTION_POSTPAID + request_url;}
        try {
            String input = body;
            String output;
            URL url = new URL(baseurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(input.getBytes());
            os.flush();
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            while ((output = br.readLine()) != null) { response.append(output.trim()); }
            conn.disconnect();
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

    /**
     * Check prepaid transaction
     * @param ref_id
     * @return
     */
    public Object check_status(String ref_id) {
        if(!this.prepaid_access) { try { throw new Exception("Please call prepaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = "{\"username\": \"" + this.getNohp() + "\", \"ref_id\": \"" + ref_id + "\", \"sign\": \"" + this.sign(ref_id) + "\"}";
        return this.HTTP_POST("/api/check-status", body);
    }

    // ---------------------------------------------------------------------------------------------------------- //

    /**
     * Postpaid area
     */

    public IAK postpaid() {
        this.postpaid_access = true;
        return this;
    }

    /**
     * Pricelist postpaid
     * @param type
     * @param status
     * @return
     */
    public Object pricelist_postpaid(String type, String status, String province_for_pdam) {
        if(!this.postpaid_access) { try { throw new Exception("Please call postpaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = null;

        if(province_for_pdam != null) {
            body = "{\"username\": \"" + this.getNohp() + "\", \"commands\": \"pricelist-pasca\", \"status\": \"" + status + "\", \"province\": \"" + province_for_pdam + "\", \"sign\": \"" + this.sign("pl") + "\"}";
        } else {
            body = "{\"commands\": \"pricelist-pasca\", \"username\": \"" + this.getNohp() + "\", \"status\": \"" + status + "\", \"sign\": \"" + this.sign("pl") + "\"}";
        }

        String url = "/api/v1/bill/check/" + type;
        return this.HTTP_POST_POSTPAID(url, body);
    }

    /**
     * Inquiry postpaid product
     * @return
     */
    public Object inquiry_postpaid(String ref_id, String customer_id, String product_code, String month_for_bpjs_product) {
        if(!this.postpaid_access) { try { throw new Exception("Please call postpaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = null;
        String url = "/api/v1/bill/check";

        if (month_for_bpjs_product == null) {
            // Inquiry selain produk BPJS
            body = "{\"commands\": \"inq-pasca\", \"username\": \"" + this.getNohp() + "\", \"code\": \"" + product_code + "\", \"ref_id\": \"" + ref_id + "\", \"hp\": \"" + customer_id + "\", \"sign\": \"" + this.sign(ref_id) + "\"}";

        } else {
            // Inquiry produk BPJS
            body = "{\"commands\": \"inq-pasca\", \"username\": \"" + this.getNohp() + "\", \"code\": \"" + product_code + "\", \"ref_id\": \"" + ref_id + "\", \"hp\": \"" + customer_id + "\", \"sign\": \"" + this.sign(ref_id) + "\", \"month\": \"" + month_for_bpjs_product + "\"}";
        }

        return this.HTTP_POST_POSTPAID(url, body);
    }

    /**
     * Inquiry E-samsat product
     * @param ref_id
     * @param customer_id
     * @param product_code
     * @return
     */
    public Object inquiry_postpaid_esamsat(String ref_id, String customer_id, String product_code, String nomor_identitas) {
        if(!this.postpaid_access) { try { throw new Exception("Please call postpaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = null;
        String url = "/api/v1/bill/check";
        body = "{\"commands\": \"inq-pasca\", \"username\": \"" + this.getNohp() + "\", \"code\": \"" + product_code + "\", \"ref_id\": \"" + ref_id + "\", \"hp\": \"" + customer_id + "\", \"sign\": \"" + this.sign(ref_id) + "\", \"nomor_identitas\": \"" + nomor_identitas + "\"}";
        return this.HTTP_POST_POSTPAID(url, body);
    }

    /**
     * To pay inquiry postpaid product
     * @param tr_id_from_inquiry
     * @return
     */
    public Object pay_postpaid_inquiry(String tr_id_from_inquiry) {
        if(!this.postpaid_access) { try { throw new Exception("Please call postpaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = null;
        String url = "/api/v1/bill/check";
        body = "{\"commands\": \"pay-pasca\", \"username\": \"" + this.getNohp() + "\", \"tr_id\": \"" + tr_id_from_inquiry + "\", \"sign\": \"" + this.sign(tr_id_from_inquiry) + "\"}";
        return this.HTTP_POST_POSTPAID(url, body);
    }

    /**
     * To check payment status
     * @param ref_id
     * @return
     */
    public Object check_status_postpaid_payment(String ref_id) {
        if(!this.postpaid_access) { try { throw new Exception("Please call postpaid method first!"); } catch (Exception e) { e.printStackTrace(); } }
        String body = null;
        String url = "/api/v1/bill/check";
        body = "{\"commands\": \"checkstatus\", \"username\": \"" + this.getNohp() + "\", \"ref_id\": \"" + ref_id + "\", \"sign\": \"" + this.sign("cs") + "\"}";
        return this.HTTP_POST_POSTPAID(url, body);
    }
}
