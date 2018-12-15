package sample;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class DispatchImpl extends UnicastRemoteObject implements DispatchingInterface {
    public int poortnummer=1099;
    ArrayList<ArrayList<String>> waitingGames2=new ArrayList<>();
    ArrayList<ArrayList<String>> waitingGames3=new ArrayList<>();
    ArrayList<ArrayList<String>> waitingGames4=new ArrayList<>();

    ArrayList<String> occupiedPlayers=new ArrayList<>();

    //Distributed arrays
    ArrayList<Integer>appservers=new ArrayList<>();
    HashMap<Integer, Integer> gamesPerServer=new HashMap();
    HashMap<String, Integer> playerGame=new HashMap<>();

    public  DBServerInterface stub;

    public DispatchImpl (DBServerInterface stub) throws RemoteException{
        this.stub = stub;
        appservers.add(1099);
        appservers.add(1097);
        
        gamesPerServer.put(1099, 0);
        gamesPerServer.put(1097, 0);
    }

    @Override
    public void testConnectie(){
        System.out.println("connectie is er");
    }

    @Override
    public boolean SignIn(String a, String b) throws RemoteException{
        System.out.println("username: "+a+ "ww: "+b);
        return stub.signIn(a,b);
    }
    @Override
    public String LogIn(String a, String b) throws RemoteException {
        System.out.println("username: "+a+ "ww: "+b);
        return stub.logIn(a,b);
    }
    @Override
    public void logOut(String sessionToken) throws RemoteException {
        stub.removeFromOnlinePlayers(sessionToken);
    }
    @Override
    public synchronized int addToGame(String sessionToken, int aantalspelers, boolean host) {
        System.out.println("1token: " + sessionToken);
        if (aantalspelers == 2) {
            if (host || waitingGames2.size() == 0) {
                waitingGames2.add(new ArrayList<>());
                waitingGames2.get(0).add(sessionToken);
                return vindtTegenspeler(sessionToken);
            } else {
                ArrayList<String> tempgame = waitingGames2.get(0);
                tempgame.add(sessionToken);
                System.out.println("size"+tempgame.size());
                if (tempgame.size() == 2) {
                    for (String player:tempgame) {
                        occupiedPlayers.add(player);
                    }
                    try {
                        startGame(tempgame);
                    }catch (RemoteException re){
                        re.printStackTrace();
                        System.out.println(re);
                    }catch (NotBoundException ne){
                        ne.printStackTrace();
                        System.out.println(ne);
                    }
                    System.out.println("3token: " + sessionToken);
                    // String speler1 = waitingPlayers2.get(0);
                    //String speler2 = waitingPlayers2.get(1);
                    //waitingPlayers2.clear();

                    waitingGames2.remove(tempgame);
                    iterateHashmap();
                    return playerGame.get(sessionToken);
                    //Game game = new Game(speler1, speler2);

                    //watchGames.add(tempgame);


                } else {
                    return vindtTegenspeler(sessionToken);
                }
            }
        } else if (aantalspelers == 3) {
            if (host || waitingGames3.size() == 0) {
                waitingGames3.add(new ArrayList<>());
                return vindtTegenspeler(sessionToken);
            } else {
                ArrayList<String> tempgame = waitingGames3.get(0);
                tempgame.add(sessionToken);
                if (tempgame.size() == 3) {
                    for (String player:tempgame) {
                        occupiedPlayers.add(player);
                    }
                    try{
                    startGame(tempgame);
                    }catch (RemoteException re){

                    }catch (NotBoundException ne){

                    }
                    notifyAll();
                    System.out.println("3token: " + sessionToken);
                    // String speler1 = waitingPlayers2.get(0);
                    //String speler2 = waitingPlayers2.get(1);
                    //waitingPlayers2.clear();

                    waitingGames3.remove(tempgame);
                    return playerGame.get(sessionToken);
                    //Game game = new Game(speler1, speler2);

                    //watchGames.add(tempgame);


                    } else {
                        return vindtTegenspeler(sessionToken);
                    }
                }
            } else {
                if (host || waitingGames4.size() == 0) {
                   waitingGames4.add(new ArrayList<>());
                   return vindtTegenspeler(sessionToken);
                } else {
                  ArrayList<String> tempgame = waitingGames4.get(0);
                  tempgame.add(sessionToken);
                 if (tempgame.size() == 4) {
                     for (String player:tempgame) {
                         occupiedPlayers.add(player);
                     }try {

                         startGame(tempgame);

                     }catch (RemoteException re){
                        re.printStackTrace();
                         System.out.println(re);
                     }catch (NotBoundException ne){
                        ne.printStackTrace();
                         System.out.println(ne);
                     }

                     System.out.println("3token: " + sessionToken);
                    // String speler1 = waitingPlayers2.get(0);
                    //String speler2 = waitingPlayers2.get(1);
                    //waitingPlayers2.clear();
                     waitingGames4.remove(tempgame);
                     return playerGame.get(sessionToken);

                    //Game game = new Game(speler1, speler2);

                    //watchGames.add(tempgame);

                }else {
                    return vindtTegenspeler(sessionToken);

                }
              }
            }

        }
    @Override
    public synchronized int vindtTegenspeler(String sessionToken){
        try {
            while (!occupiedPlayers.contains(sessionToken)) {
                System.out.println("waitings");
                wait();
            }
            System.out.println("dobby is free");
            return playerGame.get(sessionToken);
        }
        catch (Exception e){
            e.printStackTrace();
            System.out.println(e);
        }
        return -2;
    }
    public void startGame(ArrayList<String> game) throws RemoteException, NotBoundException {
        int poortnr=findLeastOccupiedServer();

        startGameOnServer(poortnr, game);
        System.out.println("hihi");
        for (String player:game) {
            System.out.println("poornr hier= "+poortnr);
            playerGame.put(player, poortnr);
        }
        notifyAll();

    }
    public void startGameOnServer(int poortnummer, ArrayList<String> game) throws RemoteException, NotBoundException{
        System.out.println("poortnummer"+poortnummer);
        Registry myRegistry = LocateRegistry.getRegistry("localhost", poortnummer);
// search for CounterService
        Counter impl= (Counter) myRegistry.lookup("Login");
        impl.startGame(game);
    }
    public int findLeastOccupiedServer() {
        int least = -1;
        int leastKey=0;
        for (HashMap.Entry<Integer, Integer> entry : gamesPerServer.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if (least == -1 || value < least) {
                least = value;
                leastKey = key;
            }
        }
        System.out.println("laatste key: "+poortnummer);
        return leastKey;
    }
    public void iterateHashmap(){
        for (HashMap.Entry<String, Integer> entry : playerGame.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            System.out.println("key: "+key + " value: "+value);
        }
    }


}
