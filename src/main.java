public class main {

    public static void main(String[] args) {
        /**
         * Here is a sample for usage IAK Java SDK
         * Just uncomment one by one System.out.println code below to see the result
         *
         * Contributor : Teguh Rijanandi <teguhrijanandi02@gmail.com>
         */

        IAK iak = new IAK();

        // Set Apikey, noHp (username), and stage environment here
        iak.setApikey("1195eaba63dc90ee");
        iak.setNohp("089655541804");
        iak.setStage("sandbox");

        // A. Prepaid

        // 1. Check Balance
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjUz-check-balance
        //System.out.println(iak.prepaid().checkBalance());

        // 2. Pricelist
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjU1-pricelist
        //System.out.println(iak.prepaid().pricelist("pulsa", "three", "all"));

        // 3. Inquiry PLN
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjc5-inquiry-pln
        //System.out.println(iak.prepaid().inquiry_pln("12345678901"));

        // 4. Inquiry Game ID
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjc3-inquiry-game-id
        //System.out.println(iak.prepaid().inquiry_gameId("103", "156378300|8483"));

        // 5. Inquiry Game Server
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjc4-inquiry-game-server
        //System.out.println(iak.prepaid().inquiry_gameServer("142"));

        // 6. Topup prepaid product
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjc2-top-up
        //System.out.println(iak.prepaid().topup("orderNum31", "0896512435", "hthree1000"));


    }
}
