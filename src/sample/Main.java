package sample;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {
    public void start(DBServerInterface stub) throws Exception {

            Registry registry = LocateRegistry.createRegistry(1100);

// create a new service named CounterService

            registry.rebind("disp", new DispatchImpl(stub));

    }
    public static void main(String[] args){
        Main main=new Main();
        try {
            DBServerInterface stub=main.connectToDatabase();
            main.start(stub);
        }catch (Exception e){

        }
    }
    private DBServerInterface connectToDatabase() {
        DBServerInterface stub = null;
        try {
            // fire to localhost port 1099
            Registry myRegistry = LocateRegistry.getRegistry("localhost", 1098);

            // search for CounterService
            stub = (DBServerInterface) myRegistry.lookup("DBService");

            // call server's method

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("connected with database server");
        return stub;
    }
}
