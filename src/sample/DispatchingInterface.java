package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatchingInterface extends Remote{

    void testConnectie() throws RemoteException;
    boolean SignIn(String a, String b) throws RemoteException;
    String LogIn(String a, String b) throws RemoteException;
    void logOut(String sessionToken) throws RemoteException;
    int addToGame(String sessionToken, int aantalspelers, boolean host) throws RemoteException;
    int vindtTegenspeler(String sessionToken) throws RemoteException;

}
