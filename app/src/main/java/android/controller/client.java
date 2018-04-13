package android.controller;

public class client extends Thread {
    MainActivity Main;
    public client(MainActivity main){
        Main = main;

    }
    @Override
    public void run(){
        Main.init();
        while (true){
            super.run();
            if (Main.isRefreshed()){
                Main.infoTrade();
            }

        }

    }
}
