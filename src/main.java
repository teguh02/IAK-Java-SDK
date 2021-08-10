public class main {

    public static void main(String[] args) {
        IAK iak = new IAK();
        iak.setApikey("1195eaba63dc90ee");
        iak.setNohp("089655541804");
        iak.setStage("sandbox");

        System.out.println(iak.prepaid().checkBalance());
    }
}
