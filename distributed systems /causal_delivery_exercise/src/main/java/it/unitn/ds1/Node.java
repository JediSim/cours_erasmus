package it.unitn.ds1;

import akka.actor.AbstractActor;
import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.Props;
import scala.Int;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Node extends AbstractActor {

    private final int id;         // ID of the current actor

    private ActorRef selfRef;
    private List<ActorRef> group;

    private  List<Pair> data = new ArrayList<>();

    private final Integer N = 1; // number of replicas

//    TODO
    private Integer R = 1;

    private List<Pair> getRespond = new ArrayList<>();

//    TODO
    private Integer W;

    public Node(int id) {
        this.group = new ArrayList<>();
        this.id = id;
    }

//    static public Props props(int id) {
//        return Props.create(Node.class, () -> new Node(id));
//    }
    static public Props props(int id) {return Props.create(Node.class, () -> new Node(id) );}

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

    public static class LeaveSys implements Serializable {
        private final ActorRef node;
        public LeaveSys(ActorRef node){
            this.node = node;
        }
    }

    public static class LeavingMember implements Serializable {
        private final ActorRef node;
        public LeavingMember(ActorRef node){
            this.node = node;
        }
    }

    public static class UpdateMemberList implements Serializable {
        private final List<ActorRef> group;
        private final ActorRef selfRef;

        public UpdateMemberList(List<ActorRef> group, ActorRef selfRef) {

            this.group = List.copyOf(group);
            this.selfRef = selfRef;
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
            this.newData = new Pair(newData.getKey(), newData.getValue());
        }
    }

    public static class Get implements Serializable {
        private final Pair dataSearch;

        public Get(Character key) { this.dataSearch = new Pair(key,0); }
    }

    public static class GetReplica implements Serializable {
        private final Pair dataSearch;

        private final ActorRef replyTo;

        public GetReplica(Pair pair, ActorRef replyTo) {
            this.dataSearch = pair;
            this.replyTo = replyTo;
        }
    }

    public static class UpdateForJoinOrLeave implements Serializable {
        private final Pair data;

        public UpdateForJoinOrLeave(Pair data){ this.data = new Pair(data.getKey(), data.getValue(), data.getVersion()); }
    }

    public static class UpdateReplicaForJoinOrLeave implements Serializable {
        private final Pair data;

        public UpdateReplicaForJoinOrLeave(Pair data){ this.data = new Pair(data.getKey(), data.getValue(), data.getVersion()); }
    }

    public static class GetReplicaRespond implements Serializable {
        private final Pair data;

        public GetReplicaRespond(Pair data){ this.data = new Pair(data.getKey(), data.getValue(), data.getVersion()); }
    }

    public static class Recovery implements Serializable {

        public Recovery(){}
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(JoinSys.class,    this::onJoinSys)
                .match(NewMember.class, this::onNewMember)
                .match(LeaveSys.class, this::onLeaveSys)
                .match(LeavingMember.class, this::onLeavingMember)
                .match(UpdateMemberList.class, this::onUpdateMemberList)
                .match(Update.class, this::onUpdate)
                .match(UpdateReplica.class, this::onUpdateReplica)
                .match(UpdateForJoinOrLeave.class, this::onUpdateForJoinOrLeave)
                .match(UpdateReplicaForJoinOrLeave.class, this::onUpdateReplicaForJoinOrLeave)
                .match(Get.class, this::onGet)
                .match(GetReplica.class, this::onGetReplica)
                .match(GetReplicaRespond.class, this::onGetReplicaRespond)
                .match(Recovery.class, this::onRecovery)
                .match(PrintGroup.class, this::onPrintGroup)
                .match(PrintData.class, this::onPrintData)
                .build();
    }

    private void onJoinSys(JoinSys msg) {
        this.group.add(msg.newMember);
        Collections.sort(this.group, new ActorRefComparator());

        for(ActorRef node : this.group) {
            node.tell(new Node.NewMember(msg.newMember, msg.id), ActorRef.noSender());
        }
        msg.newMember.tell(new UpdateMemberList(this.group, msg.newMember), ActorRef.noSender());
    }

    private void onNewMember(NewMember msg) {
        if (this.group.contains(msg.node)) {
            this.updateOnJoinOrLeave();
            return;
        }
        this.group.add(msg.node);
        Collections.sort(this.group, new ActorRefComparator());
        this.updateOnJoinOrLeave();


    }

    private void onLeaveSys(LeaveSys msg) {
        this.group.remove(msg.node);
        Collections.sort(this.group, new ActorRefComparator());
        this.updateOnJoinOrLeave();

        for(ActorRef node: this.group){
            node.tell(new Node.LeavingMember(msg.node), ActorRef.noSender());
        }
    }

    private void onLeavingMember(LeavingMember msg){
        if(!this.group.contains(msg.node)){
            return ;
        }
        this.group.remove(msg.node);
        Collections.sort(this.group, new ActorRefComparator());
        this.updateOnJoinOrLeave();
    }

    public void onUpdateMemberList(UpdateMemberList msg) {
        for (ActorRef node : msg.group) {
            if (!this.group.contains(node)){
                this.group.add(node);
            }
        }
        this.selfRef = msg.selfRef;
        Collections.sort(this.group, new ActorRefComparator());
    }

    private void onUpdate(Update msg) {
        int index = this.group.indexOf(this.selfRef);
        Double i = (26.0/this.group.size()*index) + 65;
        Double j = (26.0/this.group.size()*(index+1) + 65);
        List<ActorRef> nNext = this.getNNext(this.group.indexOf(this.selfRef));
        if ((int)msg.newData.getKey() >= i && (int)msg.newData.getKey() < j) {
            if (this.data.contains(msg.newData)) {
//                this.data.set(this.data.indexOf(msg.newData), msg.newData);
                this.data.get(this.data.indexOf(msg.newData)).setValue(msg.newData.getValue());
//                this.data.get(this.data.indexOf(msg.newData)).setVersion(this.data.get(this.data.indexOf(msg.newData)).getVersion()+1);
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
            this.data.get(this.data.indexOf(msg.newData)).setValue(msg.newData.getValue());
//            this.data.get(this.data.indexOf(msg.newData)).setVersion(this.data.get(this.data.indexOf(msg.newData)).getVersion()+1);
        }else {
            this.data.add(msg.newData);
        }
    }

    private void onUpdateForJoinOrLeave(UpdateForJoinOrLeave msg) {
        int index = this.group.indexOf(this.selfRef);
        Double i = (26.0/this.group.size()*index) + 65;
        Double j = (26.0/this.group.size()*(index+1) + 65);
        List<ActorRef> nNext = this.getNNext(this.group.indexOf(this.selfRef));
        if ((int)msg.data.getKey() >= i && (int)msg.data.getKey() < j) {
            if (this.data.contains(msg.data)) {
                this.data.set(this.data.indexOf(msg.data), msg.data);
            }else {
                this.data.add(msg.data);
            }
            for (ActorRef node : nNext) {
                node.tell(new UpdateReplicaForJoinOrLeave(msg.data), ActorRef.noSender());
            }
        }else{
            nNext.get(0).tell(new UpdateForJoinOrLeave(msg.data), ActorRef.noSender());
        }
    }

    private void onUpdateReplicaForJoinOrLeave(UpdateReplicaForJoinOrLeave msg) {
        if (this.data.contains(msg.data)) {
            this.data.set(this.data.indexOf(msg.data), msg.data);
        }else {
            this.data.add(msg.data);
        }
    }

    private void onGet(Get msg) {
        List<ActorRef> nNode = getNode(msg.dataSearch.getKey());
        for (ActorRef node : nNode) {
            node.tell(new GetReplica(msg.dataSearch, this.selfRef), getSelf());
        }

    }

    private void onGetReplica(GetReplica msg) {
        System.out.println("get : " + this.data.get(this.data.indexOf(msg.dataSearch)));
        getSender().tell(new GetReplicaRespond(this.data.get(this.data.indexOf(msg.dataSearch))), ActorRef.noSender());
    }

    private void onGetReplicaRespond(GetReplicaRespond msg) {
        this.getRespond.add(msg.data);

        if (this.getRespond.size() == R){
            Pair res = this.getRespond.get(0);
            for (Pair pair: this.getRespond){

                if (res.getVersion() >= pair.getVersion()){
                    res = pair;
                }
                res = pair;
            }
            System.out.println("result of get : " + res);
            this.getRespond.clear();
        }
    }

    private void onRecovery(Recovery msg){
        ActorRef node = this.group.get(0);
        Wait wait = new Wait();

        node.tell(new LeaveSys(this.selfRef), ActorRef.noSender());

        wait.wait(2000);

        node.tell(new JoinSys(this.selfRef, this.id), ActorRef.noSender());
    }

    private List<ActorRef> getNNext(Integer index) {
        List<ActorRef> nNext = new ArrayList<>();
        Integer i = 1;
        Integer n = this.group.size();
        Integer j;
        for (ActorRef node : this.group) {
            j = (index + i) % n;
            if (j < n){
                nNext.add(this.group.get(j));
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
        int index;
        for (ActorRef node : this.group) {
            index = this.group.indexOf(node);
            i = (26.0/this.group.size()*index) + 65;
            j = (26.0/this.group.size()*(index+1) + 65);
            if ((int)key >= i && (int)key < j){
                nNode.add(node);
                nNode.addAll(getNNext(index));
            }
        }
        return nNode;
    }

    private void updateOnJoinOrLeave(){
        List<Pair> tmpData = List.copyOf(this.data);
        this.data.clear();

        for (Pair data : tmpData) {
            onUpdateForJoinOrLeave(new UpdateForJoinOrLeave(data));
        }
    }

    class ActorRefComparator implements Comparator<ActorRef> {
        @Override
        public int compare(ActorRef a, ActorRef b){
            String idA;
            String idB;
            idA = a.toString().split("#")[0];
            idA = idA.split("node")[1];
            idB = b.toString().split("#")[0];
            idB = idB.split("node")[1];
            return Integer.parseInt(idA) - Integer.parseInt(idB);

        }
    }

    public void setSelfRef(ActorRef selfRef){
        this.selfRef = selfRef;
    }

    //----------------------------------------------------------testing----------------------------------------------------------

    public static class PrintGroup implements Serializable {
        public PrintGroup() {}
    }

    private void onPrintGroup(PrintGroup msg) {
        System.out.println("selfRef : " + this.selfRef + "id : "+this.id);
        System.out.println("Node " + this.id + " group: " + this.group);
    }

    public static class PrintData implements Serializable {
        public PrintData() {}
    }

    private void onPrintData(PrintData msg) {
        System.out.println("Node " + this.id + " data: " + this.data);
    }

}
