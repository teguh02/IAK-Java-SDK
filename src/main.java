public class main {

    public static void main(String[] args) {
        IAK iak = new IAK();
        iak.setApikey("1195eaba63dc90ee");
        iak.setNohp("089655541804");
        iak.setStage("sandbox");

        //System.out.println(iak.prepaid().checkBalance());
        //System.out.println(iak.prepaid().pricelist("pulsa", "three", "all"));
        //System.out.println(iak.prepaid().inquiry_pln("12345678901"));
        //System.out.println(iak.prepaid().inquiry_gameId("103", "156378300|8483"));
        System.out.println(iak.prepaid().inquiry_gameServer("142"));
    }
}
