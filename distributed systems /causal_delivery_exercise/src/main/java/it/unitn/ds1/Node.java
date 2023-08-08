package it.unitn.ds1;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Node extends AbstractActor {

    private final int id;         // ID of the current actor
    private List<ActorRef> group;

    public Node(int id) {
        this.group = new ArrayList<>();
        this.id = id;
    }

    static public Props props(int id) {
        return Props.create(Node.class, () -> new Node(id));
    }

    public static class JoinSys implements Serializable {
        private final ActorRef newMember;
        private final int id;

        public JoinSys(ActorRef newMember, int id) {
            this.newMember = newMember;
            this.id = id;
        }


    }

    public static class NewMember implements Serializable {
        private final ActorRef node;
        private final int id;

        public NewMember(ActorRef node, int id) {
            this.node = node;
            this.id = id;
        }
    }

    public static class UpdateMemberList implements Serializable {
        private final List<ActorRef> group;

        public UpdateMemberList(List<ActorRef> group) {
            this.group = List.copyOf(group);
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JoinSys.class,    this::onJoinSys)
                .match(NewMember.class, this::onNewMember)
                .match(UpdateMemberList.class, this::onUpdateMemberList)
                .match(PrintGroup.class, this::onPrintGroup)
                .build();
    }

    private void onJoinSys(JoinSys msg) {
//        System.out.println("id : " + msg.id + " newMember: " + msg.newMember);
        this.group.add(msg.newMember);


        for(ActorRef node : this.group) {
            node.tell(new Node.NewMember(msg.newMember, msg.id), ActorRef.noSender());
        }
//        System.out.println("Node " + this.id + " joined the system");
//        System.out.println("Node " + this.id + " group: " + this.group + "joining the system");
        msg.newMember.tell(new UpdateMemberList(this.group), ActorRef.noSender());
    }

    private void onNewMember(NewMember msg) {
//        System.out.println("id : " + msg.id + " newMember: " + msg.node);
        if (this.group.contains(msg.node)) {
//            System.out.println("Node " + msg.id + " already in the system");
            return;
        }
        this.group.add(msg.node);
//        System.out.println("Node " + this.id + " group: " + this.group);

    }

    public void onUpdateMemberList(UpdateMemberList msg) {
//        this.group = msg.group;
        for (ActorRef node : msg.group) {
            if (!this.group.contains(node)){
                this.group.add(node);
            }
        }
//        System.out.println("Node " + this.id + " group: " + this.group);
    }

    //----------------------------------------------------------testing----------------------------------------------------------

    public static class PrintGroup implements Serializable {
        public PrintGroup() {}
    }

    private void onPrintGroup(PrintGroup msg) {
        System.out.println("Node " + this.id + " group: " + this.group);
    }

}
