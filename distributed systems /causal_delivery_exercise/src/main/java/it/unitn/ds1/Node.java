package it.unitn.ds1;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import scala.Int;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node extends AbstractActor {

    private final int id;         // ID of the current actor
    private List<ActorRef> group;

    private List<Pair> data = new ArrayList<>();

    private Integer N = 1; // number of replicas

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

    public static class Update implements Serializable {

        private final Pair newData;

        public Update(Character key, Integer value) {
            this.newData = new Pair(key, value);
        }
    }

    public static class UpdateReplica implements Serializable {

        private final Pair newData;

        public UpdateReplica(Pair newData) {
            this.newData = newData;
        }
    }

    public static class Get implements Serializable {
        private final Pair dataSearch;

        public Get(Character key) { this.dataSearch = new Pair(key,0); }
    }

    public static class GetReplica implements Serializable {
        private final Pair dataSearch;

        public GetReplica(Pair pair) { this.dataSearch = pair; }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JoinSys.class,    this::onJoinSys)
                .match(NewMember.class, this::onNewMember)
                .match(UpdateMemberList.class, this::onUpdateMemberList)
                .match(Update.class, this::onUpdate)
                .match(UpdateReplica.class, this::onUpdateReplica)
                .match(Get.class, this::onGet)
                .match(GetReplica.class, this::onGetReplica)
                .match(PrintGroup.class, this::onPrintGroup)
                .match(PrintData.class, this::onPrintData)
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
            this.updateOnJoin();
            return;
        }
        this.group.add(msg.node);
//        System.out.println("Node " + this.id + " group: " + this.group);
        this.updateOnJoin();


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

    private void onUpdate(Update msg) {
        Double i = (26.0/this.group.size()*this.id) + 65;
        Double j = (26.0/this.group.size()*(this.id+1) + 65);
//        System.out.println("Node " + this.id + " received update request for key " + msg.newData.getKey() + " i : " + i + " j : " + j);
        List<ActorRef> nNext = this.getNNext(this.id);
        if ((int)msg.newData.getKey() >= i && (int)msg.newData.getKey() < j) {
            if (this.data.contains(msg.newData)) {
                this.data.set(this.data.indexOf(msg.newData), msg.newData);
            }else {
                this.data.add(msg.newData);
            }
            for (ActorRef node : nNext) {
                node.tell(new UpdateReplica(msg.newData), ActorRef.noSender());
            }
        }else{
            nNext.get(0).tell(new Update(msg.newData.getKey(), msg.newData.getValue()), ActorRef.noSender());
        }
    }

    private void onUpdateReplica(UpdateReplica msg) {
        if (this.data.contains(msg.newData)) {
            this.data.set(this.data.indexOf(msg.newData), msg.newData);
        }else {
            this.data.add(msg.newData);
        }
    }

    private void onGet(Get msg) {
        List<ActorRef> nNode = getNode(msg.dataSearch.getKey());
        for (ActorRef node : nNode) {
            node.tell(new GetReplica(msg.dataSearch), ActorRef.noSender());
        }
    }

    private void onGetReplica(GetReplica msg) {
        System.out.println("get : " + this.data.get(this.data.indexOf(msg.dataSearch)));
    }

    private List<ActorRef> getNNext(Integer id) {
        List<ActorRef> nNext = new ArrayList<>();
        Integer i = 1;
        Integer n = this.group.size();
        Integer j;
        Pattern p;
        Matcher m;
        for (ActorRef node : this.group) {
            j = (id + i) % n;
            p = Pattern.compile(".*node"+j+".*");
            m = p.matcher(node.toString());
            if (m.find()){
                nNext.add(node);
                if (i == this.N){
                    return nNext;
                }
                i++;
            }
        }
        return nNext;
    }

    private List<ActorRef> getNode(Character key) {
        List<ActorRef> nNode = new ArrayList<>();
        Double i;
        Double j;
        String id;
        for (ActorRef node : this.group) {
            id = node.toString().split("#")[0];
            id = id.split("node")[1];
            i = (26.0/this.group.size()*Integer.parseInt(id)) + 65;
            j = (26.0/this.group.size()*(Integer.parseInt(id)+1) + 65);
            if ((int)key >= i && (int)key < j){
                nNode.add(node);
                nNode.addAll(getNNext(Integer.parseInt(id)));
            }
        }
        return nNode;
    }

    private void updateOnJoin(){
        List<Pair> tmpData = List.copyOf(this.data);
        this.data.clear();

        for (Pair data : tmpData) {
            onUpdate(new Update(data.getKey(), data.getValue()));
        }
    }

    //----------------------------------------------------------testing----------------------------------------------------------

    public static class PrintGroup implements Serializable {
        public PrintGroup() {}
    }

    private void onPrintGroup(PrintGroup msg) {
        System.out.println("Node " + this.id + " group: " + this.group);
    }

    public static class PrintData implements Serializable {
        public PrintData() {}
    }

    private void onPrintData(PrintData msg) {
        System.out.println("Node " + this.id + " data: " + this.data);
    }

}
