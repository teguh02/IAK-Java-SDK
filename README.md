# IAK Java SDK
IAK / Mobilepulsa API V2 Java SDK

Created by Teguh Rijanandi <teguhrijanandi02@gmail.com> for everyone 

# Installation
1. Download latest version from <a href="https://github.com/teguh02/IAK-Java-SDK/releases">here</a>
2. Unzip and move IAK.java into your project folder like this
    ![image](https://user-images.githubusercontent.com/43981051/128820505-52873ddf-f23d-426d-bb34-f2edfc5a2070.png)
3. Import to your main class and follow from sample code below

# Sample Code
```java
public class main {

    public static void main(String[] args) {

        /**
         * Here is a sample for usage IAK Java SDK
         * Just uncomment one by one System.out.println code below to see the result
         *
         * Contributor : Teguh Rijanandi <teguhrijanandi02@gmail.com>
         */

        // ---------------------------------------------------------------------------------------------------------- //

        IAK iak = new IAK();

        // Set Apikey, noHp (username), and stage environment here
        iak.setApikey("1195eaba63dc90ee");
        iak.setNohp("089655541804");
        iak.setStage("sandbox");

        // ---------------------------------------------------------------------------------------------------------- //

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

        // 7. Check prepaid transactions
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjU0-check-status
        //System.out.println(iak.prepaid().check_status("orderNum31"));

        // ---------------------------------------------------------------------------------------------------------- //

        // B. Postpaid

        // 1. Pricelist
        // https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjQy-pricelist
        //System.out.println(iak.postpaid().pricelist_postpaid("pdam", "active", null));
        //System.out.println(iak.postpaid().pricelist_postpaid("pdam", "active", "Jakarta"));
        //System.out.println(iak.postpaid().pricelist_postpaid("pdam", "active", "Jawa Tengah"));

        // 2. Inquiry Postpaid product
        // If you want to inquiry BPJS product please see this code
        // Documentation : https://api.iak.id/docs/reference/ZG9jOjEyNjIwNjU2-inquiry
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order01", "8801234560001", "BPJS", "2"));

        // If you not to inquiry BPJS product please see this code
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order02", "0110014601", "PGAS", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order03", "6391601201", "FNMEGA", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order04", "10202001", "PDAMKOTA.SURABAYA", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order05", "530000000001", "PLNPOSTPAID", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order06", "127246500101", "TVTLKMV", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order07", "1072161401", "TVBIG", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order08", "08991234501", "HPTHREE", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order09", "6391601201", "TELKOMPSTN", null));
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order10", "01927101", "CBN", null));

        // If you want to inquiry E Samsat product, you must use code like this
        // where '0212502110170100' is nomor_identitas (Registered identity number / KTP)
        //System.out.println(iak.postpaid().inquiry_postpaid_esamsat("postpaid_order_esamsat", "9658548523568701", "ESAMSAT.JABAR", "0212502110170100"));

        // Then if you want to inquiry PBB product, you can use previous code (not E samsat inquiry code)
        // where '329801092375999901' is tax object number
        //System.out.println(iak.postpaid().inquiry_postpaid("postpaid_order_pbb", "329801092375999901", "PBBKOT.CIMAHI", null));

        // To pay inquiry pospaid product you can see code below
        // where '9934079' is from inquiry response
        //System.out.println(iak.postpaid().pay_postpaid_inquiry("9934079"));

        // Check prepaid inquiry payment
        //System.out.println(iak.postpaid().check_status_postpaid_payment("postpaid_order_pbb"));
        //System.out.println(iak.postpaid().check_status_postpaid_payment("postpaid_order_esamsat"));
        //System.out.println(iak.postpaid().check_status_postpaid_payment("postpaid_order05"));
    }
}

```
