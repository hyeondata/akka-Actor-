package org.example;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.ActorRef;

public class Greeter extends AbstractActor {
    public static Props props(String message, ActorRef printerActor) {
        return Props.create(Greeter.class, () -> new Greeter(message, printerActor));
    }

    static public class Greet {
        public Greet() {}
    }

    private final String message;
    private final ActorRef printerActor;

    public Greeter(String message, ActorRef printerActor) {
        this.message = message;
        this.printerActor = printerActor;
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Greet.class, greet -> {
                    Thread.sleep(3000);
                    printerActor.tell(new Printer.Greeting(message), getSelf());
                })
                .build();
    }
}