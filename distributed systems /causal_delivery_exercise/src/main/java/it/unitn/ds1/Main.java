package it.unitn.ds1;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String[] args) {
        System.out.println("Hello world!");
        Wait wait = new Wait();

        final ActorSystem system = ActorSystem.create("test");

        List<ActorRef> group = new ArrayList<>();
        int id = 0;

        group.add(system.actorOf(Node.props(id), "node" + id));
        group.get(0).tell(new Node.JoinSys(group.get(0),id), ActorRef.noSender());
        id++;

        wait.wait(2000);

        group.add(system.actorOf(Node.props(id), "node" + id));
        group.get(0).tell(new Node.JoinSys(group.get(1),id), ActorRef.noSender()); // get(1) to keep it in the test list
        id++;

        wait.wait(2000);

        group.add(system.actorOf(Node.props(id), "node" + id));
        group.get(1).tell(new Node.JoinSys(group.get(2),id), ActorRef.noSender()); // get(1) to keep it in the test list
//        id++;

        wait.wait(2000);

        group.get(1).tell(new Node.Update('A', 1), ActorRef.noSender());

        wait.wait(2000);

        group.get(0).tell(new Node.Update('Z', 1), ActorRef.noSender());

        wait.wait(2000);

        group.get(2).tell(new Node.Update('A', 2), ActorRef.noSender());


        //------------------TESTING------------------

//        wait.wait(10000);

        for (ActorRef peer: group) {
            peer.tell(new Node.PrintGroup(), ActorRef.noSender());
        }

        wait.wait(2000);

        for (ActorRef peer: group) {
            peer.tell(new Node.PrintData(), ActorRef.noSender());
        }
        System.out.println((int)'Z');

        system.terminate();
    }
}