package sample;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface Counter extends Remote {
    boolean SignIn(String a, String b) throws RemoteException;
    String LogIn(String a, String b) throws RemoteException;
    void logOut(String sessionToken) throws RemoteException;
    String geefWW(String a) throws  RemoteException;
    int[]getAndereGok(String sessionToken) throws  RemoteException;
    int[]getGameGok(int i, int viewerId) throws RemoteException;
    void testConnectie() throws  RemoteException;
    boolean vindtTegenspeler(String sessionToken) throws RemoteException;
    void startGame(ArrayList<String> players) throws RemoteException;
    boolean setGame(String sessionToken) throws RemoteException;
    int getZet(int i, String sessionToken) throws RemoteException;
    void changeBeurt(String sessionToken) throws RemoteException;
    boolean checkBeurt(String sessionToken) throws RemoteException;
    boolean getResult(String sessionToken) throws  RemoteException;
    int getScore(String sessionToken) throws RemoteException;
    int getGame(int i) throws RemoteException;
    ArrayList<ArrayList<Integer>>getReedsGezet(int i) throws RemoteException;
    boolean getEnd(int game) throws  RemoteException;
    int getViewerId(int game) throws  RemoteException;
    void geefNotify(String sessionToken) throws RemoteException;

}